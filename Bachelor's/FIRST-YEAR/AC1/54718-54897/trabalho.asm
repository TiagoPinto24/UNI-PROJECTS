# Trabalho realizado por:
# Tiago Pinto 54718
# Henrique Anacleto 54897

.globl main
.data
imagem: .asciz "/home/tiago/Arquitetura/starwars.rgb"                                	#imagem do input em formato rgb
imagem2: .asciz "/home/tiago/Arquitetura/starwars2.rgb"                              	#imagem do output em formato rgb
frase: .string "Escolha um Personagem!\n1) Yoda\n2) Darth Maul\n3) Mandalorian\n"    	#string com a frase inicial que será visivel no output
pixeis_array:	.space 172800								#array com os valores rgb (3 bytes por pixel)	
.text



#########################################################################
# Função: read_rgb_image
# Descrição: Lê o ficheiro starwars.rgb e coloca cada pixel no pixeis_array
# Argumentos:
#	a0 - endereço do ficheiro starwars.rgb
#	a1 - endereço do array com os valores rgb
#	a2 - Tamanho do array
#	a7 - Comandos syscall (abrir, ler  e fechar o ficheiro)
# Retorno: 
#	 a0 - endereço do ficheiro starwars.rgb
#########################################################################
read_rgb_image: 

	li a7, 1024		# Abre o ficheiro
	li a1, 0		# Modo leitura
	ecall
	mv s6, a0		# Guarda a descrição do ficheiro noutro registo
	li a7, 63		# Lê o ficheiro e mete os valores no pixeis_array
	la a1, pixeis_array	# Endereço do pixeis_array onde serão lidos os pixeis
	li a2, 172800		# Tamanho do pixeis_array
	ecall			
	li a7, 57		# Fecha o ficheiro
	mv a0, s6		# Coloca o endereço a0 para retornar
	ecall
	ret
	
#####################################################################################################################################################
# Função: write_rgb_image
# Descrição: cria um ficheiro novo com uma imagem no formato RGB com uma mira em forma de cruz sobreposta no centro de massa do personagem escolhido
# Argumentos:
#	a0 - endereço do ficheiro starwars2.rgb
#	a1 - endereço do array com os valores rgb
#	a2 - Tamanho do array
#	a7 - Comandos syscall (abrir, escrever  e fechar o ficheiro)
#	s10 - Coordenada X do centro de massa
#	s11 - Coordenada Y do centro de massa
# Retorno: 
#	a0 - endereço do ficheiro starwars2.rgb
#####################################################################################################################################################	
write_rgb_image:

	li a7, 1024			# Abre o ficheiro	
	li a1, 1			# Modo Escrita
	ecall
	la a1, pixeis_array		# Endereço do pixeis_array onde serão lidos os pixeis
	mv s6, a0			# Guarda a descrição do ficheiro noutro registo
	add s8, zero, zero    		# X
	add s9, zero, zero		# Y
	add s5, zero, zero		# contador
	add t6, a1, a2			# Fim do array
	
	CRUZ:   
	rem s8, s5, s4			# X = contador % 320
        div s9, s5, s4			# Y = contador / 320
        beq s9, s11, CRUZ_2		# X == Cy
        beq s8, s10, CRUZ_3		# Y == Cx
        beqz zero, SEM_DESENHO
        
        CRUZ_2:
        mv t2, s10			# Guarda o valor do Cx em 2 registos diferentes
        mv t3, s10
        addi t2, t2, 10			# t2 = t2 + 10
        addi t3, t3, -10		# t3 = t3 - 10	
        
        blt s8, t3, SEM_DESENHO		# X >= Cx - 10 && X <= Cx + 10
        bgt s8, t2, SEM_DESENHO		
        beqz zero, DESENHO
        
        CRUZ_3:
        mv t2, s11			# Guarda o valor do Cy em 2 registos diferentes
        mv t3, s11
        addi t2, t2, 10			# t2 = t2 + 10
        addi t3, t3, -10		# t3 = t3 - 10	
        
        blt s9, t3, SEM_DESENHO		# Y >= Cy - 10 && Y <= Cy + 10
        bgt s9, t2, SEM_DESENHO
        beqz zero, DESENHO
        
	DESENHO:    
	addi a3, zero, 0         	# Cor Verde
        addi a4, zero, 255
        addi a5, zero, 0 
        sb a3, 0(a1)
        sb a4, 1(a1)
        sb a5, 2(a1)
        
	SEM_DESENHO:
	addi a1, a1, 3			# 3 próximos bytes
        addi s5, s5, 1			# Contador ++
        bne a1, t6, CRUZ
        
	FIM:    
	li a7, 64			#Lê o ficheiro descrito e coloca os valores para o pixeis_array
	la a1, pixeis_array		#Endereço do pixeis_array onde serão lidos os pixeis da imagem
	ecall			
	li a7, 57			#Fechar o ficheiro
	mv a0, s6			#Mover o endereço para a0 para retorno
	ecall
	ret
	
	
####################################################################################
# Função: location
# Descrição: calcula o “centro de massa” para um certo personagem
# Argumentos: 
#	a1 - endereço do array com os valores rgb
#	a2 - Tamanho do array
#	s0 - Indica qual o personagem que o pixel pertence
#	s7 - Indica qual personagem escolhido
# Retorno: Nada
####################################################################################			
location:
	addi s4, s4, 320		#numero de colunas da imagem	
	
 	LOOP:

        addi sp,sp,-4			# aloca espaco na stack
        sw ra,0(sp)			# guarda ra na stack	

        lbu a3,0(a1)    # R
        lbu a4,1(a1)    # G
        lbu a5,2(a1)    # B

        jal hue	
        
        addi a1,a1,3            	# 3 próximos bytes
        addi a2,a2,-3           	# array da imagem dimninui 3 bytes

        bne s7,s0,NAO_COINCIDE    	# Se o personagem a que o pixel pertence for diferente do personagem escolhido
        addi s1,s1,1            	# Contador de pixeis do personagem
           
        rem s8, s5, s4			#X = contador % 320
        div s9, s5, s4			#Y = contador / 320
        add s10, s10, s8		#Cx = Cx + X
        add s11, s11, s9		#Cy = Cy + Y

        NAO_COINCIDE:
        lw ra,0(sp)			# recupera ra na stack
        addi sp,sp,4			# liberta espaco na stack
        
        addi s5, s5, 1			#contador ++

        bne a2,zero,LOOP        	#Enquanto não ler os pixeis todos, não faz as contas finais do Cx e Cy
        div s10, s10, s1		#Cx = Cx / Contador de pixeis do personagem
        div s11, s11, s1		#Cy = Cy / Contador de pixeis do personagem

        ret
        
#####################################################################################
# Função: hue
# Descrição: calcula a componente Hue a partir das componentes R, G e B de um pixel.
# Argumentos: 
#	a3 - R
#	a4 - G
#	a5 - B
# Retorno: 
#	a6 - Valor da Hue
#####################################################################################   		
hue:	
	addi sp, sp, -4			# aloca espaco na stack
	sw ra, 0(sp)			# guarda ra na stack
	addi t2, zero, 60		#Colocar o valor 60 num registo
	
	blt a3, a4, condicao2		# R > G
	bge a5, a4, condicao2		# G >= B
	sub a6, a4, a5			# Hue = G - B
	mul a6, a6, t2			# Hue = Hue * 60
	sub t4, a3, a5			# R - B
	div a6, a6, t4			# Hue = Hue / (R - B)
	jal indicator
	beqz zero, SAIR
	
	condicao2:
	bge a3, a4, condicao3		# G >= R
	blt a3, a5, condicao3		# R > B
	sub a6, a3, a5			# Hue = R - B
	mul a6, a6, t2			# Hue = Hue * 60
	sub t4, a4, a5			# G - B
	div a6, a6, t4			# Hue = Hue / (G - B)
	addi t3, zero, 120		# Colocar o valor 120 num registo
	sub a6, t3, a6			# Hue = 120 - Hue
	jal indicator
	beqz zero, SAIR
	
	condicao3:
	blt a4, a5, condicao4		# G > B
	bge a3, a5, condicao4		# B >= R
	sub a6, a5, a3			# Hue = B - R
	mul a6, a6, t2			# Hue = Hue * 60
	sub t4, a4, a3			# G - R
	div a6, a6, t4			# Hue = Hue / (G - R)
	addi a6, a6, 120		# Hue = Hue + 120
	jal indicator
	beqz zero, SAIR
	
	condicao4:	
	bge a4, a5, condicao5		# B >= G
	blt a4, a3, condicao5		# G > R
	sub a6, a4, a3			# Hue = G - R
	mul a6, a6, t2			# Hue = Hue * 60
	sub t4, a5, a3			# B - R
	div a6, a6, t4			# Hue = Hue / (B - R) 
	addi t3, zero, 240		# Colocar o valor 240 num registo
	sub a6, t3, a6			# Hue = 240 - Hue
	jal indicator
	beqz zero, SAIR
	
	condicao5:
	blt a5, a3, condicao6		# B > R
	bge a4, a3, condicao6		# R >= G
	sub a6, a3, a4			# Hue = R - G
	mul a6, a6, t2			# Hue = Hue * 60
	sub t4, a5, a4			# B - G
	div a6, a6, t4			# Hue = Hue / (B - G)
	addi a6, a6, 240		# Hue = Hue + 240
	jal indicator
	beqz zero, SAIR
	
	condicao6:
	bge a5, a3, condicao7		#R >= B
	blt a5, a4, condicao7		#B > G
	sub a6, a5, a4			# Hue = B - G
	mul a6, a6, t2			# Hue = Hue * 60
	sub t4, a3, a4			# R - G
	div a6, a6, t4			# Hue = Hue / (R - G)
	addi t3, zero, 360		# Colocar o valor 360 num registo
	sub a6, t3, a6			# Hue = 360 - Hue
	jal indicator
	beqz zero, SAIR
	
	condicao7:
	add a6, zero, zero		# Hue = 0
	jal indicator
	
	SAIR:
	lw, ra, 0(sp)			# recupera ra original da stack
	addi sp, sp, 4			# liberta espaco na stack
	ret
	
#################################################################################################
# Função: indicator
# Descrição: dado um pixel com componentes R, G, B, indica a que personagem esse pixel pertence
# Argumentos: 
#	a6 - Valor da Hue
# Retorno: 
#	s0 - Personagem indicado
#################################################################################################	
indicator:
	add s0, zero, zero		
	addi t1, zero, 40
	addi t2, zero, 80
	addi t3, zero, 1
	addi t4, zero, 15
	addi t5, zero, 160
	addi t6, zero, 180
	
	yoda:
	blt a6, t1, darthmaul		# 40 <= Hue <= 80
	blt t2, a6, darthmaul		
	addi s0, zero, 1		# s0 = 1
	ret
	
	darthmaul:
	blt a6, t3, mandalorian		# 1 <= Hue <= 15
	blt t4, a6, mandalorian        
	addi s0, zero, 2		# s0 = 2
	ret
	
	mandalorian:
	blt a6, t5, nenhum		# 160 <= Hue <= 180
	blt t6, a6, nenhum              
	addi s0, zero, 3		# s0 = 3
	ret
	
	nenhum:
	ret
	
##################################################################################################
# Função: main
# Descrição: Pede ao user para escolher um personagem e depois executar as funções implementadas
# Argumentos: 
#	a0 - string com a frase inicial
#	a7 - Comandos syscall
# Retorno: 0
##################################################################################################	
main:
	addi t0, zero, 1
	addi t1, zero, 3
	addi, a7, zero, 4     	# Exibe a frase de inicio
	la a0, frase		# Colocar o endereço da string em a0
	ecall
	addi a7, zero, 5  	# Lê o personagem escolhido, até ser válido
	WHILE: 
	ecall
	blt a0, t0, WHILE	# 1 <= Personagem <= 3
	blt t1, a0, WHILE
	add s7, a0, zero	# s7 = a0
	
	la a0, imagem           # Colocar o endereço da imagem de input em a0
	jal read_rgb_image	# Função read_rgb_image
	jal location		# Função location
	la a0, imagem2		# Colocar o endereço da imagem de output em a0
	li a2, 172800		#Tamanho do pixeis_array
	jal write_rgb_image	#Função write_rgb_image
		
		
		
		
