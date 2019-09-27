CFLAGS=-g2
default:: build

build: runner.c
	gcc $(CFLAGS) -o runner.o runner.c
	clear

mem: runner.o
	valgrind --leak-check=full ./runner.o

run: runner.o
	./runner.o

clean: runner.o
	rm runner.o
	clear
