package hr.meske.finalgameattempt.networking;

import hr.meske.finalgameattempt.chat.ChatService;
import hr.meske.finalgameattempt.chat.ChatServiceImpl;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
    private ServerSocket serverSocket;
    public static final int SERVER_PORT = 1000;
    private int numberOfPlayers = 0;
    private ConcurrentHashMap<Integer, ServerSideConnection> clientConnections = new ConcurrentHashMap<>();

    public Server() {
        try {
            serverSocket = new ServerSocket(SERVER_PORT);
            System.out.println("Server started on port " + SERVER_PORT);
            LocateRegistry.createRegistry(1099);
            ChatService chatService = new ChatServiceImpl();
            Naming.rebind("ChatService", chatService);
            System.out.println("ChatService is bound in RMI registry");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void acceptConnections() {
        try {
            System.out.println("Waiting for connections...");
            while (clientConnections.size() < 2) {
                Socket socket = serverSocket.accept();
                numberOfPlayers++;
                ServerSideConnection serverSideConnection = new ServerSideConnection(socket, numberOfPlayers);
                clientConnections.put(numberOfPlayers, serverSideConnection);
                Thread thread = new Thread(serverSideConnection);
                thread.start();
                System.out.println("Player " + numberOfPlayers + " connected.");
            }
            System.out.println("All players connected!");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private class ServerSideConnection implements Runnable {
        private Socket socket;
        private DataInputStream inputStream;
        private DataOutputStream outputStream;
        private int playerID;

        public ServerSideConnection(Socket socket, int playerID) {
            this.socket = socket;
            this.playerID = playerID;
            try {
                inputStream = new DataInputStream(socket.getInputStream());
                outputStream = new DataOutputStream(socket.getOutputStream());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        public void run() {
            try {
                outputStream.writeInt(playerID);
                outputStream.flush();



                while (true) {
                    int dataSize = inputStream.readInt();
                    byte[] data = new byte[dataSize];
                    inputStream.readFully(data);

                    System.out.println("Received data from player " + playerID);

                    for (ServerSideConnection clientConnection : clientConnections.values()) {

                        if (clientConnection == this) {
                            //continue;
                        }

                        clientConnection.outputStream.writeInt(dataSize);
                        clientConnection.outputStream.write(data);
                        clientConnection.outputStream.flush();
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }


    public static void main(String[] args) {
        Server server = new Server();
        server.acceptConnections();
    }
}
