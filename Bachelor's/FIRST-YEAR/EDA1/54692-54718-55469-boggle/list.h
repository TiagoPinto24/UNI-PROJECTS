#include <stdbool.h>
typedef char* ElementTypeList;

#ifndef _List_H
#define _List_H

struct Node;
typedef struct Node *PtrToNode;
typedef PtrToNode List;
typedef PtrToNode Position;

List CreateList(List L);
int IsEmptyList( List L );
int IsLast( Position P);

Position Find( ElementTypeList X, List L );
Position FindPrevious( ElementTypeList X, List L );

void Insert( ElementTypeList X, List L, Position P );
void Delete( Position P, List L );
void DeleteList( List L );

Position Header( List L );
Position First( List L );
Position Advance( Position P );
ElementTypeList Retrieve( Position P );
void PrintList(List L);

#endif
