import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IClientListener extends Remote {

    void receiveMessage(String message)
            throws RemoteException;

    String getClientId()
            throws RemoteException;
}