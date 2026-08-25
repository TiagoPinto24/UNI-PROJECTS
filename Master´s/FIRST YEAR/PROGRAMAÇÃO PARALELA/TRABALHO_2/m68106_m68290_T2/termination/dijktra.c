/*
  Parallel Programming - 2026/2027

  Skeleton of an implementation of a termination detection algorithm
*/

#include <mpi.h>
#include <pthread.h>
#include <stdbool.h>

#include "global.h"
#include "control.h"
#include "util.h"

#define ACK_MESSAGE 2
#define MAX_PROCESSES 128

#define TERMINATE_MESSAGE 24

typedef struct
{
  int value;
} control_message_t;

static int id;
static bool initiator;

static bool active = false;

static int parent = -1;

static bool children[MAX_PROCESSES];
static int child_count = 0;
static bool termination_sent = false;  

static pthread_mutex_t lock = PTHREAD_MUTEX_INITIALIZER;

/*
  Adds a new child process to the dependency tree.
*/

static void add_child(int p)
{
  if (!children[p])
  {
    children[p] = true;
    child_count++;
  }
}

/*
  Removes a child process from the dependency tree.
*/

static void remove_child(int p)
{
  if (children[p])
  {
    children[p] = false;
    child_count--;
  }
}

/*
  Sends an acknowledgement message to the parent process.
*/

static void send_ack(int dst)
{
  control_message_t msg;
  msg.value = 1;

  MPI_Send(&msg,
           sizeof(msg),
           MPI_BYTE,
           dst,
           ACK_MESSAGE,
           MPI_COMM_WORLD);
}

/*
  Checks if the current process can terminate.
*/

bool try_finish()
{
  if (active)
    return false;

  if (child_count != 0)
    return false;

  if (parent != -1)
  {
    int p = parent;
    send_ack(p);
    parent = -1;
    return true;
  }

  if (initiator && id == 0)
  {
    if (!termination_sent) {
      termination_sent = true;
      return true;
    }
  }
  return false;
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
  id = args->id;
  int processes = args->processes;

  initiator = args->initiator;

  for (int i = 0; i < MAX_PROCESSES; i++)
  {
    children[i] = false;
  }

  active = initiator;
  termination_sent = false;

  MPI_Status status;
  control_message_t msg;

  while (true)
  {
    MPI_Probe(MPI_ANY_SOURCE, MPI_ANY_TAG, MPI_COMM_WORLD, &status);

    if (status.MPI_TAG == TERMINATE_MESSAGE)
    {
      MPI_Recv(&msg, 1, MPI_INT,
               status.MPI_SOURCE, TERMINATE_MESSAGE,
               MPI_COMM_WORLD, MPI_STATUS_IGNORE);
      return NULL;
    }
    else if (status.MPI_TAG == ACK_MESSAGE)
    {
      MPI_Recv(&msg,
               sizeof(msg),
               MPI_BYTE,
               status.MPI_SOURCE,
               ACK_MESSAGE,
               MPI_COMM_WORLD,
               &status);

      pthread_mutex_lock(&lock);

      remove_child(status.MPI_SOURCE);

      bool termination = try_finish();
      if (termination)
      {
        for (int i = 0; i < processes; i++)
        {
          if (i != id) {
            MPI_Send(&msg, 1, MPI_INT,
                     i, TERMINATE_MESSAGE,
                     MPI_COMM_WORLD);
          }
        }
        pthread_mutex_unlock(&lock);
        return NULL;
      }

      pthread_mutex_unlock(&lock);
    }
  }

  return NULL;
}

/*
  Called at startup of the basic algorithm in process ID.
*/

void control_basic_start_hook(int id_)
{
  id = id_;
}

/*
  Called when the basic algorithm process ID becomes active.
*/

void control_become_active_hook(int id_)
{
  pthread_mutex_lock(&lock);
  active = true;
  pthread_mutex_unlock(&lock);
}

/*
  Called when the basic algorithm process ID becomes passive.
*/

void control_become_passive_hook(int id_)
{
  pthread_mutex_lock(&lock);
  active = false;
  try_finish();
  pthread_mutex_unlock(&lock);
}

/*
  Called when the basic algorithm process ID sends a basic message to
  process PEER.
*/

void control_basic_send_hook(int id_,
                             int peer)
{
  pthread_mutex_lock(&lock);
  add_child(peer);
  pthread_mutex_unlock(&lock);
}

/*
  Called when the basic algorithm process ID receives a basic message
  from process PEER.
*/

void control_basic_receive_hook(int id_,
                                int peer)
{
  pthread_mutex_lock(&lock);

  if (parent == -1 && !initiator)
  {
    parent = peer;
  }
  else if (peer != parent)
  {
    send_ack(peer);
  }
  pthread_mutex_unlock(&lock);
}