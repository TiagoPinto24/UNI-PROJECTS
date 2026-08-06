#include <stdlib.h>
#include "queue.h"
#include "fatal.h"

#define MinQueueSize (5)

int successor(int i, Queue Q)
{
    return (i + 1) % Q->Capacity;
}

int size(Queue Q)
{
    int count = 0;
    for (int i = Q->Front; i != Q->Rear; i = successor(i, Q))
        count++;
    return count;
}

Queue CreateQueue(int MaxElements)
{
    Queue Q;

    if (MaxElements < MinQueueSize)
        Error("Queue size is too small");

    Q = malloc(sizeof(struct QueueRecord));
    if (Q == NULL)
        FatalError("Out of space!!!");

    Q->Array = malloc(sizeof(ElementType) * MaxElements);
    if (Q->Array == NULL)
        FatalError("Out of space!!!");

    Q->Capacity = MaxElements + 1;
    MakeEmptyQueue(Q);

    return Q;
}

void DisposeQueue(Queue Q)
{
    if (Q != NULL)
    {
        free(Q->Array);
        free(Q);
    }
}

int IsEmptyQueue(Queue Q)
{
    return Q->Front == Q->Rear;
}

int IsFullQueue(Queue Q)
{
    return size(Q) + 1 == Q->Capacity;
}

void MakeEmptyQueue(Queue Q)
{
    Q->Rear = 0;
    Q->Front = 0;
}

void Enqueue(ElementType X, Queue Q)
{
    if (IsFullQueue(Q))
        FatalError("Está cheia");
    Q->Array[Q->Rear] = X;
    Q->Rear = successor(Q->Rear, Q);
}

ElementType Front(Queue Q)
{
    return Q->Array[Q->Front];
}

ElementType Dequeue(Queue Q)
{
    if (IsEmptyQueue(Q))
        FatalError("Está vazia!");
    ElementType x = Q->Array[Q->Front];
    Q->Front = successor(Q->Front, Q);
    return x;
}

void PrintQueue(Queue Q)
{
    printf("[ ");
    for (int i = Q->Front; i != Q->Rear; i = successor(i,Q))
    {
        printf("%d ",Q->Array[i]);
    }
    printf("]\n");
}
int viewQueue(Queue Q, ElementType element)
{
    for (int i = Q->Front; i != Q->Rear; i = successor(i, Q))
    {
        if (element == Q->Array[i])
        {
            return 1;
        }
    }
    return 0;
}

/*int main()
{
    Queue queue = CreateQueue(11);
    int programas[3][11] = {
        {1, 2, 2, 2, 4, 1, 1, 0, 0, 0, 0},
        {1, 1, 4, 2, 2, 2, 4, 0, 0, 0, 0},
        {2, 4, 2, 1, 6, 1, 3, 1, 1, 0, 0}};
        Enqueue(programas[0][1], queue);
        Enqueue(programas[1][1], queue); 
        Enqueue(programas[2][1], queue);
        PrintQueue(queue);
        printf("%d\n", viewQueue(queue, programas[0][10]));
}*/

