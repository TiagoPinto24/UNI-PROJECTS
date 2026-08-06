package SD;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/devices")
public class Server {

    // abrir o ficheiro de parametros
    public static Properties prop() {
        Properties prop = new Properties();
        try {
            InputStream input = new FileInputStream("config/parametros.properties");
            prop.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prop;
    }

    // iniciar o servidor http
    public static HttpServer startServer() throws IOException {
        ResourceConfig rc = new ResourceConfig()
                .packages("SD")
                .register(Autenticacao.class);
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(prop().getProperty("HttpURL")), rc);
    }

    static PostgresConnector Connect_Database() throws Exception {
        Properties prop = prop();

        // vai buscar os valores ao parametros.properties
        String host = prop.getProperty("hostDB");
        String db = prop.getProperty("dbDB");
        String user = prop.getProperty("userDB");
        String password = prop.getProperty("passwordDB");
        String port = prop.getProperty("portDB");

        // Conectar a base de dados
        PostgresConnector pc = new PostgresConnector(host, db, user, password, port);
        pc.connect();

        return pc;
    }

    // iniciar a conexão mqtt
    private static void MqttConnections() {
        String broker = prop().getProperty("brokerhost");
        String topic = prop().getProperty("brokertopic");
        String clientId = "Server";

        try {
            MqttClient client = new MqttClient(broker, clientId);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);

            client.setCallback(new MqttCallback() {
                public void connectionLost(Throwable cause) {
                    System.out.println("Conexão perdida: " + cause.getMessage());
                }

                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    String messageString = new String(message.getPayload());
                    System.out.println("Mensagem recebida: " + messageString);

                    String[] separados = messageString.split(" ");

                    // Inserção das métricas na base de dados, se o dispositivo existir
                    try {
                        PostgresConnector pc = Connect_Database();
                        Statement stmt = pc.getStatement();
                        ResultSet rs = stmt
                                .executeQuery("SELECT * FROM public.devices WHERE id = '" + separados[0] + "'");
                        if (rs.next()) {
                            stmt.executeUpdate(
                                    "INSERT INTO public.metrics(id,humidity,temperature,timestamp) VALUES ('" +
                                            separados[0] + "', " + separados[1].replace(",", ".") + ", "
                                            + separados[2].replace(",", ".") + ", '" +
                                            separados[3] + ", " + separados[4] + "')");
                        } else
                            System.out.println(
                                    "Dispositivo com o ID " + separados[0] + " não foi encontrado\nMétrica descartada");
                        pc.disconnect();
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.err.println("Problemas a inserir na base de dados");
                    }
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                }
            });
            client.connect(connOpts);
            System.out.println("Conectado");
            client.subscribe(topic);

        } catch (MqttException e) {
            System.err.println("Erro na conexão ao broker: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // função que limpa a base de dados e adiciona 5 dispositivos iniciais
    static void addDevicestoDatabase() throws Exception {
        PostgresConnector pc = Connect_Database();
        Statement stmt = pc.getStatement();
        try {
            stmt.executeUpdate("DELETE FROM public.metrics");
            stmt.executeUpdate("DELETE FROM public.devices");

            stmt.executeUpdate(
                    "INSERT INTO public.devices (ID, edificio, piso, sala, servico) VALUES ('1', 'A', '1', '1', 'recepcao')");
            stmt.executeUpdate(
                    "INSERT INTO public.devices (ID, edificio, piso, sala, servico) VALUES ('2', 'A', '2', '2', 'consultorio1')");
            stmt.executeUpdate(
                    "INSERT INTO public.devices (ID, edificio, piso, sala, servico) VALUES ('3', 'A', '2', '3', 'consultorio2')");
            stmt.executeUpdate(
                    "INSERT INTO public.devices (ID, edificio, piso, sala, servico) VALUES ('4', 'B', '1', '4', 'sala de operacoes')");
            stmt.executeUpdate(
                    "INSERT INTO public.devices (ID, edificio, piso, sala, servico) VALUES ('5', 'B', '2', '5', 'sala de cirurgias')");

            stmt.executeUpdate(
                    "INSERT INTO public.metrics (ID, temperature, humidity, timestamp) VALUES ('1', '22', '33', '2024/12/10 12:12:12')");
            stmt.executeUpdate(
                    "INSERT INTO public.metrics (ID, temperature, humidity, timestamp) VALUES ('1', '22', '33', '2024/12/10 12:12:13')");
            stmt.executeUpdate(
                    "INSERT INTO public.metrics (ID, temperature, humidity, timestamp) VALUES ('1', '40', '60', '2024/12/15 12:12:15')");
            stmt.executeUpdate(
                    "INSERT INTO public.metrics (ID, temperature, humidity, timestamp) VALUES ('1', '24', '35', '2024/12/11 12:12:14')");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pc.disconnect();
        }
    }

    public static void main(String[] args) throws Exception {
        addDevicestoDatabase();

        MqttConnections();
        System.out.println("Conexão ao broker Mqtt bem sucedida");

        startServer();
        System.out.println("Conexão ao servidor http bem sucedida");
    }

    // Adicionar um novo dispositivo
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response AddDevice(String device) {
        String[] separado = device.split(" ");
        try {
            PostgresConnector pc = Connect_Database();
            Statement stmt = pc.getStatement();

            stmt.executeUpdate("INSERT INTO public.devices (ID,edificio,piso,sala,servico) VALUES ('"
                    + separado[0] + "', '" + separado[1] + "', '" + separado[2] + "', '" + separado[3] + "', '"
                    + separado[4] + "')");
            pc.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Problemas ao adicionar um dispositivo").build();
        }
        return Response.status(Response.Status.OK).entity("Dispositivo com o id " + separado[0] + " adicionado")
                .build();
    }

    // Listar os dispositivos
    @GET
    public Response listDevices() {
        List<String> devices = new ArrayList<>();
        try {
            PostgresConnector pc = Connect_Database();
            Statement stmt = pc.getStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM public.devices");
            while (rs.next()) {
                StringBuilder linha = new StringBuilder();
                ResultSetMetaData dados = rs.getMetaData();
                int ncolunas = dados.getColumnCount();
                for (int j = 1; j <= ncolunas; j++) {
                    String aux = dados.getColumnName(j);
                    linha.append(aux)
                            .append(": ");
                    if (aux.equals("mean_temperature"))
                        linha.append(rs.getString(j).substring(0, 4).trim());
                    else
                        linha.append(rs.getString(j).trim());
                    if (j < ncolunas) {
                        linha.append(" | ");
                    }
                }
                devices.add(linha.toString());
            }
            pc.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Problemas ao listar dispositivos")
                    .build();
        }
        return Response.status(Response.Status.OK).entity("Dispositivos: " + devices).build();
    }

    // Atualizar um dispositivo
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateDevice(@PathParam("id") String id, String update) {
        String[] separado = update.split(" ");
        int result = 0;
        try {
            PostgresConnector pc = Connect_Database();
            Statement stmt = pc.getStatement();

            result = stmt.executeUpdate(
                    "UPDATE public.devices SET " + separado[0] + " = '" + separado[1] + "' WHERE ID = '" + id + "';");
            pc.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Problemas ao atualizar um dispositivo").build();
        }
        if (result == 0)
            return Response.status(Response.Status.OK)
                    .entity("Dispositivo com o id " + id + " nao encontrado ou atualização já existente").build();
        return Response.status(Response.Status.OK).entity("Dispositivo com o id " + id + " atualizado").build();
    }

    // Remover um dispositivo
    @DELETE
    @Path("/{id}")
    public Response deleteDevice(@PathParam("id") String id) {
        int result = 0;
        try {
            PostgresConnector pc = Connect_Database();
            Statement stmt = pc.getStatement();
            stmt.executeUpdate("DELETE FROM public.metrics WHERE ID = '" + id + "';");
            result = stmt.executeUpdate("DELETE FROM public.devices WHERE ID = '" + id + "';");
            pc.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Problemas ao remover dispositivo");
        }
        if (result == 0)
            return Response.status(Response.Status.OK).entity("Despositivo com o id " + id + " não encontrado").build();
        return Response.status(Response.Status.OK).entity("Despositivo com o id " + id + " removido").build();
    }

    // Listar as temperaturas médias
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/averageTemperature/{filtros}")
    public Response averageTemperature(@PathParam("filtros") String filtros) {
        String[] s = filtros.replace("-", " ").replace(":", "/").split("_");
        List<String> temperatures = new ArrayList<>();
        try {
            PostgresConnector pc = Connect_Database();
            Statement stmt = pc.getStatement();

            String query = "SELECT d." + s[2];
            if (s.length >= 4)
                query += ", d." + s[3];
            if (s.length >= 5)
                query += ", d." + s[4];
            if (s[0].equals("0")) {
                query += ", AVG(temperature) AS mean_temperature FROM public.metrics m JOIN public.devices d " +
                        "ON m.id = d.id WHERE m.timestamp >= CURRENT_TIMESTAMP - INTERVAL '1 DAY' ";
            } else {
                query += ", AVG(temperature) AS mean_temperature FROM public.metrics m JOIN public.devices d " +
                        "ON m.id = d.id WHERE m.timestamp >= '" + s[0] + "' AND m.timestamp <= '" + s[1] + "' ";
            }
            if (s.length == 6)
                query += "AND d.servico = '" + s[5] + "' ";
            query += "GROUP BY d." + s[2];
            if (s.length >= 4)
                query += ", d." + s[3];
            if (s.length >= 5)
                query += ", d." + s[4];
            query += ";";

            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                StringBuilder linha = new StringBuilder();
                ResultSetMetaData dados = rs.getMetaData();
                int ncolunas = dados.getColumnCount();

                for (int j = 1; j <= ncolunas; j++) {
                    String aux = dados.getColumnName(j);
                    linha.append(aux)
                            .append(": ");
                    if (aux.equals("mean_temperature"))
                        linha.append(rs.getString(j).substring(0, 4).trim());
                    else
                        linha.append(rs.getString(j).trim());
                    if (j < ncolunas) {
                        linha.append(" | ");
                    }
                }
                temperatures.add(linha.toString());
            }
            pc.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Problemas ao listar temperaturas")
                    .build();
        }
        return Response.status(Response.Status.OK).entity("Temperaturas: " + temperatures).build();
    }

    // Listar as humidades médias
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/averageHumidity/{filtros}")
    public Response averageHumidity(@PathParam("filtros") String filtros) {
        String[] s = filtros.replace("-", " ").replace(":", "/").split("_");
        List<String> humidities = new ArrayList<>();
            try {
            PostgresConnector pc = Connect_Database();
            Statement stmt = pc.getStatement();

            String query = "SELECT d." + s[2];
            if (s.length >= 4) query += ", d." + s[3];
            if (s.length >= 5) query += ", d." + s[4];
            if (s[0].equals("0")) {
                query += ", AVG(humidity) AS mean_humidity FROM public.metrics m JOIN public.devices d " +
                    "ON m.id = d.id WHERE m.timestamp >= NOW() - INTERVAL '1 DAY' ";
            } else {
            query += ", AVG(humidity) AS mean_humidity FROM public.metrics m JOIN public.devices d " +
                    "ON m.id = d.id WHERE m.timestamp >= '" + s[0] + "' AND m.timestamp <= '" + s[1] + "' ";
            }
            if (s.length == 6) query += "AND d.servico = '" + s[5] + "' ";
            query += "GROUP BY d." + s[2];
            if (s.length >= 4) query += ", d." + s[3];
            if (s.length >= 5) query += ", d." + s[4];
            query += ";";
            
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                StringBuilder linha = new StringBuilder();
                ResultSetMetaData dados = rs.getMetaData();
                int ncolunas = dados.getColumnCount();

                for (int j = 1; j <= ncolunas; j++) {
                    String aux = dados.getColumnName(j);
                    linha.append(aux)
                            .append(": ");
                    if (aux.equals("mean_humidity"))
                        linha.append(rs.getString(j).substring(0, 4).trim());
                    else
                        linha.append(rs.getString(j).trim());
                    if (j < ncolunas) {
                        linha.append(" | ");
                    }
                }
                humidities.add(linha.toString());
            }
            pc.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Problemas ao listar humidades").build();
            }
        return Response.status(Response.Status.OK).entity("Humidades: " + humidities).build();
    }
}