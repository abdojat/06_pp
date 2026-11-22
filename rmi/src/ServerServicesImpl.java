
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServerServicesImpl extends UnicastRemoteObject
        implements IServerServices {

    private final Map<String, Map<String, IClientListener>> rooms =
            new ConcurrentHashMap<>();

    public ServerServicesImpl() throws RemoteException {
        super();
        createRoom("General");
    }

    @Override
    public boolean createRoom(String roomName) throws RemoteException {
        if (roomName == null || roomName.isBlank()) {
            return false;
        }

        return rooms.putIfAbsent(
                roomName, new ConcurrentHashMap<>()) == null;
    }

    @Override
    public String[] listRooms() throws RemoteException {
        return rooms.keySet().toArray(new String[0]);
    }

    @Override
    public boolean joinRoom(String username, String roomName,
                            IClientListener listener)
            throws RemoteException {

        if (username == null || username.isBlank() || listener == null) {
            return false;
        }

        Map<String, IClientListener> users = rooms.get(roomName);

        if (users == null) {
            return false;
        }

        boolean joined = users.putIfAbsent(username, listener) == null;

        if (joined) {
            System.out.println(username + " joined " + roomName);
        }

        return joined;
    }

    @Override
    public void leaveRoom(String username, String roomName)
            throws RemoteException {

        Map<String, IClientListener> users = rooms.get(roomName);

        if (users != null) {
            users.remove(username);
            System.out.println(username + " left " + roomName);
        }
    }

    @Override
    public void sendMessage(String from, String to,
                            String roomName, String message)
            throws RemoteException {

        Map<String, IClientListener> users = rooms.get(roomName);

        if (users == null || !users.containsKey(from)) {
            throw new RemoteException("Sender is not in the room");
        }

        IClientListener receiver = users.get(to);

        if (receiver == null) {
            throw new RemoteException("Receiver not found");
        }

        // CALLBACK: Server invokes a method on the client
        receiver.receiveMessage("[Private] " + from + ": " + message);

        System.out.println("Private message: " + from + " -> " + to);
    }

    @Override
    public void broadcastMessage(String from, String roomName,
                                 String message)
            throws RemoteException {

        Map<String, IClientListener> users = rooms.get(roomName);

        if (users == null || !users.containsKey(from)) {
            throw new RemoteException("Sender is not in the room");
        }

        for (Map.Entry<String, IClientListener> entry : users.entrySet()) {
            if (!entry.getKey().equals(from)) {
                try {
                    // CALLBACK to each connected client
                    entry.getValue().receiveMessage(
                            "[Room " + roomName + "] "
                                    + from + ": " + message);
                } catch (RemoteException e) {
                    System.err.println(
                            "Could not deliver to " + entry.getKey());
                }
            }
        }
    }

    @Override
    public String[] listUsers(String roomName)
            throws RemoteException {

        Map<String, IClientListener> users = rooms.get(roomName);

        if (users == null) {
            return new String[0];
        }

        return users.keySet().toArray(new String[0]);
    }
}
