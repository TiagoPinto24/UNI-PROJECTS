package com.mds.projeto2;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Date;

public class Main {

	// Aguarda a confirmação do utilizador
	static void promptEnterKey() {
		System.out.println("Prima ENTER para continuar");
		Scanner scanner = new Scanner(System.in);
		scanner.nextLine();
	}

	public static void main(String[] args) {
		ArrayList<quarto> quartos = new ArrayList<>();
		Scanner input = new Scanner(System.in);

		// Exemplo de quartos
		// quartos.add(new quarto(201, 3, 2, true, true, "Serra"));
		// quartos.add(new quarto(202, 6, 4, true, false, "Mar"));
		// quartos.add(new quarto(203, 1, 1, true, true, "Mar"));
		// quartos.add(new quarto(204, 4, 2, false, false, "Parte de trás de outro edifício"));
		// quartos.add(new quarto(205, 3, 3, false, true, "Serra"));
		// quartos.add(new quarto(206, 2, 2, false, false, "Montanha"));

		while (true) {
			System.out.println("Menu");
			System.out.println("1 - Adicionar quarto");
			System.out.println("2 - Remover quarto");
			System.out.println("3 - Editar as propriedades do quarto");
			System.out.println("4 - Listar quartos");
			System.out.println("5 - Registar a manutenção");
			System.out.println("6 - Efetuar uma reserva");
			System.out.println("7 - Listar manutenções");
			System.out.println("9 - Sair");
			System.out.println("Escolha uma opção: ");
			int op = input.nextInt();

			switch (op) {
				case 1:

					System.out.println("Adicionar quarto ");
					System.out.println("Numero do quarto: ");
					int numero = input.nextInt();
					System.out.println("Capacidade maxima do quarto: ");
					int capacidadeMax = input.nextInt();
					System.out.println("Numero de camas do quarto: ");
					int nrcamas = input.nextInt();
					System.out.println("Cozinha do quarto: 1 - Sim");
					Boolean cozinha = input.nextInt() == 1;
					System.out.println("Varanda do quarto: 1 - Sim");
					Boolean varanda = input.nextInt() == 1;
					System.out.println("Vista do quarto: ");
					String vista = input.next();
					adicionarQuarto(quartos, numero, capacidadeMax, nrcamas, cozinha, varanda, vista);
					promptEnterKey();
					break;

				case 2:
					System.out.println("Apagar quarto");
					System.out.println("Digite o numero do quarto: ");
					int num = input.nextInt();
					int pos = getPosicaoporNumero(num, quartos);
					if (pos == -1) {
						System.out.println("Quarto nao existe");
						promptEnterKey();
						break;
					}
					removerQuarto(quartos, pos);
					System.out.println("Quarto " + num + " removido");
					promptEnterKey();
					break;

				case 3:
					System.out.println("Editar as propriedades do quarto");
					System.out.println("Digite o numero do quarto: ");
					num = input.nextInt();
					pos = getPosicaoporNumero(num, quartos);
					if (pos == -1) {
						System.out.println("Quarto não existe");
						promptEnterKey();
						break;
					}
					quarto quartoToEdit = quartos.get(pos);

					System.out.println("Editar quarto");
					System.out.println("Capacidade maxima do quarto: ");
					capacidadeMax = input.nextInt();
					System.out.println("Número de camas do quarto: ");
					nrcamas = input.nextInt();
					System.out.println("Cozinha do quarto: 1 - Sim");
					cozinha = input.nextInt() == 1;
					System.out.println("Varanda do quarto: 1 - Sim");
					varanda = input.nextInt() == 1;
					System.out.println("Vista do quarto: ");
					vista = input.next();

					quartoToEdit.setCapacidadeMax(capacidadeMax);
					quartoToEdit.setNrcamas(nrcamas);
					quartoToEdit.setCozinha(cozinha);
					quartoToEdit.setVaranda(varanda);
					quartoToEdit.setVista(vista);
					System.out.println(quartoToEdit.toString());
					promptEnterKey();
					break;

				case 4:
					System.out.println("Listar quartos");
					for (int i = 0; i < quartos.size(); i++) {
						System.out.println(quartos.get(i).toString());
					}
					promptEnterKey();
					break;

				case 5:
					System.out.println("Registar manutenção");
					System.out.println("Digite o numero do quarto: ");
					num = input.nextInt();
					int i = getPosicaoporNumero(num, quartos);
					if (i != -1) {
						System.out.print("tipo de manutenção: ");
						String tipo = input.next();
						quartos.get(i).registarManutencao(tipo);
						System.out.println("Manutenção registada com sucesso");
					}
					promptEnterKey();
					break;

				case 6:
					System.out.println("Efetuar reserva");
					System.out.println("Digite o numero do quarto:");
					num = input.nextInt();
					input.nextLine();
					i = getPosicaoporNumero(num, quartos);
					if (i == -1) {
						System.out.println("Quarto não existe");
						promptEnterKey();
						break;
					}
					if (quartos.get(i).manutencoesporConcluir() > 0) {
						System.out.println("Este quarto tem manutenções por realizar, deseja prosseguir: 1 - Sim");
						Boolean continuar = input.nextInt() == 1;
						if (!continuar)
							break;
					}
					System.out.print("Data de entrada (no formato YYYY-MM-DD): ");
					String dataEntradaString = input.next();
					System.out.print("Data de saida (no formato YYYY-MM-DD): ");
					String dataSaidaString = input.next();
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
					Date dataEntrada = null;
					Date dataSaida = null;
					try {
						dataEntrada = dateFormat.parse(dataEntradaString);
						dataSaida = dateFormat.parse(dataSaidaString);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					reservas reserva = new reservas(dataEntrada, dataSaida);
					System.out.println(reserva.toString());
					quartos.get(i).setreservas(reserva);

					promptEnterKey();
					break;

				case 7:
					System.out.println("Listar manutenções");
					System.out.println("Digite o numero do quarto: (ou 0 para todos os quartos)");
					num = input.nextInt();
					if (num == 0) {
						for (int j = 0; j < quartos.size(); j++) {
							System.out.println("\nQuarto " + quartos.get(j).getNumero() + ": ");
							quartos.get(j).getTodasManutencoes();
							System.out.println();
						}
					} else {
						i = getPosicaoporNumero(num, quartos);
						if (i != -1) {
							quartos.get(i).getTodasManutencoes();
						} else {
							System.out.println("Quarto não encontrado");
						}
					}
					promptEnterKey();
					break;

				case 9:
					return;
				default:
					System.out.println("Opção inválida");

			}
		}

	}

	public static void adicionarQuarto(ArrayList<quarto> quartos, int numero, int capacidadeMax, int nrcamas, boolean cozinha,
			boolean varanda, String vista) {
		quarto q = new quarto(numero, capacidadeMax, nrcamas, cozinha, varanda, vista);
		quartos.add(q);
	}

	public static void removerQuarto(ArrayList<quarto> quartos, int numero){
		if (numero != -1)
			quartos.remove(numero);
	}

	static int getPosicaoporNumero(int num, ArrayList<quarto> quartos) {
		for (int i = 0; i < quartos.size(); i++) {
			if (num == quartos.get(i).getNumero()) {
				return i;
			}
		}
		return -1;
	}

}