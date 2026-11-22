
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServerMain {

    public static void main(String[] args) {
        try {
            // Start RMI Registry on port 6000
            Registry registry = LocateRegistry.createRegistry(6000);

            // Create the chat server
            ServerServicesImpl server = new ServerServicesImpl();

            // Register the service
            registry.rebind("rmiServer", server);

            System.out.println("==============================");
            System.out.println("     RMI CHAT SERVER");
            System.out.println("==============================");
            System.out.println("Server started successfully!");
            System.out.println("Port: 6000");
            System.out.println("Service: rmiServer");
            System.out.println("Default room: General");
            System.out.println("Waiting for clients...");

        } catch (Exception e) {
            System.err.println("Server failed to start!");
            e.printStackTrace();
        }
    }
}
