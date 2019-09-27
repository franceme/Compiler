#include <stdio.h>
int main (int argc, char *argv[])
{
	char name[20];
	printf("Hello, Whats Your Name Son?\n");

	fgets(name,20,stdin);
 	printf("Hi there, %s", name);
 	return 0;
}
