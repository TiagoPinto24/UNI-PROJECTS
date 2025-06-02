package SD;

import java.sql.*;
/**
 *
 * @author jsaias
 */
public class PostgresConnector {

    private String PG_HOST;
    private String PG_DB;
    private String USER;
    private String PWD;
    private String PG_PORT;

    Connection con = null;
    Statement stmt = null;

    public PostgresConnector(String host, String db, String user, String pw, String port) {
        PG_HOST=host;
        PG_DB= db;
        USER=user;
        PWD= pw;
        PG_PORT = port;
    }

    public void connect() throws Exception {
        try {
            Class.forName("org.postgresql.Driver");
            // Correctly construct the URL
            String url = "jdbc:postgresql://" + PG_HOST + ":" + PG_PORT + "/" + PG_DB;
            con = DriverManager.getConnection(url, USER, PWD);
            stmt = con.createStatement();

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Problems setting the connection");
        }
    }

    public void disconnect() {// importante: fechar a ligacao á BD
        try {
            stmt.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Statement getStatement() {
        return stmt;
    }
}