package SD;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Scanner;

public class Client_admin {

    // config da API
    private static final String BASE_URL = Server.prop().getProperty("HttpURL") + "/devices";
    private static final String USERNAME = Server.prop().getProperty("API_username");
    private static final String PASSWORD = Server.prop().getProperty("API_password");

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        int option = 0;

        System.out.println("Bem-vindo à página do cliente administrativo.");
        while (option < 4) {

            System.out.println("Que operação deseja realizar?");
            System.out.println("1- Temperatura média");
            System.out.println("2- Humidade média");
            System.out.println("3- Inserir comando personalizado");
            System.out.println("4- Sair");
            System.out.print("Opção: ");
            option = scan.nextInt();
            scan.nextLine();

            switch (option) {
                case 1:
                    System.out.println(
                            "Deseja ver a temperatura média desde de que dia? (se não desejar, digite 0)\nExemplo:AAAA/MM/DD");
                    System.out.print("Dia: ");
                    String diaInputInicial = scan.nextLine();
                    String diaInputFinal;
                    if (diaInputInicial.equals("0"))
                        diaInputFinal = "0";
                    else {
                        System.out.println("Deseja ver a temperatura média até que dia? \nExemplo:AAAA/MM/DD");
                        System.out.print("Dia: ");
                        diaInputFinal = scan.nextLine();
                    }

                    System.out
                            .println("Deseja ver a temperatura média por:\n1-edificios\n2-pisos\n3-salas\n4-serviços");
                    System.out.print("Opção: ");
                    int option2 = scan.nextInt();
                    scan.nextLine();

                    System.out.println(diaInputInicial + " - " + diaInputFinal);

                    try {
                        if (option2 == 4) {
                            System.out.println("De que serviço deseja consultar?");
                            String servico = scan.nextLine();
                            averageTemperature(diaInputInicial, diaInputFinal, option2, servico.toLowerCase());
                        } else
                            averageTemperature(diaInputInicial, diaInputFinal, option2, null);
                    } catch (IOException e) {
                        System.out.println("Error: " + e.getMessage());
                        e.printStackTrace();
                    }
                    break;

                case 2:
                    System.out.println(
                            "Deseja ver a humidade média desde de que dia? (se não desejar, digite 0)\nExemplo:AAAA/MM/DD");
                    System.out.print("Dia: ");
                    String diaInputInicialH = scan.nextLine();
                    String diaInputFinalH;
                    if (diaInputInicialH.equals("0"))
                        diaInputFinalH = "0";
                    else {
                        System.out.println("Deseja ver a humidade média até que dia? \nExemplo:AAAA/MM/DD");
                        System.out.print("Dia: ");
                        diaInputFinalH = scan.nextLine();
                    }
                    System.out.println("Deseja ver a humidade média por:\n1-edificios\n2-pisos\n3-salas\n4-serviços");
                    System.out.print("Opção: ");
                    int option3 = scan.nextInt();
                    scan.nextLine();

                    try {
                        if (option3 == 4) {
                            System.out.println("De que serviço deseja consultar?");
                            String servico = scan.nextLine();
                            // System.out.println("servico: " + servico + "option3: " + option3);
                            averageHumidity(diaInputInicialH, diaInputFinalH, option3, servico.toLowerCase());
                        } else
                            averageHumidity(diaInputInicialH, diaInputFinalH, option3, null);
                    } catch (IOException e) {
                        System.err.println("Error: " + e.getMessage());
                        e.printStackTrace();
                    }
                    break;

                case 3:
                    System.out.println("Comandos Disponiveis:");
                    System.out.println();
                    System.out.println("Criar *id* *edificio* *piso* *sala* *servico*");
                    System.out.println("Ler");
                    System.out.println("Atualizar *id* *componente* *alteração*");
                    System.out.println("Remover *id*");
                    System.out.println();
                    System.out.println("Insira o comando:");
                    String comando = scan.nextLine();
                    String[] comandoSeparados = comando.split(" ", 2);

                    if (comandoSeparados[0].toUpperCase().equals("CRIAR")) {
                        try {
                            if (comandoSeparados.length < 2)
                                System.err.println("Comando inválido! Certifique-se de inserir o comando completo.");
                            else
                                addDevice(comandoSeparados[1]);
                        } catch (IOException e) {
                            System.err.println("Error: " + e.getMessage());
                            e.printStackTrace();
                        }
                    } else if (comandoSeparados[0].toUpperCase().equals("LER")) {
                        try {
                            listDevices();
                        } catch (IOException e) {
                            System.err.println("Error: " + e.getMessage());
                            e.printStackTrace();
                        }
                    } else if (comandoSeparados[0].toUpperCase().equals("ATUALIZAR")) {
                        String[] aux = comandoSeparados[1].split(" ", 2);
                        try {
                            if (comandoSeparados.length < 2)
                                System.err.println("Comando inválido! Certifique-se de inserir o comando completo.");
                            else
                                updateDevice(aux[0], aux[1]);
                        } catch (IOException e) {
                            System.err.println("Error: " + e.getMessage());
                            e.printStackTrace();
                        }
                    } else if (comandoSeparados[0].toUpperCase().equals("REMOVER")) {
                        try {
                            if (comandoSeparados.length < 2)
                                System.err.println("Comando inválido! Certifique-se de inserir o comando completo.");
                            else
                                deleteDevice(comandoSeparados[1]);
                        } catch (IOException e) {
                            System.err.println("Error: " + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                    break;

                case 4:
                    break;
                default:
                    System.out.println("Opção inválida.");
                    break;
            }
        }
        scan.close();
    }

    // Funções para a temperatura média
    public static void averageTemperature(String diaInicial, String diaFinal, int optionFiltro, String servico)
            throws IOException {
        String url = BASE_URL + "/averageTemperature/" + diaInicial + "_" + diaFinal + "_";
        switch (optionFiltro) {
            case 1:
                sendRequest("GET", url + "edificio", null);
                break;
            case 2:
                sendRequest("GET", url + "edificio_piso", null);
                break;
            case 3:
                sendRequest("GET", url + "edificio_piso_sala", null);
                break;
            case 4:
                sendRequest("GET", url + "edificio_piso_sala_" + servico.replace(" ", "-"), null);
                break;
            default:
                System.out.println("Opção inválida.");
                break;
        }
    }

    // Função para a humidade média
    public static void averageHumidity(String diaInicial, String diaFinal, int optionFiltro, String servico)
            throws IOException {
        String url = BASE_URL + "/averageHumidity/" + diaInicial + "_" + diaFinal + "_";

        switch (optionFiltro) {
            case 1:
                sendRequest("GET", url + "edificio", null);
                break;
            case 2:
                sendRequest("GET", url + "edificio_piso", null);
                break;
            case 3:
                sendRequest("GET", url + "edificio_piso_sala", null);
                break;
            case 4:
                sendRequest("GET", url + "edificio_piso_sala_" + servico.replace(" ", "-"), null);
                break;
            default:
                System.out.println("Opção inválida.");
                break;
        }
    }

    // Adicionar um novo dispositivo
    public static void addDevice(String jsonString) throws IOException {
        sendRequest("POST", BASE_URL, jsonString);
    }

    // Listar todos
    public static void listDevices() throws IOException {
        sendRequest("GET", BASE_URL, null);
    }

    // Atualizar um dispositivo
    public static void updateDevice(String id, String jsonString) throws IOException {
        String url = BASE_URL + "/" + id;
        sendRequest("PUT", url, jsonString);
    }

    // Apagar um dispositivo
    public static void deleteDevice(String id) throws IOException {
        String url = BASE_URL + "/" + id;
        sendRequest("DELETE", url, null);
    }

    private static void sendRequest(String method, String url, String body) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod(method);

        String encodedCredentials = Base64.getEncoder()
                .encodeToString((USERNAME + ":" + PASSWORD).getBytes(StandardCharsets.UTF_8));
        connection.setRequestProperty("Authorization", "Basic " + encodedCredentials);

        if (body != null && (method.equals("POST") || method.equals("PUT"))) {
            // Mandamos o output na sua forma original, para ser tratado pelo server como
            // uma string normal
            connection.setDoOutput(true);

            connection.setRequestProperty("Content-Type", "application/json");
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = body.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
        }

        int responseCode = connection.getResponseCode();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                responseCode >= 200 && responseCode < 300 ? connection.getInputStream()
                        : connection.getErrorStream()))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line.trim());
            }
            System.out.println("Response: " + response);
        }
    }
}