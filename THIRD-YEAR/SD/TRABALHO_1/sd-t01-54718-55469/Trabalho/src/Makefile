#Antes de usar este makefile é necessário realizar o comando "cd Trabalho/src" no terminal

#Após estar dentro da pasta "src" execute o comando "make" para compilar e correr o programa

# Variáveis
JAVAC = javac
JAVA = java
CLASSPATH = .:../lib/postgresql.jar
SRC = sd
BIN = $(SRC)/Server.java $(SRC)/PostgresConnector.java

# Nome do programa principal
MAIN_CLASS = sd.Server

# Regra padrão
all: compile run

# Compilar os arquivos Java
compile:
	$(JAVAC) -cp $(CLASSPATH) $(BIN)

# Executar o programa principal
run:
	$(JAVA) -cp $(CLASSPATH) $(MAIN_CLASS)

# Limpar arquivos compilados
clean:
	rm -f $(SRC)/*.class
