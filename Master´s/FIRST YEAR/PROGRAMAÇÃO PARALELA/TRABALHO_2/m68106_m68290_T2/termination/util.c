/*
  Parallel Programming - 2026/2027

  Utility functions
*/

#include <stdio.h>
#include <stdarg.h>
#include <stdlib.h>
#include <time.h>


// controls the output of debugging messages, set to 1 to have no
// messages
int QUIET = 0;


// returns a random integer in [A,B]
int rand_ab(int a, int b)
{
  return a + rand() % (b - a + 1);
}

// sleep between MIN and MAX ms
void rand_sleep(int min, int max)
{
  int ms = rand_ab(min, max);
  struct timespec ts = { ms / 1000, (ms % 1000) * 1000 };

  nanosleep(&ts, NULL);
}

// output a debugging message to stderr
void trace(char *format, ...)
{
  va_list args;

  if (QUIET)
    return;

  va_start (args, format);
  vfprintf(stderr, format, args);
  va_end (args);
}

// unconditionally output a message to stderr
void warn(char *format, ...)
{
  va_list args;

  va_start (args, format);
  vfprintf(stderr, format, args);
  va_end (args);
}
