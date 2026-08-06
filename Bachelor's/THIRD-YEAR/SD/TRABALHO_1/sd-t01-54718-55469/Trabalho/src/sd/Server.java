package sd;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {
    public static void main(String[] args) {
        try {
            Registry registro = LocateRegistry.createRegistry(1099);
            GestorAlugueresImpl server = new GestorAlugueresImpl();

            registro.rebind("GestorAluguerImpl", server);
            System.out.println("Servidor está pronto!!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}