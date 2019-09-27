CFLAGS=-g2 -std=c11
default:: build

build: runner.c
	gcc $(CFLAGS) -o runner.o runner.c

mem: runner.o
	valgrind --leak-check=full ./runner.o

run: runner.o
	./runner.o

clean: runner.o
	rm runner.o
