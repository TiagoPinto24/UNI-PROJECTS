//
//  hash-linear.c
//  HashLinearInt
//
//  Created by Ligia Ferreira on 20/05/2021.
//

#include "./hash-linear.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define Error( Str )        FatalError( Str )
#define FatalError( Str )   fprintf( stderr, "%s\n", Str ), exit( 1 )
#define MinTableSize (10)

enum KindOfEntry { Legitimate, Empty, Deleted };

struct HashEntry
{
    ElementType     Element;
    enum KindOfEntry Info;
};

typedef struct HashEntry Cell;

/* Cell *TheCells will be an array of */
/* HashEntry cells, allocated later */
struct HashTbl
{   int Ocupados;
    int TableSize;
    Cell *TheCells;
};

static int
NextPrime( int N )
{
    again:
    N++;
    for (int i = 2; i * i < N; i++) {
    if (N%i==0)
        goto again;
    }
    return N;
}

Index Hash (ElementType Key, int TableSize, LinHashTable H) {
    int num=0;
    for (int i = 0; Key[i] != '\0'; i++)
        num+=Key[i];
    for (int i = 0; ; i++) {
        if (H->TheCells[(num+i*i)%TableSize].Info==Empty)
            return (num+i*i)%TableSize;
    }  
}

LinHashTable InitializeTable( int TableSize ) {
    LinHashTable H;
    int i;

    if( TableSize < MinTableSize ){
        Error( "Table size too small" );
        return NULL;
    }

    H = malloc( sizeof( struct HashTbl ) );
    if( H == NULL )
        FatalError( "Out of space!!!" );
    
    H->TableSize = NextPrime(TableSize);
    H->Ocupados = 0;

    H->TheCells = malloc( sizeof( Cell ) * H->TableSize );
    if( H->TheCells == NULL )
        FatalError( "Out of space!!!" );

    for( i = 0; i < H->TableSize; i++ )
        H->TheCells[ i ].Info = Empty;

    return H;
}

TablePosition ProcPos( ElementType Key, LinHashTable H ){
    int num=0,aux=0;
    for (int i = 0; Key[i] != '\0'; i++)
        num+=Key[i];
    TablePosition p;
    for (int i = 0; aux==0; i++) {
        p=(num+i*i)%H->TableSize;
            if (H->TheCells[p].Info==Empty)
                return 0;
            if (H->TheCells[p].Info!=Empty && !strcmp(Key,H->TheCells[p].Element))
                aux=1;
    }  
    
    return p;
}

void TableInsert( ElementType Key, LinHashTable H ){
    TablePosition p = Hash(Key,H->TableSize,H);
    H->TheCells[p].Element=malloc(sizeof(Key));
    for (int i = 0; Key[i]!='\0' ; i++)
        H->TheCells[p].Element[i]=Key[i];
    H->TheCells[p].Info=Legitimate;
    H->Ocupados++;
}

float LoadFactor(LinHashTable H){
    return (float)H->Ocupados/H->TableSize;
}

LinHashTable Rehash( LinHashTable H ){
    int i, OldSize;
    Cell *OldCells;

    OldCells = H->TheCells;
    OldSize  = H->TableSize;

    /* Get a new, empty table */
    H = InitializeTable( 2 * OldSize );

    /* Scan through old table, reTableinserting into new */
    for( i = 0; i < OldSize; i++ )
        if( OldCells[ i ].Info == Legitimate )
        TableInsert( OldCells[ i ].Element, H );

     free( OldCells );

    return H;
}

ElementType TableRetrieve( TablePosition P, LinHashTable H ){
    return H->TheCells[ P ].Element;
}

void DestroyTable( LinHashTable H ){
    free( H->TheCells );
    free( H );
}

void PrintTable(LinHashTable H){
    for (int i = 0; i < 57765 ; i++) {
        if (H->TheCells[i].Info!=Empty)
            printf("%d | %s\n--------\n",i,H->TheCells[i].Element); 
    }
}