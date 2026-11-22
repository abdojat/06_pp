
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ClientListener extends UnicastRemoteObject
        implements IClientListener {

    private final String clientId;

    public ClientListener(String clientId) throws RemoteException {
        super();
        this.clientId = clientId;
    }

    @Override
    public void receiveMessage(String message)
            throws RemoteException {

        System.out.println();
        System.out.println("================================");
        System.out.println(" NEW MESSAGE");
        System.out.println("================================");
        System.out.println(message);
        System.out.println("================================");
        System.out.print("> ");
    }

    @Override
    public String getClientId() throws RemoteException {
        return clientId;
    }
}
