#include <stdio.h>
#include <stdlib.h>

#define MAIN() int main (int argc, char *argv[])

#define ARG(n) (((n)<=argc)? argv[n]: "inexistente")

#define ARG1 ARG(1)
#define ARG2 ARG(2)
#define ARG3 ARG(3)
#define ARG4 ARG(4)