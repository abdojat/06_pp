import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IServerServices extends Remote {

    // Create a chat room
    boolean createRoom(String roomName)
            throws RemoteException;

    // Get available chat rooms
    String[] listRooms()
            throws RemoteException;

    // Join a chat room
    boolean joinRoom(String username, String roomName,
                     IClientListener listener)
            throws RemoteException;

    // Leave a chat room
    void leaveRoom(String username, String roomName)
            throws RemoteException;

    // Send a private message
    void sendMessage(String from, String to,
                     String roomName, String message)
            throws RemoteException;

    // Send a message to everyone in a room
    void broadcastMessage(String from, String roomName,
                          String message)
            throws RemoteException;

    // Get users in a chat room
    String[] listUsers(String roomName)
            throws RemoteException;
}