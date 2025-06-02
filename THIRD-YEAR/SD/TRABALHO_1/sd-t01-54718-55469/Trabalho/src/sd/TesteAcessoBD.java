package sd;


import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author jsaias
 */
public class TesteAcessoBD {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {

        // coloque os argumentos
        
        // PostgresConnector pc = new PostgresConnector( ?? );
        // NOTA: não DEVE ter configuracoes no código fonte!!!
        // passar como argumento ou ler de .properties
        
        PostgresConnector pc = new PostgresConnector("127.0.0.1", "sd", "postgres", "SD2024", "15432");
        
        // estabelecer a ligacao ao SGBD
        pc.connect();
        Statement stmt = pc.getStatement();

	// *******
        // update/insert
        try {

           String name = "Tiago";
           stmt.executeUpdate("INSERT INTO public.sogma (name) VALUES ('" + name + "')"); 

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Problems on insert...");
        }

	// ******
        // query	
        try {
            
            ResultSet rs =stmt.executeQuery("SELECT * FROM public.sogma");
            System.out.println("Res");
            while (rs.next()) {
                String nome =rs.getString("name");
                System.out.printf("NOME: %s\n", nome);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Problems retrieving data from db...");
        }

        // desligar do SGBD:
        pc.disconnect();
    }


}