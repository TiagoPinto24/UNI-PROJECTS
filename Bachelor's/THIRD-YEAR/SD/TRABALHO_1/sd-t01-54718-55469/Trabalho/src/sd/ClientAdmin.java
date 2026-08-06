package sd;

import java.rmi.Naming;
import java.util.ArrayList;
import java.util.Scanner;

public class ClientAdmin {
    public static void main(String[] args) {
        try {
            GestorAlugueres server = (GestorAlugueres) Naming.lookup("rmi://localhost:1099/GestorAluguerImpl");
            Scanner scan = new Scanner(System.in);
            int opcao = 0;

            System.out.println(
                    "Conectado com sucesso!\nBem-vindo à área de administrador, que operação deseja realizar?\n(Escolha o número correspondente)");

            while (opcao < 3) {

                System.out.println("1-Listar veículos por estado administrativo.");
                System.out.println("2-Aprovar um veículo");
                System.out.println("3-Sair");

                opcao = scan.nextInt();

                switch (opcao) {
                    case 1:
                        //Listar veiculos por estado administrativo
                        ArrayList<String> veiculos_admin = server.ListarVeiculosAdmin();
                        for (int i = 0; i < veiculos_admin.size(); i++)
                            System.out.println(veiculos_admin.get(i));
                        break;

                    case 2:
                        //aprovar um veiculo
                        String veiculo;
                        String resposta;

                        System.out.println("Qual a matrícula do veículo que deseja aprovar?");
                        resposta = scan.next();
                        veiculo = resposta + " ";

                        server.AprovarVeiculo(veiculo);
                        System.out.println("Veiculo aprovado com sucesso!");
                        break;

                    case 3:
                        server.Close();
                        break;

                    default:
                        break;
                }

                if (opcao < 3)
                    System.out.println("Que outra operação gostaria de realizar?");

            }

            scan.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}