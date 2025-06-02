package sd;

import java.rmi.Naming;
import java.util.ArrayList;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try {

            GestorAlugueres server = (GestorAlugueres) Naming.lookup("rmi://localhost:1099/GestorAluguerImpl");
            Scanner scan = new Scanner(System.in);
            int opcao = 0;

            System.out.println(
                    "Conectado com sucesso!\nBem-vindo à área de cliente, que operação deseja realizar?\n(Escolha o número correspondente)");

            while (opcao < 7) {

                System.out.println("1-Registar um novo veículo");
                System.out.println("2-Registar um novo cliente");
                System.out.println("3-Registar um novo aluguer realizado");
                System.out.println("4-Consultar lista de veículos disponiveis para aluguer");
                System.out.println("5-Consultar lista de localizações de veículos alugados");
                System.out.println("6-Consultar histórico de aluguer de um veículo");
                System.out.println("7-Sair");

                opcao = scan.nextInt();
                String resposta = null;

                switch (opcao) {
                    case 1:
                        //registar um novo veiculo
                        String veiculo;

                        System.out.println("Qual a matrícula do veículo?");
                        resposta = scan.next();
                        veiculo = resposta;

                        System.out.println("Qual o modelo do veículo?");
                        resposta = scan.next();
                        veiculo = veiculo + " " + resposta;

                        System.out.println("Qual o tipo de veículo?");
                        resposta = scan.next();
                        veiculo = veiculo + " " + resposta;

                        System.out.println("Qual a localização atual do veículo?");
                        resposta = scan.next();
                        veiculo = veiculo + " " + resposta;

                        veiculo = veiculo + " disponivel naoaprovado";

                        server.RegistarVeiculo(veiculo);
                        System.out.println("O veículo foi registado com sucesso!");
                        break;

                    case 2:
                        //Registar um novo cliente
                        String cliente;

                        System.out.println("Qual o nome do novo cliente?");
                        resposta = scan.next();
                        cliente = resposta;

                        System.out.println("Qual o contato?");
                        resposta = scan.next();
                        cliente = cliente + " " + resposta;

                        System.out.println("Qual o documento de identificação?");
                        resposta = scan.next();
                        cliente = cliente + " " + resposta;

                        server.RegistarCliente(cliente);
                        System.out.println("Novo cliente registado com sucesso!");
                        break;

                    case 3:
                        //Registar um novo aluguer, só é possivel se houver um cliente e um veiculo já inseridos
                        String aluguer;

                        System.out.println("Qual a matrícula do veículo alugado?");
                        resposta = scan.next();
                        aluguer = resposta;

                        System.out.println("Qual o documento de identificação do cliente?");
                        resposta = scan.next();
                        aluguer = aluguer + " " + resposta;

                        System.out.println("Qual foi o custo do aluguer?");
                        resposta = scan.next();
                        aluguer = aluguer + " " + resposta;

                        System.out.println("Em que data aconteceu o aluguer?");
                        resposta = scan.next();
                        aluguer = aluguer + " " + resposta;

                        System.out.println("Qual a duração prevista do aluguer?");
                        resposta = scan.next();
                        aluguer = aluguer + " " + resposta;

                        System.out.println("Para onde deseja o aluguer?");
                        resposta = scan.next();
                        aluguer = aluguer + " " + resposta;

                        ArrayList<String> verifica = server.RegistarAloguer(aluguer);
                        if (verifica.get(0).equals("error"))
                            System.out.println(
                                    "Cliente não existente ou veiculo nao disponivel para aluguer ou não existente!");
                        else
                            System.out.println("Aluguer registado com sucesso!");
                        break;

                    case 4:
                        //Consultar listade veículos disponiveis para aluguer, com filtros de localização e tipo
                        String filtros;

                        System.out
                                .println("De que localização quer consultar?\n(digite *todas* para não ter restrição)");
                        resposta = scan.next();
                        filtros = resposta;

                        System.out.println(
                                "Que tipo de veículo quer consultar?\n(digite *todas* para não ter restrição)");
                        resposta = scan.next();
                        filtros = filtros + " " + resposta;
                        System.out.println(filtros);

                        ArrayList<String> Veiculos_disponiveis = server.ListarVeiculosDisponiveis(filtros);
                        for (int i = 0; i < Veiculos_disponiveis.size(); i++)
                            System.out.println(Veiculos_disponiveis.get(i));
                        break;

                    case 5:
                        //Lista localizações de veiculos alugados
                        ArrayList<String> localizacoes = server.ListarVeiculosAlugados();
                        for (int i = 0; i < localizacoes.size(); i++)
                            System.out.println(localizacoes.get(i));
                        break;

                    case 6:
                        //Listar histórico de alugueres de um veiculo pela matricula
                        String veiculo6;

                        System.out.println(
                                "De que veiculo gostaria de consultar o histórico de alugueres?\ninsira a matrícula.");
                        resposta = scan.next();
                        veiculo6 = resposta + " ";

                        ArrayList<String> historico = server.HistroicoAlugueres(veiculo6);
                        for (int i = 0; i < historico.size(); i++)
                            System.out.println(historico.get(i));
                        break;

                    case 7:
                        server.Close();
                        break;

                    default:
                        break;
                }

                if (opcao < 7)
                    System.out.println("Que outra operação gostaria de realizar?");

            }

            scan.close();

        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}