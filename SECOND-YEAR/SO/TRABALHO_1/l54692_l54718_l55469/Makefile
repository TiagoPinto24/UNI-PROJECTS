all: trabalho

trabalho: trabalho.o
	$(CC) -o $@ $^

clean:
	rm -f *.o trabalho

%.o: %.c
	$(CC) -c $^