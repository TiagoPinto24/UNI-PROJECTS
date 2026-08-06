#include "stackar.h"
#include "fatal.h"
#include <stdlib.h>
#include <stdio.h>
#include <stdlib.h>


#define EmptyTOS ( -1 )
#define MinStackSize ( 5 )

Stack CreateStack( int MaxElements )
{

    Stack S;	

	if( MaxElements < MinStackSize )
		Error( "Stack size is too small" );

	S = malloc( sizeof( struct StackRecord ) );
	if( S == NULL )
		FatalError( "Out of space!!!" );

	S->Array = malloc( sizeof( ElementType ) * MaxElements );
	if( S->Array == NULL )
		FatalError( "Out of space!!!" );

	S->Capacity = MaxElements;
	MakeEmpty( S );

	return S;
}

void DisposeStack( Stack S )
{
    if( S != NULL )
    {
        free( S->Array );
        free( S );
    }
}


int IsEmpty( Stack S )
{
	if (S->TopOfStack==-1)
		return 1;
	return 0;
}


int IsFull( Stack S )
{
	if (S->TopOfStack==S->Capacity-1)
		return 1;
	return 0;
}


void MakeEmpty( Stack S ){
	S->TopOfStack=EmptyTOS;
}
void Push( ElementType X, Stack S )
{
	if (IsFull(S))
		FatalError("Stack cheia");
	S->TopOfStack++;
	S->Array[S->TopOfStack]=malloc(sizeof(X));
	for (int i = 0; X[i]!='\0' ; i++)
        S->Array[S->TopOfStack][i]=X[i];
}


ElementType Top( Stack S )
{
	if (IsEmpty(S))
		FatalError("Está vazia");
	return S->Array[S->TopOfStack];
}


ElementType Pop( Stack S )
{
	ElementType aux=malloc(sizeof(S->Array[S->TopOfStack]));
	if (IsEmpty(S))
		FatalError("Está vazia");
	strcpy(aux,S->Array[S->TopOfStack]);
	S->TopOfStack--;
	return aux;
}
void printstack (Stack S){
	printf("[");
	for (int i = 0; i < S->TopOfStack; i++)
	{
		printf("%s ;",S->Array[i]);
	}
	if (IsEmpty(S))
	{
		printf("]\n");
	}
	else
		printf("%s ]\n",S->Array[S->TopOfStack]);
}
