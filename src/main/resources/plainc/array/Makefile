default:: build

build: runner.c
	clear
	gcc -o runner.o runner.c
	clear

run: runner.o
	clear
	./runner.o

mem: runner.o
	valgrind --leak-check=full ./runner.o

clean: runner.o
	clear
	rm runner.o
	clear
