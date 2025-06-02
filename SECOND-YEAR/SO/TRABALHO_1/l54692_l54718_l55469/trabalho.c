#include "defs.h"
#include "queue.h"
#include "queue.c"
#include "inputs.c"

void TransformInput (int in[][11], int progSZ, int prog[][11]) {
    for (int i = 0; i < progSZ; i++) {
        for (int j = 0; j < 11; j++) {
            prog[i][j] = in[i][j];
        }
    }
}

int InputSize (int in) {
    int n=0;
    switch (in)
    {
    case 0:
        n=sizeof(input00) / sizeof(input00[0]);
        break;
    case 1:
        n=sizeof(input01) / sizeof(input01[0]);
        break;
    case 2:
        n=sizeof(input02) / sizeof(input02[0]);
        break;
    case 3:
        n=sizeof(input03) / sizeof(input03[0]);
        break;
    case 4:
        n=sizeof(input04) / sizeof(input04[0]);
        break;
    }
    return n;
}

void NewProgram(int time, int progSZ, int prog[][11], Queue R)
{
    for (int i = 0; i < progSZ; i++)
    {
        if (prog[i][1] == time)
            Enqueue(i, R);
    }
}

int MenorTempo(int prog[][11], int progSZ)
{
    int aux = prog[0][1];
    int res = prog[0][0];
    for (int i = 0; i < progSZ; i++)
    {
        if (aux > prog[i][1])
        {
            aux = prog[i][1];
            res = i;
        }
    }
    return res;
}

int CountDownZombie(Queue Z, int term, Queue Ztimer)
{
    while (size(Ztimer)<size(Z))
        Enqueue(3,Ztimer);
    for (int i = Ztimer->Front; i != Ztimer->Rear; i=successor(i,Ztimer))
        Ztimer->Array[i]--;
    for (int i = Ztimer->Front; i != Ztimer->Rear; i=successor(i,Ztimer))
    {
        if (!IsEmptyQueue(Ztimer) && !IsEmptyQueue(Z) && Ztimer->Array[i]==0)
        {
            Dequeue(Ztimer);
            Dequeue(Z);
            term++;
        }
    }
    return term;
    
}

void FreeInterruptible(int prog[][11], Queue I, Queue Z, Queue R, int aux)
{
    if (aux == 10 || prog[I->Array[I->Front]][aux + 1] == 0)
        Enqueue(Dequeue(I), Z);
    else if (aux != 10 && prog[I->Array[I->Front]][aux + 1] != 0){
        Enqueue(Dequeue(I), R);
    }
}

void FreeExecutable(int prog[][11], int RR, Queue I, Queue Z, Queue R, int exe, int aux)
{
    if (RR == 3 && prog[exe][aux] != 0)
        Enqueue(exe, R);
    else if (prog[exe][aux] == 0 && aux != 10 && prog[exe][aux + 1] != 0)
        Enqueue(exe, I);
    else if (prog[exe][aux] == 0 && (aux == 10 || prog[exe][aux + 1] == 0))
        Enqueue(exe, Z);
}

FILE* FileOpener (int n) {
    FILE *output;
    switch (n)
    {
    case 0:
        output = fopen("output00.out","w");
        break;
    case 1:
        output = fopen("output01.out","w");
        break;
    case 2:
        output = fopen("output02.out","w");
        break;
    case 3:
        output = fopen("output03.out","w");
        break;
    case 4:
        output = fopen("output04.out","w");
        break;
    }
    return output;
}

void OutputInicial (FILE* op, int progSZ) {
    fprintf(op,"time inst | ");
    for (int i = 1; i <= progSZ; i++) {
        if (i==progSZ)
            fprintf(op,"th%d\n",i);
        else
            fprintf(op,"th%d           | ",i);
    }
}

void Output(FILE* op, int progSZ, int t, int prog[][11], Queue R, int exe, Queue Z, Queue I)
{
    if (t-1!=0) {
    fprintf(op,"%d\t        ", t-1);
    for (int i = 0; i < progSZ; i++)
    {
        if (!IsEmptyQueue(R)&&viewQueue(R, i))
            fprintf(op,"READY\t        ");
        else if (i == exe)
            fprintf(op,"EXECUTING\t    ");
        else if (!IsEmptyQueue(Z)&&viewQueue(Z, i))
            fprintf(op,"ZOMBIE\t        ");
        else if (!IsEmptyQueue(I)&&viewQueue(I, i))
            fprintf(op,"INTERRUPTIBLE   ");
        else
            fprintf(op,"\t            ");
    }
    fprintf(op,"\n");
    }
}

MAIN()
{

    int input;
    printf("Escolha um input de 0 a 4:\n");
    scanf("%d",&input);

    int programas[InputSize(input)][11];
    int programaSize= sizeof(programas) / sizeof(programas[0]);

    //escolha do input
    switch (input)
    {
    case 0:
        TransformInput(input00,programaSize,programas);
        break;
    case 1:
        TransformInput(input01,programaSize,programas);
        break;
    case 2:
        TransformInput(input02,programaSize,programas);
        break;
    case 3:
        TransformInput(input03,programaSize,programas);
        break;
    case 4:
        TransformInput(input04,programaSize,programas);
        break;
    }

    int roundRobin = 0,executing = -1;

    Queue ready = CreateQueue(10);

    Queue interruptible = CreateQueue(10);

    Queue zombie = CreateQueue(10);
    Queue zombieTimer = CreateQueue(zombie->Capacity);

    FILE *output = FileOpener(input);

    OutputInicial(output, programaSize);

    int time = 1, auxExecute, auxInterruptible, processChange = MenorTempo(programas, programaSize);
    for (int terminadas = 0; terminadas < programaSize; time++)
    {

        //atualizar o tempo do interruptible
        if (!IsEmptyQueue(interruptible))
        {
            auxInterruptible = 0;
            for (int j = 2; j < 11; j++)
            {
                if (programas[interruptible->Array[interruptible->Front]][j] != 0)
                {
                    programas[interruptible->Array[interruptible->Front]][j]--;
                    auxInterruptible = j;
                    j = 10;
                }
            }
        }

        //mudança do processo no executing, se possivel
        if (executing < 0 && !IsEmptyQueue(ready))
        {
            if (processChange != programas[ready->Array[ready->Front]][0])
            {
                executing = -4;
                processChange = programas[ready->Array[ready->Front]][0];
            }
            if (executing < -1)
                executing++;
            else if (executing == -1) {
                executing = Dequeue(ready);
                
            }
        }
        
        //atualizar os tempos do executing
        if (executing >= 0)
        {
            auxExecute = 0;
            for (int i = 2; i < 11; i++)
            {
                if (programas[executing][i] != 0)
                {
                    programas[executing][i]--;
                    auxExecute = i;
                    roundRobin++;
                    i = 10;
                }
            }
        }

        Output(output, programaSize, time, programas, ready, executing, zombie, interruptible);

        //retirar o processo do interruptible, se possivel
        if (!IsEmptyQueue(interruptible) && programas[interruptible->Array[interruptible->Front]][auxInterruptible] == 0)
            FreeInterruptible(programas, interruptible, zombie, ready, auxInterruptible);

        NewProgram(time, programaSize, programas, ready);

        //retirar o processo do executing, se possivel
        if (executing >= 0 && (roundRobin == 3 || programas[executing][auxExecute] == 0) )
        {
            processChange = programas[executing][0];
            FreeExecutable(programas,roundRobin,interruptible,zombie,ready,executing,auxExecute);
            roundRobin = 0;
            executing = -2;
        }

        terminadas = CountDownZombie(zombie, terminadas,zombieTimer);
    }
    return 0;
}