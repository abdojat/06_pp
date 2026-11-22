
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class ClientMain {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        ClientListener listener = null;
        IServerServices server = null;

        String username = null;
        String roomName = null;

        try {
            // Connect to the RMI server
            Registry registry =
                    LocateRegistry.getRegistry("localhost", 6000);

            server = (IServerServices) registry.lookup("rmiServer");

            System.out.println("==============================");
            System.out.println("       RMI CHAT CLIENT");
            System.out.println("==============================");
            System.out.println("Connected to server!");

            // Enter username
            System.out.print("Enter your username: ");
            username = scanner.nextLine().trim();

            if (username.isEmpty()) {
                System.out.println("Username cannot be empty.");
                return;
            }

            // Display available rooms
            System.out.println("\nAvailable rooms:");

            for (String room : server.listRooms()) {
                System.out.println("- " + room);
            }

            System.out.print("\nEnter room name: ");
            roomName = scanner.nextLine().trim();

            // Create callback object
            listener = new ClientListener(username);

            // Join selected room
            boolean joined =
                    server.joinRoom(username, roomName, listener);

            if (!joined) {
                System.out.println(
                        "Cannot join room. Check room name or username.");
                return;
            }

            System.out.println("\nJoined room: " + roomName);
            System.out.println("Welcome, " + username + "!");

            boolean running = true;

            while (running) {
                System.out.println("\n========== MENU ==========");
                System.out.println("1. Send private message");
                System.out.println("2. Broadcast message");
                System.out.println("3. List users");
                System.out.println("4. List rooms");
                System.out.println("5. Create room");
                System.out.println("6. Exit");
                System.out.print("Choose: ");

                String choice = scanner.nextLine().trim();

                switch (choice) {

                    case "1":
                        System.out.print("Send to: ");
                        String to = scanner.nextLine().trim();

                        System.out.print("Message: ");
                        String privateMessage = scanner.nextLine();

                        server.sendMessage(
                                username, to, roomName, privateMessage);

                        System.out.println("Message sent!");
                        break;

                    case "2":
                        System.out.print("Message: ");
                        String broadcast = scanner.nextLine();

                        server.broadcastMessage(
                                username, roomName, broadcast);

                        System.out.println("Broadcast sent!");
                        break;

                    case "3":
                        System.out.println("\nUsers in " + roomName + ":");

                        for (String user : server.listUsers(roomName)) {
                            System.out.println("- " + user);
                        }
                        break;

                    case "4":
                        System.out.println("\nAvailable rooms:");

                        for (String room : server.listRooms()) {
                            System.out.println("- " + room);
                        }
                        break;

                    case "5":
                        System.out.print("New room name: ");
                        String newRoom = scanner.nextLine().trim();

                        if (server.createRoom(newRoom)) {
                            System.out.println("Room created!");
                        } else {
                            System.out.println(
                                    "Room already exists or invalid name.");
                        }
                        break;

                    case "6":
                        server.leaveRoom(username, roomName);
                        roomName = null;
                        running = false;
                        System.out.println("Disconnected.");
                        break;

                    default:
                        System.out.println("Invalid option.");
                }
            }

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();

        } finally {
            // Release the exported callback object
            if (listener != null) {
                try {
                    UnicastRemoteObject.unexportObject(listener, true);
                } catch (Exception ignored) {
                }
            }

            scanner.close();
        }

        System.exit(0);
    }
}
