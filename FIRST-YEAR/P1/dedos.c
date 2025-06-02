#include "defs.h"
#include <string.h>
#include <time.h>

char modo1extenso[30];              // Modo do jogador 1 no placar
char modo2extenso[30];              // Modo do jogador 2 no placar
char modo1des[30];                  // Desistência do jogador 1
char modo2des[30];                  // Desistência do jogador 2
int dedos[2][2] = {{1, 1}, {1, 1}}; // Mãos dos jogadores

void ee(int atacante)
{ // Esquerda ataca esquerda
    if (atacante == 0)
    {
        if (dedos[0][0] != 1)
        {
            if (dedos[1][0] != 1)
                printf("%d dedos da esquerda atacam %d dedos da esquerda, ", dedos[0][0], dedos[1][0]);
            else
                printf("%d dedos da esquerda atacam dedo da esquerda, ", dedos[0][0]);
        }
        else
        {
            if (dedos[1][0] != 1)
                printf("dedo da esquerda ataca %d dedos da esquerda, ", dedos[1][0]);
            else
                printf("dedo da esquerda ataca dedo da esquerda, ");
        }
        dedos[1][0] = (dedos[1][0] + dedos[0][0]) % 5;
        printf(dedos[1][0] != 1 ? "ficam %d dedos\n\n" : "fica %d dedo\n\n", dedos[1][0]);
    }
    else if (atacante == 1)
    {
        if (dedos[1][0] != 1)
        {
            if (dedos[0][0] != 1)
                printf("%d dedos da esquerda atacam %d dedos da esquerda, ", dedos[1][0], dedos[0][0]);
            else
                printf("%d dedos da esquerda atacam dedo da esquerda, ", dedos[1][0]);
        }
        else
        {
            if (dedos[0][0] != 1)
                printf("dedo da esquerda ataca %d dedos da esquerda, ", dedos[0][0]);
            else
                printf("dedo da esquerda ataca dedo da esquerda, ");
        }
        dedos[0][0] = (dedos[0][0] + dedos[1][0]) % 5;
        printf(dedos[0][0] != 1 ? "ficam %d dedos\n\n" : "fica %d dedo\n\n", dedos[0][0]);
    }
}

void ed(int atacante)
{ // Esquerda ataca direita
    if (atacante == 0)
    {
        if (dedos[0][0] != 1)
        {
            if (dedos[1][1] != 1)
                printf("%d dedos da esquerda atacam %d dedos da direita, ", dedos[0][0], dedos[1][1]);
            else
                printf("%d dedos da esquerda atacam dedo da direita, ", dedos[0][0]);
        }
        else
        {
            if (dedos[1][1] != 1)
                printf("dedo da esquerda ataca %d dedos da direita, ", dedos[1][1]);
            else
                printf("dedo da esquerda ataca dedo da direita, ");
        }
        dedos[1][1] = (dedos[0][0] + dedos[1][1]) % 5;
        printf(dedos[1][1] != 1 ? "ficam %d dedos\n\n" : "fica %d dedo\n\n", dedos[1][1]);
    }
    else if (atacante == 1)
    {
        if (dedos[1][0] != 1)
        {
            if (dedos[0][1] != 1)
                printf("%d dedos da esquerda atacam %d dedos da direita, ", dedos[1][0], dedos[0][1]);
            else
                printf("%d dedos da esquerda atacam dedo da direita, ", dedos[1][0]);
        }
        else
        {
            if (dedos[0][1] != 1)
                printf("dedo da esquerda ataca %d dedos da direita, ", dedos[0][1]);
            else
                printf("dedo da esquerda ataca dedo da direita, ");
        }
        dedos[0][1] = (dedos[1][0] + dedos[0][1]) % 5;
        printf(dedos[0][1] != 1 ? "ficam %d dedos\n\n" : "fica %d dedo\n\n", dedos[0][1]);
    }
}

void dd(int atacante)
{ // Direita ataca direita
    if (atacante == 0)
    {
        if (dedos[0][1] != 1)
        {
            if (dedos[1][1] != 1)
                printf("%d dedos da direita atacam %d dedos da direita, ", dedos[0][1], dedos[1][1]);
            else
                printf("%d dedos da direita atacam dedo da direita, ", dedos[0][1]);
        }
        else
        {
            if (dedos[1][1] != 1)
                printf("dedo da direita ataca %d dedos da direita, ", dedos[1][1]);
            else
                printf("dedo da direita ataca dedo da direita, ");
        }
        dedos[1][1] = (dedos[0][1] + dedos[1][1]) % 5;
        printf(dedos[1][1] != 1 ? "ficam %d dedos\n\n" : "fica %d dedo\n\n", dedos[1][1]);
    }
    else if (atacante == 1)
    {
        if (dedos[1][1] != 1)
        {
            if (dedos[0][1] != 1)
                printf("%d dedos da direita atacam %d dedos da direita, ", dedos[1][1], dedos[0][1]);
            else
                printf("%d dedos da direita atacam dedo da direita, ", dedos[1][1]);
        }
        else
        {
            if (dedos[0][1] != 1)
                printf("dedo da direita ataca %d dedos da direita, ", dedos[0][1]);
            else
                printf("dedo da direita ataca dedo da direita, ");
        }
        dedos[0][1] = (dedos[1][1] + dedos[0][1]) % 5;
        printf(dedos[0][1] != 1 ? "ficam %d dedos\n\n" : "fica %d dedo\n\n", dedos[0][1]);
    }
}

void de(int atacante)
{ // Direita ataca esquerda
    if (atacante == 0)
    {
        if (dedos[0][1] != 1)
        {
            if (dedos[1][0] != 1)
                printf("%d dedos da direita atacam %d dedos da esquerda, ", dedos[0][1], dedos[1][0]);
            else
                printf("%d dedos da direita atacam dedo da esquerda, ", dedos[0][1]);
        }
        else
        {
            if (dedos[1][0] != 1)
                printf("dedo da direita ataca %d dedos da esquerda, ", dedos[1][0]);
            else
                printf("dedo da direita ataca dedo da esquerda, ");
        }
        dedos[1][0] = (dedos[0][1] + dedos[1][0]) % 5;
        printf(dedos[1][0] != 1 ? "ficam %d dedos\n\n" : "fica %d dedo\n\n", dedos[1][0]);
    }
    else if (atacante == 1)
    {
        if (dedos[1][1] != 1)
        {
            if (dedos[0][0] != 1)
                printf("%d dedos da direita atacam %d dedos da esquerda, ", dedos[1][1], dedos[0][0]);
            else
                printf("%d dedos da direita atacam dedo da esquerda, ", dedos[1][1]);
        }
        else
        {
            if (dedos[0][0] != 1)
                printf("dedo da direita ataca %d dedos da esquerda, ", dedos[0][0]);
            else
                printf("dedo da direita ataca dedo da esquerda, ");
        }
        dedos[0][0] = (dedos[1][1] + dedos[0][0]) % 5;
        printf(dedos[0][0] != 1 ? "ficam %d dedos\n\n" : "fica %d dedo\n\n", dedos[0][0]);
    }
}

void igual(int atacante)
{ // Divide dedos igualmente
    int soma;
    if (atacante == 0)
    {
        soma = dedos[0][0] + dedos[0][1];
        dedos[0][0] = soma / 2;
        dedos[0][1] = soma / 2;
        printf("divide os %d dedos pelas duas mãos\n\n", soma);
    }
    else if (atacante == 1)
    {
        soma = dedos[1][0] + dedos[1][1];
        dedos[1][0] = soma / 2;
        dedos[1][1] = soma / 2;
        printf("divide os %d dedos pelas duas mãos\n\n", soma);
    }
}

void aocalhas(int atacante)
{ // Estratégia aleatória
    printf("vez do ao-calhas %d: ", atacante + 1);
    while (1)
    {
        int random = (rand() % 5) + 1;
        if ((random == 1) && (dedos[atacante][0] != 0) && (dedos[!atacante][0] != 0))
        {
            printf("ee\n");
            ee(atacante);
            break;
        }
        else if ((random == 2) && (dedos[atacante][0] != 0) && (dedos[!atacante][1] != 0))
        {
            printf("ed\n");
            ed(atacante);
            break;
        }
        else if ((random == 3) && (dedos[atacante][1] != 0) && (dedos[!atacante][1] != 0))
        {
            printf("dd\n");
            dd(atacante);
            break;
        }
        else if ((random == 4) && (dedos[atacante][1] != 0) && (dedos[!atacante][0] != 0))
        {
            printf("de\n");
            de(atacante);
            break;
        }
        else if ((random == 5) && ((dedos[atacante][0] == 0 && (dedos[atacante][1] % 2 == 0))) ||
                 (dedos[atacante][1] == 0 && (dedos[atacante][0] % 2 == 0)))
        {
            printf("=\n");
            igual(atacante);
            break;
        }
    }
}

void humano(int atacante)
{ // Jogador humano
    char jogada[10];
    while (1)
    {
        printf("vez do humano %d: ", atacante + 1);
        scanf("%s", jogada);
        if (!strcmp(jogada, "ee") && (dedos[atacante][0] != 0) && (dedos[!atacante][0] != 0))
        {
            ee(atacante);
            break;
        }
        else if (!strcmp(jogada, "ed") && (dedos[atacante][0] != 0) && (dedos[!atacante][1] != 0))
        {
            ed(atacante);
            break;
        }
        else if (!strcmp(jogada, "de") && (dedos[atacante][1] != 0) && (dedos[!atacante][0] != 0))
        {
            de(atacante);
            break;
        }
        else if (!strcmp(jogada, "dd") && (dedos[atacante][1] != 0) && (dedos[!atacante][1] != 0))
        {
            dd(atacante);
            break;
        }
        else if ((!strcmp(jogada, "=")) && ((dedos[atacante][0] == 0 && (dedos[atacante][1] % 2 == 0)) ||
                                            (dedos[atacante][1] == 0 && (dedos[atacante][0] % 2 == 0))))
        {
            igual(atacante);
            break;
        }
        else if (!strcmp(jogada, "."))
        {
            printf("desistência: vitória do %s!!!\n", atacante ? modo1des : modo2des);
            exit(1);
        }
        else
        {
            printf("jogada inválida!\n");
        }
    }
}

void chicoesperto(int atacante)
{ // Estratégia inteligente
    printf("vez do chico-esperto %d: ", atacante + 1);
    if (dedos[atacante][0] >= dedos[atacante][1])
    {
        if ((dedos[!atacante][0] <= dedos[!atacante][1]) && dedos[!atacante][0] != 0)
        {
            printf("ee\n");
            ee(atacante);
        }
        else
        {
            printf("ed\n");
            ed(atacante);
        }
    }
    else
    {
        if ((dedos[!atacante][0] <= dedos[!atacante][1]) && dedos[!atacante][0] != 0)
        {
            printf("de\n");
            de(atacante);
        }
        else
        {
            printf("dd\n");
            dd(atacante);
        }
    }
}

MAIN()
{
    int modo1 = 0, modo2 = 0;
    srand(time(0));

    // Configura jogador 1
    if (!strcmp("humano", ARG1))
    {
        modo1 = 1;
        strcpy(modo1extenso, "          humano");
        strcpy(modo1des, "humano 1");
    }
    else if (!strcmp("chico-esperto", ARG1))
    {
        modo1 = 2;
        strcpy(modo1extenso, "   chico-esperto");
        strcpy(modo1des, "chico-esperto 1");
    }
    else if (!strcmp("ao-calhas", ARG1))
    {
        modo1 = 3;
        strcpy(modo1extenso, "       ao-calhas");
        strcpy(modo1des, "ao-calhas 1");
    }
    else
    {
        printf("argumento invalido\n");
        exit(0);
    }

    // Configura jogador 2
    if (!strcmp("humano", ARG2))
    {
        modo2 = 1;
        strcpy(modo2extenso, "          humano");
        strcpy(modo2des, "humano 2");
    }
    else if (!strcmp("chico-esperto", ARG2))
    {
        modo2 = 2;
        strcpy(modo2extenso, "   chico-esperto");
        strcpy(modo2des, "chico-esperto 2");
    }
    else if (!strcmp("ao-calhas", ARG2))
    {
        modo2 = 3;
        strcpy(modo2extenso, "       ao-calhas");
        strcpy(modo2des, "ao-calhas 2");
    }
    else
    {
        printf("argumento invalido\n");
        exit(0);
    }

    printf("Jogo dos dedos - %s vs %s\n\n", ARG1, ARG2);
    printf("%s 1:   %d, %d\n%s 2:   %d, %d\n\n", modo1extenso, dedos[0][0], dedos[0][1], modo2extenso, dedos[1][0], dedos[1][1]);

    while (1)
    {
        // Turno do jogador 1
        if (modo1 == 1)
            humano(0);
        else if (modo1 == 2)
            chicoesperto(0);
        else if (modo1 == 3)
            aocalhas(0);

        printf("%s 1:   %d, %d\n%s 2:   %d, %d\n\n", modo1extenso, dedos[0][0], dedos[0][1], modo2extenso, dedos[1][0], dedos[1][1]);

        if ((dedos[1][0] == 0) && (dedos[1][1] == 0))
        {
            printf("vitória do %s 1!!!\n", modo1 == 1 ? "humano" : modo1 == 2 ? "chico-esperto"
                                                                              : "ao-calhas");
            exit(1);
        }

        // Turno do jogador 2
        if (modo2 == 1)
            humano(1);
        else if (modo2 == 2)
            chicoesperto(1);
        else if (modo2 == 3)
            aocalhas(1);

        printf("%s 1:   %d, %d\n%s 2:   %d, %d\n\n", modo1extenso, dedos[0][0], dedos[0][1], modo2extenso, dedos[1][0], dedos[1][1]);

        if ((dedos[0][0] == 0) && (dedos[0][1] == 0))
        {
            printf("vitória do %s 2!!!\n", modo2 == 1 ? "humano" : modo2 == 2 ? "chico-esperto"
                                                                              : "ao-calhas");
            exit(1);
        }
    }
}