
ALG ?= safra

CONTROL_ALGORITHM = $(ALG).c

# add extra flags here
XCFLAGS = 

CC = mpicc
CFLAGS = -std=gnu11 -Wall -Wno-parentheses -g3 $(XWARN) $(XCFLAGS)

#LDLIBS = -lrt

XWARN = # -Wextra -Wconversion


C_FILES = termination.c basic.c util.c $(CONTROL_ALGORITHM)
O_FILES = $(addsuffix .o, $(basename $(C_FILES)))

H_FILES = global.h util.h basic.h control.h

termination : $(O_FILES)


# dependencies

$(O_FILES) : $(H_FILES)

$(addsuffix .o, $(basename $(CONTROL_ALGORITHM))) : control.h


clean :
	@$(RM) $(O_FILES) termination
