typedef enum {false,true}bool;

typedef enum {INT,FLOAT,BOOL,CHAR,STR}_class;

#define sin(T)		\
	struct sin_##T	\
	{		\
		T value;\
	}

#define new_sin(T)						\
({								\
	struct sin_##T *v = malloc(sizeof(sin(T))+sizeof(T));	\
	printf("Caught Here\n");					\
	v;							\
})

#define foo(x)                                                            \
({                                                            \
    _class tmp;                                             \
        if (__builtin_types_compatible_p (typeof (x),int )) \
            tmp=INT;                                               \
    tmp;                                                        \
})
