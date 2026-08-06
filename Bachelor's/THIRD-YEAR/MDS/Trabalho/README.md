# TrabalhoFinalMDS
Repositório para o trabalho de Metodologias e Desenvolvimento de Software
## Relatório Final
#### Realizado por: 
* Tiago Pinto 54718
* João Loios 55469
* Duarte Silva 55877
### Introdução
Este trabalho, desenvolvido no âmbito da Licenciatura de Engenharia informática na Universidade de Évora na unidade curricular de Metodologias e Desenvolvimento de Software, tem como objetivo fazer a implementação parcial do sistema especificado na primeira parte. Este sistema implementa e testa a maioria dos use cases definidos para o funcionário e gestor,  sendo eles:
1. Criar e remover quartos;
2. Editar e especificar as propriedades de um quarto;
3. Verificar quais quartos precisam de manutenções e qual o tipo de manutenções;
4. Registar todas as manutenções de um quarto e como não era exigido implementar a opção de um cliente reservar o quarto, entregámo-la aos funcionários e gestor.
### Implementação
Implementámos assim as classes quarto, manutenção e reservas além da classe main.
#### Quarto.java
Nesta classe implementamos os quartos definidos com um número, capacidade máxima, nº de camas, se tem ou não cozinha e/ou varanda, que tipo de vista tem e também um arrayList para as reservas programadas para esse quarto e para as suas manutenções. Decidimos implementar funções para, além de operações normais, como set e get de qualquer um dos parâmetros definidos, ver se um quarto está alugado para certa data, ou averiguar manutenções relativas a esse quarto. Optamos por deixar nesse registo todas as manutenções que esse quarto já teve, incluindo as realizadas.
#### Manutencao.java e reservas.java
Aqui definimos as manutenções atribuindo-lhes um tipo, um valor bool que indica se foi ou não realizada, e datas para quando foi registada e quando foi realizada, já na classe reservas apenas definimos as reservas com uma data de inicio e uma de término. Ambas estas classes servem de auxilio à classe quarto.
#### Main.java
Desenvolvemos uma main com um menu disponibilizando ao utilizador a oportunidade de usar os métodos implementados pelas classes.
#### Testes Junit
Implementámos 5 ficheiros de teste um para cada issue fora o issue de criar e remover quartos sendo este dividido em 2 ficheiros de teste.
### Conclusão
Ao realizar este trabalho, conseguimos implementar todas as funcionalidades propostas no enunciado, como também, achamos que não ficou nada por fazer. Por fim, gostámos de realizar este trabalho, e pretendemos melhorar os nossos conhecimentos e adquirir mais, futuramente.
