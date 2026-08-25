/*
  Parallel Programming - 2026/2027

  Common variables, constants and types
*/

#include <stdbool.h>

typedef struct {
  int id, processes;
  bool initiator;
} thread_args_t;
