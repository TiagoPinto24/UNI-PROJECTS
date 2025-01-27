//
//  hash-linear.h
//  HashLinearInt
//
//  Created by Ligia Ferreira on 20/05/2021.
//

typedef char* ElementType;


#ifndef _HashLin_H
#define _HashLin_H

typedef unsigned int Index;
typedef Index TablePosition;

struct HashTbl;
typedef struct HashTbl *LinHashTable;

LinHashTable InitializeTable( int TableSize );
void DestroyTable( LinHashTable H );
TablePosition ProcPos( ElementType Key, LinHashTable H );
void TableInsert( ElementType Key, LinHashTable H );
ElementType TableRetrieve( TablePosition P, LinHashTable H );
LinHashTable Rehash( LinHashTable H );
void PrintTable(LinHashTable H);
float LoadFactor(LinHashTable H);

/* Routines such as Delete are MakeEmpty are omitted */

#endif  /* _HashLin_H */

/* END */
