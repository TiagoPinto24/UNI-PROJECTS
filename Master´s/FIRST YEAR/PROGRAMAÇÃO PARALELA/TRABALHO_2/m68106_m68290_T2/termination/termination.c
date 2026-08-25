/*
  Parallel Programming - 2026/2027

  The main program
*/

#include <mpi.h>
#include <pthread.h>

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>

#include "global.h"
#include "basic.h"
#include "control.h"
#include "util.h"

/*
  Main program:

  - Initialise the MPI environment.
  - Process command line arguments.
  - Start the basic algorithm thread.
  - Start the control algorithm (termination detection) thread.
  - Wait for the control algorithm to terminate, signalling that the
    terminatation of the basic algorithm has been detected.
  - Cancel the basic algorithm thread.
*/
int main(int argc, char *argv[])
{
  // process data
  int rank, processes;

  int mpi_threads_support;

  // initialise MPI framework
  MPI_Init_thread(&argc, &argv, MPI_THREAD_MULTIPLE, &mpi_threads_support);

  MPI_Comm_rank(MPI_COMM_WORLD, &rank);		// get process id
  MPI_Comm_size(MPI_COMM_WORLD, &processes);	// get number of processes

  if (mpi_threads_support < MPI_THREAD_MULTIPLE && rank == 0)
    warn("%s: warning: threads not fully supported\n", argv[0]);

  if (processes < 2)
    warn("%s: warning: there must be at least 2 processes\n", argv[0]);

  // parse arguments
  int arg = 1;

  while (arg < argc)
    {
      // parse one argument
      if (strcmp(argv[arg], "--quiet") == 0)
	{
	  // silence debugging messages
	  QUIET = 1;
	}
      else if (strcmp(argv[arg], "-r") == 0)
	{
	  // re-seed random number generator
	  srand(time(0));
	}
      else
	warn("%s: unknown option `%s'\n", argv[0]);

      arg++;
    }

  // give all the processes a chance to start
  MPI_Barrier(MPI_COMM_WORLD);

  // the program
  if (rank == 0)
    printf("Starting basic algorithm\n");

  // arguments for the basic and the control algorithms
  thread_args_t threads_args = { rank, processes, rank == 0 };

  // start basic algorithm thread
  pthread_t basic_thread;

  if (pthread_create(&basic_thread, NULL, basic_algorithm, &threads_args) != 0)
    {
      perror("pthread_create (basic)");

      return 1;
    }
      
  // start control thread
  pthread_t control_thread;

  if (pthread_create(&control_thread, NULL, detect_termination, &threads_args) != 0)
    {
      perror("pthread_create (control)");

      return 2;
    }

  // wait for control thread to end
  pthread_join(control_thread, NULL);

  // stop (terminated) basic algorithm thread
  pthread_cancel(basic_thread);
  pthread_join(basic_thread, NULL);

  // wrap up
  MPI_Finalize();

  if (rank == 0)
    printf("Terminating\n");

  return 0;
}
