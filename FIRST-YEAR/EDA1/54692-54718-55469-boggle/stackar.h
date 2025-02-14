typedef char* ElementType;

#ifndef _Stack_h
#define _Stack_h

struct StackRecord
{
    int Capacity;
    int TopOfStack;
    ElementType *Array;
};

typedef struct StackRecord *Stack;


Stack CreateStack( int MaxElements );
void DisposeStack( Stack S );

int IsEmpty( Stack S );
int IsFull( Stack S );
void MakeEmpty( Stack S );

void Push( ElementType X, Stack S );
void printstack (Stack S);
ElementType Top( Stack S );
ElementType Pop( Stack S );

#endif  /* _Stack_h */