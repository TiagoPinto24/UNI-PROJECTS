package sd;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;


public interface GestorAlugueres extends Remote{
    //cliente regular
    void RegistarVeiculo(String veiculo) throws RemoteException;
    void RegistarCliente(String cliente) throws RemoteException;
    ArrayList<String> RegistarAloguer(String aluguer) throws RemoteException;
    ArrayList<String> ListarVeiculosDisponiveis(String filtros) throws RemoteException;
    ArrayList<String> ListarVeiculosAlugados() throws RemoteException;
    ArrayList<String> HistroicoAlugueres(String veiculo) throws RemoteException;
    void Close()throws RemoteException;

    //cliente administrativo
    ArrayList<String> ListarVeiculosAdmin() throws RemoteException;
    void AprovarVeiculo(String veiculo) throws RemoteException;
}