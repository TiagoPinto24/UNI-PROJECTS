/*
  Parallel Programming - 2026/2027

  Skeleton of an implementation of a termination detection algorithm
*/

#include <mpi.h>
#include <pthread.h>

#include <stdio.h>
#include <stdbool.h>
#include <stddef.h>
#include <unistd.h>

#include "global.h"
#include "control.h"
#include "util.h"

#define TOKEN_MESSAGE 23
#define TERMINATE_MESSAGE 24 

#define WHITE 1
#define BLACK 0

//Token and it's costum MPI type
typedef struct {
    int count;
    int color;   
} token;
static MPI_Datatype MPI_TOKEN_TYPE;

//Process values
static int proc_color = WHITE; 
static int proc_count = 0; 
static bool active = false; 

//Helper variables to control the token
static token saved_token;
static bool has_token = false;

//ID of the next process
static int next;

/*
  Helper function that initializes the type of the token for MPI operations
*/
static void init_token_type()
{
    int blocklengths[2] = {1, 1};
    MPI_Datatype types[2] = {MPI_INT, MPI_INT};
    MPI_Aint offsets[2];

    offsets[0] = offsetof(token, count);
    offsets[1] = offsetof(token, color);

    MPI_Type_create_struct(2, blocklengths, offsets, types, &MPI_TOKEN_TYPE);
    MPI_Type_commit(&MPI_TOKEN_TYPE);
}

/*
  Helper function to psuh the token to the next process
*/
void forward_token(int id, token t) {
    if (id == 0)
    {
        t.color = WHITE;
        t.count = 0;
    }
    
    t.count += proc_count;

        if (proc_color == BLACK)
        {
            t.color = BLACK;
            proc_color = WHITE;
        }

        
        MPI_Send(&t, 1, MPI_TOKEN_TYPE,
                next, TOKEN_MESSAGE,
                MPI_COMM_WORLD);
        has_token = false;
        
}

/*
  Main loop of the termination detection algorithm.

  When termination is detected, it must end (returning NULL or another
  appropriate value), which signals the main thread that the basic
  algorithm has terminated.

  The arguments of the termination detection algorithm are the ID of
  the process, the number of PROCESSES running the basic algorithm,
  and whether the process is an initiator.
*/
void *detect_termination(void *_args)
{
    thread_args_t *args = _args;
    int id = args->id;
    int processes = args->processes;
    next = (id + 1) % processes;

    token token;

    static int initialized = 0;
    if (!initialized) {
        init_token_type(id);
        initialized = 1;
    }

    if (id == 0) {
        token.color = WHITE;
        token.count = 0;
        MPI_Send(&token, 1, MPI_TOKEN_TYPE,
                 next, TOKEN_MESSAGE, MPI_COMM_WORLD);
    }

   
    while (1) {       
        MPI_Status status;
        MPI_Probe(MPI_ANY_SOURCE, MPI_ANY_TAG, MPI_COMM_WORLD, &status);

        if (status.MPI_TAG == TOKEN_MESSAGE) {
            has_token = true;
            MPI_Recv(&token, 1, MPI_TOKEN_TYPE,
                    status.MPI_SOURCE, TOKEN_MESSAGE,
                    MPI_COMM_WORLD, MPI_STATUS_IGNORE);
            
            saved_token = token;
            if (!active) {
                if (id == 0 && !active && token.color == WHITE && token.count == 0) {
                    int msg = 1;
                    for (int i = 0; i < processes; i++) {
                        MPI_Send(&msg, 1, MPI_INT,
                                i, TERMINATE_MESSAGE,
                                MPI_COMM_WORLD);
                    }
                    return NULL;
                }

                forward_token(id, token);
            }
    
        }
        else if(status.MPI_TAG == TERMINATE_MESSAGE) {

            int msg;
            MPI_Recv(&msg, 1, MPI_INT,
                    status.MPI_SOURCE, TERMINATE_MESSAGE,
                    MPI_COMM_WORLD, MPI_STATUS_IGNORE);

            return NULL;
        }     
    }

}

/*
  Called at startup of the basic algorithm in process ID.
*/
void control_basic_start_hook(int id)
{
    proc_color = WHITE;
    proc_count = 0;
}

/*
  Called when the basic algorithm process ID becomes passive.
*/
void control_become_passive_hook(int id)
{
    
    active = false;
    if (has_token)
    {
        forward_token(id,saved_token);
    }
}

/*
  Called when the basic algorithm process ID becomes active.
*/
void control_become_active_hook(int id)
{
    active = true;
}

/*
  Called when the basic algorithm process ID sends a basic message to
  process PEER.
*/
void control_basic_send_hook(int id, int peer)
{
    proc_count++;
    if (id>peer)
    {
        proc_color = BLACK;
    }    
}

/*
  Called when the basic algorithm process ID receives a basic message
  from process PEER.
*/
void control_basic_receive_hook(int id, int peer)
{
    
    proc_count--;
}