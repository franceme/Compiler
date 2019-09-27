#include <stdio.h>
#include <stdlib.h>

typedef enum {false,true}bool;

typedef enum {INT,FLOAT,BOOL,CHAR,STR}_class;

int main(void)
{
	bool _initialized=false;
	void** _MM;
	void* _R[1];
	

	if (!_initialized)
		goto _ini;

	_start:
		_notworking:
				*_R[0]++;
				((*((int *) (*(_MM+0)) )))=(int *)_R[0];
				_R[0]=5;
				printf("_R[0] : %d\n",_R[0]);
				printf("_MM(0) : %d\n",(*((int *) (*(_MM+0)) )));

	_ini:
		if (_initialized)
			goto _free;
		
			_MM = (void **) malloc (1 * sizeof(void*));
		
		_ini_alpha:;
				int _val = 10;
					_R[0]=_val;
				*(_MM+0)=&_val;
			
		
		_initialized=true;
		goto _start;

	_free:
		free(_MM);
	
	_printing:;
		//printf("\n\nPrinting Raw\n");
		
	

	return 0 ;
}
