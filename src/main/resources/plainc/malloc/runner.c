#include <stdio.h>

typedef enum {false,true}bool;

int main(void)
{
	bool initialized=false;
	char *str;

	if (!initialized)
		goto ini;

	start:
		strcpy(str,"tutorialspoint\n");
		printf("%s",str);

	ini:
		if (initialized)
			goto free;

		str = (char *) malloc(15);
		initialized=true;
		goto start;

	free:
		free(str);

	return(0);
}
