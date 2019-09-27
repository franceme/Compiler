int printf(const char *format,...);

int main (int argc, char *argv[])
{
	printf("\ncmdline args count=%d", argc);

 	/* First argument is executable name only */
 	printf("\nexe name=%s", argv[0]);

	int i=1;
	start:
		if (i>=argc) goto end;
		printf("\narg%d=%s", i, argv[i]);
		i++;
	if (i<argc) goto start;
	end:

 	printf("\n");
 	return 0;
}
