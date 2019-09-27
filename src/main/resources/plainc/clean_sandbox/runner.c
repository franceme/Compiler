#define DEBUG
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

void printMEM(struct _mem mem)
{
	if (mem._type == INT)
		printf("%d",mem._int);
	else if (mem._type == FLOAT)
		printf("%f",mem._float);
	else if (mem._type == BOOL)
		printf("%s",mem._bool == true ? "true" : "false");
	else if (mem._type == CHAR)
		printf("%c",mem._char);
	else//if (classType == STRING)
		printf("%s",mem._str);
}

int main(void)
{
	#ifdef DEBUG
		int _size=1;
	#endif
auto k='h';
printf("%c",k);
	struct _mem **_MM;
	struct _mem R[2];
	bool _initialized=false;

	//struct _mem test=construct(INT,dai);

	if (!_initialized)
		goto _ini;

	_start:

	_ini:
		if (_initialized)
			goto _free;

		_MM = (struct _mem **) malloc (_size * sizeof(struct _mem));

		#ifdef DEBUG
			int _testKTR=89;
		#endif

		_initialized=true;
		goto _start;

	_free:
		free(_MM);
	
	return 0 ;
}
