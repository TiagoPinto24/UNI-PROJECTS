typedef int ElementType;

#ifndef _Queue_h
#define _Queue_h

struct QueueRecord{
    int Capacity;
    int Front;
    int Rear;
    ElementType *Array;
};
typedef struct QueueRecord *Queue;

Queue CreateQueue( int MaxElements );
void DisposeQueue( Queue Q );

int IsEmptyQueue( Queue Q );
int IsFullQueue( Queue Q );
void MakeEmptyQueue( Queue Q );

ElementType Front( Queue Q );
void Enqueue( ElementType X, Queue Q );
ElementType Dequeue( Queue Q );

void PrintQueue (Queue Q);
int viewQueue(Queue Q, ElementType element);

#endif
