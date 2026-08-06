package SD;

import org.eclipse.paho.client.mqttv3.*;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class IoTDevicePublisher {
    //MQTT
    private static MqttClient client;
    private static String broker = Server.prop().getProperty("brokerhost");
    private static String topic = Server.prop().getProperty("brokertopic");
    private static String clientId = "IoTDevicePublisher";

    //Lista para guardar os dispositivos
    private static List<IoTDevice> devices = new ArrayList<>();

    //Formatação das mensagens
    private static DecimalFormat numberFormat = new DecimalFormat("#.00");
    private static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        
    public static class IoTDevice {
        String ID;
        String edificio;
        String piso;
        String sala;
        String servico;

        public IoTDevice(String id, String edificio, String piso, String sala, String servico) {
            this.ID = id;
            this.edificio = edificio;
            this.piso = piso;
            this.sala = sala;
            this.servico = servico;
        }
    }
    
    //Função que inicializa a conexão do cliente publisher
    public static void ConnectClient() {
        try {
            client = new MqttClient(broker, clientId);
            client.connect();
            System.out.println("Conexão ao broker " + broker + " esucedida");
        } catch (MqttException e) {
            System.err.println("Error connecting to broker: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //Função que cria uma mensagem para ser publicada
    public static void publishDevice(IoTDevice device) {
        try {
            String message = device.ID;
                
            Random random = new Random();
            double temperature = 20 + (30 - 20) * random.nextDouble(); //Temperatura aleatoria entre 20 e 30
            double humidity = 30 + (50 - 30) * random.nextDouble(); //Humidade aleatoria entre 30 e 50
            message += " " + numberFormat.format(temperature) + " " + numberFormat.format(humidity);

            LocalDateTime now = LocalDateTime.now();
            message += " " + dtf.format(now);

            System.out.println("Publishing message: " + message);

            //Publicar a mensagem no broker
            MqttMessage mqttMessage = new MqttMessage(message.getBytes());
            mqttMessage.setQos(2);
            client.publish(topic, mqttMessage);

        } catch (MqttException e) {
            System.err.println("Ero ao publicar a mensagem:" + e.getMessage());
            e.printStackTrace();
        }
    }

    //Funçã para adicionar um dispositivo à lista
    public static void AddDevice(IoTDevice device) {
        System.out.println("Adding device: " + device.ID);
        devices.add(device);
    }

    public static void main(String[] args) throws InterruptedException {
        ConnectClient();

        //Adicionar dispositivos iniciais
        IoTDevice device = new IoTDevice("1", "A", "1", "1", "recepcao");
        AddDevice(device);
        device = new IoTDevice("2", "A", "2", "2", "consultorio1");
        AddDevice(device);
        device = new IoTDevice("3", "A", "2", "3", "consultorio2");
        AddDevice(device);
        device = new IoTDevice("4", "B", "1", "4", "sala de espera");
        AddDevice(device);
        device = new IoTDevice("5", "B", "2", "5", "sala de operacoes");
        AddDevice(device);
        device = new IoTDevice("6", "B", "3", "6", "sala de cirurgias");
        AddDevice(device);

        //de 15 em 15 segundos publica dados
        while (true) {
            for (IoTDevice i : devices)
                publishDevice(i);
            Thread.sleep(15000);
        }
    }
}