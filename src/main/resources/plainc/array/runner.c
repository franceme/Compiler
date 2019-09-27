#include <stdio.h>
#include <stdlib.h>

typedef enum {false, true} bool;

int main(void)
{
	bool initialized=false;
	void** _MM;
	void* _R[2];

	if (!initialized)
		goto _ini;

	_start:;
		int _show=-1;
		_disp_show:
			_show++;

			/**
			 * 	int temp=5;
			 * 	void* test[5];
			 * 	test[0]=&temp;
			 * 	*((int*)test[0])
			 * */

			printf("Round %d : ",_show);
			printf("%d\n",(*( (int *) (*(_MM+_show))  ))      );

			if (_show<9)
				goto _disp_show;

	_ini:;
		if (initialized)
			goto _free;

		int _length=10;
		int _test[]={0,1,2,3,4,5,6,7,8,9};

		_MM = (void **) malloc (_length * sizeof(void*));
		
		int _alpha = -1;
		_ini_alpha:;
			_alpha++;

			*(_MM+_alpha)=&_test[_alpha];

			if (_alpha<9)
				goto _ini_alpha;

		initialized=true;
		goto _start;

	_free:;
		free(_MM);

	return(0);
}
