#include "list.h"
#include <stdlib.h>
#include "fatal.h"

struct Node{
    ElementTypeList Element;
    Position    Next;
};


List CreateList( List L )
{
    if( L != NULL )
        DeleteList( L );
    else{
        L = malloc( sizeof( struct Node ) );
        L->Element = malloc(sizeof(ElementTypeList));
        if( L == NULL )
            FatalError( "Out of memory!" );
        L->Next = NULL;
    }
    return L;
}

int IsEmptyList( List L ){
    if (L->Next==NULL)
        return 1;
    return 0;
}

int IsLast( Position P){
    if (P->Next==NULL)
        return 1;
    return 0;
}

Position Find( ElementTypeList X, List L ){
    Position P=First(L);
    while (!IsLast(P)) {
        if (P->Next->Element==X)
            return P->Next;
        P=Advance(P);
    }
    return NULL;
}

Position FindPrevious( ElementTypeList X, List L ) {
    Position P=L->Next;
    while (!IsLast(P)) {
        if (P->Next->Element==X)
            return P;
        P=Advance(P);
    }
    return NULL;
}

void Insert( ElementTypeList X, List L, Position P ) {
    Position new = malloc(sizeof(Position));
    new->Element=malloc(sizeof(X));
    for (int i = 0; X[i]!='\0'; i++)
        new->Element[i]=X[i];
    if (IsEmptyList(L))
        L->Next=new;
    else {
        new->Next=P->Next;
        P->Next=new;
    }   
}

void Delete( Position P, List L ){
    if (IsEmptyList(L))
        FatalError("Está vazia!");
    if (P==First(L))
        Header(L)->Next=First(L)->Next;
    else
        FindPrevious(P->Element,L)->Next=Find(P->Element,L)->Next;
}

void DeleteList( List L ) {
    L=NULL;
}

Position Header( List L ) {
    return L;
}

Position First( List L ) {
    return L->Next;
}


Position Advance( Position P ) {
    if (IsLast(P))
        FatalError("Advance impossível no último elemento!");    
    return P->Next;
}


ElementTypeList Retrieve( Position P ) {
    return P->Element;
}

void PrintList(List L){
    Position P=L->Next;
    printf("{");
    while (!IsLast(P)) {
        for (int i = 0; P->Element[i]!='\0'; i++)
            printf("%c",P->Element[i]);    
        printf(";");
        P=Advance(P);
        }
        for (int i = 0; P->Element[i]!='\0'; i++)
            printf("%c",P->Element[i]);
    printf("}\n");
}