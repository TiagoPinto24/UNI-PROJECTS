#include "hash-linear.h"
#include "hash-linear.c"
#include "stackar.c"
#include "stackar.h"
#include "list.h"
#include "list.c"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

// Função para criar as direções possiveis de cada letra
void CreateDirections(Stack Stack, int linha, int coluna)
{
    char N[3] = "N ", S[3] = "S ", E[3] = " E", W[3] = " W", NE[3] = "NE", NW[3] = "NW", SE[3] = "SE", SW[3] = "SW";
    if (linha == 0)
    {
        Push(S, Stack);
        if (coluna == 0)
        {
            Push(SE, Stack);
            Push(E, Stack);
        }
        else if (coluna == 3)
        {
            Push(SW, Stack);
            Push(W, Stack);
        }
        else
        {
            Push(SW, Stack);
            Push(W, Stack);
            Push(SE, Stack);
            Push(E, Stack);
        }
    }
    else if (linha == 3)
    {
        Push(N, Stack);
        if (coluna == 0)
        {
            Push(NE, Stack);
            Push(E, Stack);
        }
        else if (coluna == 3)
        {
            Push(NW, Stack);
            Push(W, Stack);
        }
        else
        {
            Push(NW, Stack);
            Push(W, Stack);
            Push(NE, Stack);
            Push(E, Stack);
        }
    }
    else
    {
        Push(N, Stack);
        Push(S, Stack);
        if (coluna == 0)
        {
            Push(NE, Stack);
            Push(E, Stack);
            Push(SE, Stack);
        }
        else if (coluna == 3)
        {
            Push(NW, Stack);
            Push(W, Stack);
            Push(SW, Stack);
        }
        else
        {
            Push(NW, Stack);
            Push(W, Stack);
            Push(NE, Stack);
            Push(E, Stack);
            Push(SE, Stack);
            Push(SW, Stack);
        }
    }
}

int main()
{

    FILE *F = fopen("corncob_caps_2023.txt", "r");
    char palavra[20], prefixo[20], letra;
    int n;
    LinHashTable Hpalavras = InitializeTable(116218), Hprefixos = InitializeTable(348654);

    // criação das tabelas hash das palavras (Hpalavras) e dos prefixos (Hprefixos)
    for (int cont = 0; cont < 58109; cont++)
    {
        letra = fgetc(F);
        for (n = 0; letra != '\n' && letra != EOF; n++)
        {
            palavra[n] = letra;
            letra = fgetc(F);
        }
        if (letra == EOF)
            palavra[n] = '\0';
        else
            palavra[n - 1] = '\0';
        for (int i = 0; palavra[i] != '\0'; i++)
        {
            prefixo[i] = palavra[i];
            prefixo[i + 1] = '\0';
            if (strlen(prefixo) > 1 && palavra[i + 1] != '\0' && ProcPos(prefixo, Hprefixos) == 0)
                TableInsert(prefixo, Hprefixos);
        }
        TableInsert(palavra, Hpalavras);
    }

    char jogo[4][4];
    int a;

    // ler o ficheiro do BOGGLE escolhido
    printf("Escolha um boggle de 0 a 2\n");
    scanf("%d", &a);

    if (a == 0)
    {
        FILE *BOGGLE = fopen("boggle0.txt", "r");
        if (BOGGLE != NULL)
        {
            for (int i = 0; i < 4; i++)
            {
                for (int j = 0; j < 4; j++)
                {
                    jogo[i][j] = fgetc(BOGGLE);
                    fgetc(BOGGLE);
                }
            }
        }
    }

    else if (a == 1)
    {
        FILE *BOGGLE = fopen("boggle1.txt", "r");
        if (BOGGLE != NULL)
        {
            for (int i = 0; i < 4; i++)
            {
                for (int j = 0; j < 4; j++)
                {
                    jogo[i][j] = fgetc(BOGGLE);
                    fgetc(BOGGLE);
                }
            }
        }
    }
    else if (a == 2)
    {
        FILE *BOGGLE = fopen("boggle2.txt", "r");
        if (BOGGLE != NULL)
        {
            for (int i = 0; i < 4; i++)
            {
                for (int j = 0; j < 4; j++)
                {
                    jogo[i][j] = fgetc(BOGGLE);
                    fgetc(BOGGLE);
                }
            }
        }
    }

    a = 0;
    Stack Sdirecoes = CreateStack(100);
    List Lrespostas = CreateList(NULL);
    char check[20] = "\0", resposta[40] = "\0";
    int l, laux = 0, c, caux = 0, p = 1, q = 3;

    // resolução do BOGGLE
    for (int linha = 0; linha < 4; linha++)
    {
        for (int coluna = 0; coluna < 4; coluna++)
        {
            palavra[0] = jogo[linha][coluna];
            resposta[0] = jogo[linha][coluna];
            resposta[1] = linha + '0';  // para transformar um int num char somamos o craracter '0'
            resposta[2] = coluna + '0'; // para transformar um int num char somamos o craracter '0'
            l = linha;
            c = coluna;

            do
            {
                Push("--", Sdirecoes); // marcador na Stack para saber quando certa letra já não tem caminhos possíveis
                CreateDirections(Sdirecoes, l, c);
                laux = l;
                caux = c;

                do
                {
                usadas:
                    strcpy(check, Pop(Sdirecoes));

                    while (!strcmp(check, "--"))
                    {
                        if (IsEmpty(Sdirecoes))
                            goto vazia;
                        p--;
                        q = q - 3;
                        palavra[p] = '\0';
                        resposta[q] = '\0';
                        strcpy(check, Pop(Sdirecoes));
                        laux = resposta[q - 2] - 48;
                        caux = resposta[q - 1] - 48;
                    }

                    l = laux;
                    c = caux;

                    // tradução do que está na Stack para valores numéricos nas linhas e colunas da matriz
                    if (check[0] == 'S')
                    {
                        if (check[1] == 'E')
                        {
                            l++;
                            c++;
                        }
                        else if (check[1] == 'W')
                        {
                            l++;
                            c--;
                        }
                        else
                            l++;
                    }
                    else if (check[0] == 'N')
                    {
                        if (check[1] == 'E')
                        {
                            l--;
                            c++;
                        }
                        else if (check[1] == 'W')
                        {
                            l--;
                            c--;
                        }
                        else
                            l--;
                    }
                    else
                    {
                        if (check[1] == 'E')
                            c++;
                        else if (check[1] == 'W')
                            c--;
                    }

                    // para a palavra não usar letras já usadas
                    for (int i = 0; resposta[i] != '\0'; i = i + 3)
                    {
                        if (l + 48 == resposta[i + 1] && c + 48 == resposta[i + 2])
                            goto usadas;
                    }

                    palavra[p] = jogo[l][c];
                    resposta[q] = jogo[l][c];
                    resposta[1 + q] = l + '0';
                    resposta[2 + q] = c + '0';
                    palavra[p + 1] = '\0';
                    resposta[3 + q] = '\0';

                    if (ProcPos(palavra, Hpalavras) != 0)
                    {
                        printf("%s:", palavra);
                        a++;
                        for (int i = 0; resposta[i] != '\0'; i = i + 3)
                        {
                            if (resposta[i + 3] == '\0')
                                printf("%c[%c;%c]", resposta[i], resposta[i + 1], resposta[i + 2]);
                            else
                                printf("%c[%c;%c]->", resposta[i], resposta[i + 1], resposta[i + 2]);
                        }
                        printf("\n");

                        Insert(resposta, Lrespostas, Header(Lrespostas));
                    }

                } while (ProcPos(palavra, Hprefixos) == 0);
                p++;
                q = q + 3;

            } while (!IsEmpty(Sdirecoes));
        vazia:
        }
    }

    printf("Foram encontrdas %d palavras!\n", a);

    return 0;
}