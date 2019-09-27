#include <stdio.h>


typedef enum {false, true} bool;

int main(void)
{
	int temp=5;
	int temp2=5;
	bool tester=false;
	void* test[5];
	test[0]=&temp;
	test[1]=&temp2;
	test[2]=&tester;

	float toost=03;
	printf("0: %d\n",*((int*)test[0]));
	printf("1: %d\n",*((int*)test[1]));
	printf("2: %s\n",*((char*)test[2]) ? "true" : "false");
}
