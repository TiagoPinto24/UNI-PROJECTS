/*
  Parallel Programming - 2026/2027

  Basic algorithm
*/

#include <mpi.h>
#include <pthread.h>

#include <stdio.h>
#include <stdbool.h>
#include <unistd.h>

#include "global.h"
#include "basic.h"

#include "util.h"
#include "control.h"

#define SEND_BASIC_MESSAGE_PROBABILITY	50
#define BECOME_PASSIVE_PROBABILITY	30

#define WORK_TIME_MIN 500		// ms
#define WORK_TIME_MAX 1500		// ms

#define BASIC_MESSAGE 1			// basic message tag

// a process is either active or passive
typedef enum { ACTIVE, PASSIVE } process_state_t;

// basic messages
typedef struct { int value; } basic_message_t;

static int pid;				// this process id

static bool is_initiator = false;
static process_state_t state = PASSIVE;

/*
  Process with id ID becomes active.
*/
static void basic_set_active(int id)
{
  state = ACTIVE;

  trace("%d: is ACTIVE\n", id);

  control_become_active_hook(id);
}

/*
  Process with id ID becomes passive.
*/
static void basic_set_passive(int id)
{
  state = PASSIVE;

  trace("%d: is PASSIVE\n", id);

  control_become_passive_hook(id);
}

/*
  Active process with id ID becomes passive with probability
  BECOME_PASSIVE_PROBABILITY.
*/
static process_state_t basic_new_state(int id)
{
  if (rand_ab(1, 100) <= BECOME_PASSIVE_PROBABILITY)
    basic_set_passive(id);

  return state;
}

/*
  Receive a basic message. The process is blocked until a message is
  received.

  The contents of the message are stored at address PTR and the id of
  the sender process is stored at PEER.
*/
static void basic_receive_message(int id, basic_message_t *ptr, int *peer)
{
  MPI_Status status;

  MPI_Recv(ptr, sizeof(basic_message_t), MPI_BYTE, MPI_ANY_SOURCE,
	   BASIC_MESSAGE, MPI_COMM_WORLD, &status);

  *peer = status.MPI_SOURCE;

  control_basic_receive_hook(id, *peer);
}

/*
  Receive a basic message, in case there is a pending message
  (non-blocking).

  If there is a pending message, the contents of the message are
  stored at address PTR, the id of the sender process is stored at
  PEER and return true.

  Otherwise, return false.
 */
static bool basic_receive_message_maybe(int id, basic_message_t *ptr, int *peer)
{
  int flag;
  MPI_Status status;

  MPI_Iprobe(MPI_ANY_SOURCE, BASIC_MESSAGE, MPI_COMM_WORLD, &flag, &status);

  if (flag == 0)
    return false;

  MPI_Recv(ptr, sizeof(basic_message_t), MPI_BYTE, MPI_ANY_SOURCE,
	   BASIC_MESSAGE, MPI_COMM_WORLD, &status);

  *peer = status.MPI_SOURCE;

  control_basic_receive_hook(id, *peer);

  return true;
}

/*
  Send a basic message, with probability
  SEND_BASIC_MESSAGE_PROBABILITY, to a random process.

  PROCESSES is the total number of processes running the basic
  algorithm.
*/
static void basic_send_message_maybe(int id, int processes)
{
  static int msg_no = 0;
  int peer;
  basic_message_t message;

  if (rand_ab(1, 100) > SEND_BASIC_MESSAGE_PROBABILITY)
    return;

  message.value = ++msg_no;

  // choose the process to send the message to
  do
    peer = rand_ab(0, processes - 1);
  while (peer == id);

  control_basic_send_hook(id, peer);

  MPI_Send(&message, sizeof(basic_message_t), MPI_BYTE, peer, BASIC_MESSAGE,
	   MPI_COMM_WORLD);

  trace("%d: sent %d to %d\n", id, message.value, peer);
}

/*
  The basic algorithm:

  - An initiator process is initially active, a non-initiator process
    is initially passive.

  - While a process is active:
    - It sends a message to some other process, with probability
      SEND_BASIC_MESSAGE_PROBABILITY;
    - It becomes passive with probability BECOME_PASSIVE_PROBABILITY.

  - A passive process waits to be sent a message and the becomes
    active.

  The arguments of the basic algorithm are the ID of the process, the
  number of PROCESSES running the algorithm, and whether the process
  is an initiator.
*/
void *basic_algorithm(void *_args)
{
  thread_args_t *args = _args;
  int id = args->id;
  int processes = args->processes;
  bool initiator = args->initiator;

  basic_message_t message;

  pid = id;

  is_initiator = initiator;

  // tell the control algorithm that the basic algorithm has started;
  // allows the synchronisation of both algorithms
  control_basic_start_hook(id);

  if (!is_initiator)
    {
      int peer;

      // wait for a message
      basic_receive_message(id, &message, &peer);

      trace("%d: got %d from %d\n", id, message.value, peer);
    }

  // process becomes active
  basic_set_active(id);

  while (true)
    {
      if (state == ACTIVE)
	{
	  // may send a message to another process
	  basic_send_message_maybe(id, processes);

	  // may become passive
	  state = basic_new_state(id);
	}

      if (state == PASSIVE)
	{
	  int peer;

	  // wait for an incoming message
	  basic_receive_message(id, &message, &peer);

	  trace("%d: got %d from %d\n", id, message.value, peer);

	  // process becomes active
	  basic_set_active(id);
	}
      else
	{
	  int peer;

	  // see if there's an incoming message
	  if (basic_receive_message_maybe(id, &message, &peer))
	    trace("%d: got %d from %d\n", id, message.value, peer);
	}

      // let some time go by
      rand_sleep(WORK_TIME_MIN, WORK_TIME_MAX);
    }
}
