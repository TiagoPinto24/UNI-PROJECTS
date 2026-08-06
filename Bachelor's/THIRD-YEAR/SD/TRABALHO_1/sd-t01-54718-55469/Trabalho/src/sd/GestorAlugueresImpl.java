package sd;

import java.sql.*;
import java.io.FileInputStream;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Properties;

import sd.GestorAlugueres;

public class GestorAlugueresImpl extends UnicastRemoteObject implements GestorAlugueres {

    public GestorAlugueresImpl() throws RemoteException {
        super();
    }

    // Tudo o que envolve a database está nesta função
    // Quanto se chama uma função usa-se im int que leva ao respetivo caso no switch
    ArrayList<String> Connect_Database(int i, String S) throws Exception {
        String host = "", db = "", user = "", password = "", port = "";

        try (InputStream input = new FileInputStream("sd/parametros.properties")) {

            Properties prop = new Properties();

            // vai buscar os valores ao parametros.properties
            prop.load(input);
            host = prop.getProperty("host");
            db = prop.getProperty("db");
            user = prop.getProperty("user");
            password = prop.getProperty("password");
            port = prop.getProperty("port");

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Conectar á base de dados
        PostgresConnector pc = new PostgresConnector(host, db, user, password, port);

        pc.connect();
        Statement stmt = pc.getStatement();

        // O que é returnado, pode ser nda, uma mensagem para o sistema ou o output da
        // função
        ArrayList<String> resposta = new ArrayList<String>();

        // Array de strings onde se guarda a informação necessária para executar cada
        // função (pode ser nada)
        String[] separados = S.split(" ");

        switch (i) {
            case 1:
                // Registar um novo veiculo
                try {
                    stmt.executeUpdate(
                            "INSERT INTO public.veiculos (matricula,modelo,tipo,local,estado_aluguer,estado_admin) VALUES ('"
                                    +
                                    separados[0] + "', '" + separados[1] + "', '" + separados[2] + "', '" + separados[3]
                                    + "', '" + separados[4] + "', '" + separados[5] + "')");

                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println("Problems on insert new vehicle");
                }
                break;

            case 2:
                // Registar um novo cliente
                try {
                    stmt.executeUpdate("INSERT INTO public.clientes (nome,contacto,documento_identificacao) VALUES ('" +
                            separados[0] + "', '" + separados[1] + "', '" + separados[2] + "')");
                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println("Problems on insert new client");
                }
                break;

            case 3:
                // Registar um novo aluguer
                try {
                    ResultSet rs = stmt.executeQuery(
                            "SELECT * FROM public.veiculos WHERE estado_aluguer = 'disponivel' AND estado_admin = 'aprovado'");
                    if (rs.next() != false) {
                        stmt.executeUpdate(
                                "INSERT INTO public.aluguer (veiculo,cliente,pagamento,data,tempo,local) VALUES ('" +
                                        separados[0] + "', '" + separados[1] + "', '" + separados[2] + "', '"
                                        + separados[3]
                                        + "', '" + separados[4] + "', '" + separados[5] + "')");
                        stmt.executeUpdate("UPDATE public.veiculos SET estado_aluguer = 'alugado' WHERE matricula = '"
                                + separados[0] + "';");
                        resposta.add(" ");
                    } else
                        resposta.add("error");

                } catch (Exception e) {
                    e.printStackTrace();
                    resposta.add("error");
                    System.err.println("Problems on insert new client");
                }
                break;

            case 4:
                // Listar veiculos disponiveis para aluguer com filtros por localização e tipo
                // de veiculo
                try {
                    ResultSet rs;
                    if (!separados[0].equals("todas")) {
                        if (!separados[1].equals("todas")) {
                            rs = stmt.executeQuery(
                                    "SELECT * FROM public.veiculos WHERE estado_aluguer = 'disponivel' AND local = '" +
                                            separados[0] + "' AND tipo = '" + separados[1] + "'");
                        }
                        rs = stmt.executeQuery(
                                "SELECT * FROM public.veiculos WHERE estado_aluguer = 'disponivel' AND local = '" +
                                        separados[0] + "'");
                    } else if (!separados[1].equals("todas")) {
                        rs = stmt.executeQuery(
                                "SELECT * FROM public.veiculos WHERE estado_aluguer = 'disponivel' AND tipo = '"
                                        + separados[1] + "'");
                    } else {
                        rs = stmt.executeQuery("SELECT * FROM public.veiculos WHERE estado_aluguer = 'disponivel'");
                    }

                    // obter a linha toda
                    while (rs.next()) {
                        StringBuilder linha = new StringBuilder();
                        ResultSetMetaData dados = rs.getMetaData();
                        int ncolunas = dados.getColumnCount();

                        for (int j = 1; j <= ncolunas; j++) {
                            linha.append(dados.getColumnName(j))
                                    .append(": ")
                                    .append(rs.getString(j).trim());

                            if (j < ncolunas) {
                                linha.append(" | ");
                            }
                        }
                        resposta.add(linha.toString());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println("Problems on reaching List");
                }
                break;

            case 5:
                // Listar localizações de veiculos alugados
                try {
                    ResultSet rs = stmt.executeQuery("SELECT * FROM public.veiculos WHERE estado_aluguer = 'alugado'");
                    while (rs.next()) {
                        resposta.add(rs.getString("local"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println("Problems on reaching List");
                }
                break;

            case 6:
                // Listar histórico de aluguer de um veiculo
                try {
                    ResultSet rs = stmt
                            .executeQuery("SELECT * FROM public.aluguer WHERE veiculo = '" + separados[0] + "'");

                    // obter a linha toda
                    while (rs.next()) {
                        StringBuilder linha = new StringBuilder();
                        ResultSetMetaData dados = rs.getMetaData();
                        int ncolunas = dados.getColumnCount();

                        for (int j = 1; j <= ncolunas; j++) {
                            linha.append(dados.getColumnName(j))
                                    .append(": ")
                                    .append(rs.getString(j).trim());

                            if (j < ncolunas) {
                                linha.append(" | ");
                            }
                        }
                        resposta.add(linha.toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println("Problems on reaching List");
                }
                break;

            case 7:
                // Caso queira remover os elementos da database é só desabilitar os comentários
                /* 
                stmt.executeUpdate("DELETE FROM aluguer");
                stmt.executeUpdate("DELETE FROM veiculos");
                stmt.executeUpdate("DELETE FROM clientes");*/

                break;
            case 8:
                // Listar veículos por estado administrativo
                ResultSet rs;
                rs = stmt.executeQuery("SELECT * FROM public.veiculos WHERE estado_admin = 'aprovado'");
                resposta.add("Aprovados:\n");

                // obter a linha toda
                while (rs.next()) {
                    StringBuilder linha = new StringBuilder();
                    ResultSetMetaData dados = rs.getMetaData();
                    int ncolunas = dados.getColumnCount();

                    for (int j = 1; j <= ncolunas; j++) {
                        linha.append(dados.getColumnName(j))
                                .append(": ")
                                .append(rs.getString(j).trim());

                        if (j < ncolunas) {
                            linha.append(" | ");
                        }
                    }
                    resposta.add(linha.toString());
                }

                rs = stmt.executeQuery("SELECT * FROM public.veiculos WHERE estado_admin = 'naoaprovado'");
                resposta.add("Não aprovados:\n");

                // obter a linha toda
                while (rs.next()) {
                    StringBuilder linha = new StringBuilder();
                    ResultSetMetaData dados = rs.getMetaData();
                    int ncolunas = dados.getColumnCount();

                    for (int j = 1; j <= ncolunas; j++) {
                        linha.append(dados.getColumnName(j))
                                .append(": ")
                                .append(rs.getString(j).trim());

                        if (j < ncolunas) {
                            linha.append(" | ");
                        }
                    }
                    resposta.add(linha.toString());
                }
                break;
            case 9:
                // Aprovar um veiculo, alterar o estado_admin para aprovado
                try {
                    stmt.executeUpdate("UPDATE public.veiculos SET estado_admin = 'aprovado' WHERE matricula = '"
                            + separados[0] + "';");
                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println("Problems on reaching List");
                }
                break;

            default:
                break;
        }
        pc.disconnect();
        return resposta;

    }

    @Override
    public void RegistarVeiculo(String veiculo) throws RemoteException {
        try {
            Connect_Database(1, veiculo);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Problems with the connection to the database");
        }
    }

    @Override
    public void RegistarCliente(String cliente) throws RemoteException {
        try {
            Connect_Database(2, cliente);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Problems with the connection to the database");
        }
    }

    @Override
    public ArrayList<String> RegistarAloguer(String aluguer) throws RemoteException {
        ArrayList<String> valida = new ArrayList<String>();
        try {
            // System.out.println(aluguer);
            valida = Connect_Database(3, aluguer);
        } catch (Exception e) {
            e.printStackTrace();
            valida.add("error");
            System.out.println("Problems with the connection to the database");
        }
        return valida;
    }

    @Override
    public ArrayList<String> ListarVeiculosDisponiveis(String filtros) throws RemoteException {
        ArrayList<String> Veiculos_disponiveis = new ArrayList<String>();
        try {
            Veiculos_disponiveis = Connect_Database(4, filtros);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Problems with the connection to the database");
        }

        return Veiculos_disponiveis;
    }

    @Override
    public ArrayList<String> ListarVeiculosAlugados() throws RemoteException {
        ArrayList<String> Veiculos_alugados = new ArrayList<String>();
        try {
            Veiculos_alugados = Connect_Database(5, " ");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Problems with the connection to the database");
        }
        return Veiculos_alugados;
    }

    @Override
    public ArrayList<String> HistroicoAlugueres(String veiculo) throws RemoteException {
        ArrayList<String> Historico_alugueres = new ArrayList<String>();
        try {
            Historico_alugueres = Connect_Database(6, veiculo);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Problems with the connection to the database");
        }
        return Historico_alugueres;
    }

    public void Close() throws RemoteException {
        try {
            Connect_Database(7, " ");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Problems with the connection to the database");
        }
    }

    // CLIENTE ADMINISTRATIVO
    @Override
    public ArrayList<String> ListarVeiculosAdmin() throws RemoteException {
        ArrayList<String> Veiculos_admin = new ArrayList<String>();
        try {
            Veiculos_admin = Connect_Database(8, " ");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Problems with the connection to the database");
        }
        return Veiculos_admin;
    }

    @Override
    public void AprovarVeiculo(String veiculo) throws RemoteException {
        try {
            Connect_Database(9, veiculo);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Problems with the connection to the database");
        }
    }
}