//#define MM
//#define R
//#define STR
//#define OLD
#include <stdio.h>
#include <stdlib.h>

typedef enum {false,true}bool;

typedef enum {INT,FLOAT,BOOL,CHAR,STR}_class;

struct _mem
{
	union 
	{
		int _int;
		float _float;
		bool _bool;
		char _char;
		char* _str;
	};

	_class _type;
};

struct _mem construct(_class classType,void* memLoc)
{
	struct _mem _new;

	if (classType == INT)
		_new._int = *(int *)memLoc;
	else if (classType == FLOAT)
		_new._int = *(float *)memLoc;
	else if (classType == BOOL)
		_new._bool = *(bool *)memLoc;
	else if (classType == CHAR)
		_new._char = *(char *)memLoc;
	else//if (classType == STRING)
		_new._str = *(char *)memLoc;

	return _new;
};

void printMEM(struct _mem *mem)
{
	if (*mem._type == INT)
		printf("%d",*mem._int);
	else if (*mem._type == FLOAT)
		printf("%f",*mem._float);
	else if (*mem._type == BOOL)
		printf("%s",*mem._bool == true ? "true" : "false");
	else if (*mem._type == CHAR)
		printf("%c",*mem._char);
	else//if (classType == STRING)
		printf("%s",*mem._str);
}

int main(void)
{
	int kai=69;
	int hai=0;
	void* dai=&kai;
	_class zai=INT;
	struct _mem test=construct(zai,dai);

	dai=&hai;

	bool _initialized=false;
	#ifdef MM
		void** _MM;
	#endif

	#ifdef R
		void* _R[1];
	#endif

	#ifdef OLD
		int *_point;
		void *_point2[1];
		int _testRedirect=7;
	#endif

	#ifdef STR
		char *_str;
	#endif

	if (!_initialized)
		goto _ini;

	_start:
		_notworking:
			#ifdef STR
				strcpy(_str,"tutorialspoint");
				printf("str : %s\n",_str);
			#endif

			#ifdef R
				printf("_R[0] : %d\n",_R[0]);
				*_R[0]++;
				printf("_R[0] : %d\n",_R[0]);
			#endif

			#ifdef MM
				printf("_MM(0) : %d\n",(*((int *)(*(_MM+0)) )));
			#endif

		_working:
			#ifdef OLD
				printf("_point : %d\n",*_point);
				*_point=6;
				printf("_point : %d\n",*_point);

				printf("_point 2: %d\n",*(int*)_point2[0]);
				*(int*)_point2[0]=6;
				printf("_point 2: %d\n",*(int*)_point2[0]);
				*(int*)_point2[0]=&_testRedirect;
			#endif

	_ini:
		if (_initialized)
			goto _free;

		#ifdef MM
			_MM = (void **) malloc (1 * sizeof(void*));
		#endif
		#ifdef STR
			_str = (char *) malloc(15);
		#endif
		_ini_alpha:;
			#ifdef OLD
				int _val = 10;
				int _test=4;
				int _test2=89;
			#endif

			#ifdef R
					_R[0]=_val;
			#endif

			#ifdef MM
				*(_MM+0)=&_val;
			#endif

			#ifdef OLD
				_point=&_test;
				_point2[0]=&_test2;
			#endif
		
		_initialized=true;
		goto _start;

	_free:
		#ifdef STR
		free(_str);
		#endif

		#ifdef MM
			free(_MM);
		#endif

	#ifdef R
		_R[0]=(int *)5;
	#endif

	printf("\n\nPrinting Raw\n");
	#ifdef OLD
		printf("Raw val : %d\n",_val);
		printf("Raw test : %d\n",_test);
		printf("Raw test2 : %d\n",_test2);
		printf("Raw Redirect : %d\n",_testRedirect);
	#endif

	return 0 ;
}
