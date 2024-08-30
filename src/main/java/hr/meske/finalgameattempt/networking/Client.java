package hr.meske.finalgameattempt.networking;

import hr.meske.finalgameattempt.State.GameManager;
import hr.meske.finalgameattempt.chat.ChatClient;
import hr.meske.finalgameattempt.chat.ChatService;
import javafx.application.Platform;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Client {
    private ClientSideConnection clientSideConnection;
    private ChatService chatService;
    private ChatClient chatClient;
    GameManager parent;
    private int clientID;

    public Client(GameManager parent) {
        this.parent = parent;
    }

    public void connectToServer() {

        clientSideConnection = new ClientSideConnection();

        try {
            chatService = (ChatService) Naming.lookup("rmi://localhost/ChatService");
            chatClient = new ChatClientImpl();
            chatService.registerClient(chatClient);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getClientID() {
        return clientID;
    }

    public void sendMessageToChat(String username, String message) {
        try {
            chatService.sendMessage(username, message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public  ClientSideConnection getClientSideConnection() {
        return clientSideConnection;
    }

    private class ChatClientImpl extends UnicastRemoteObject implements ChatClient {
        protected ChatClientImpl() throws RemoteException {}

        @Override
        public void receiveMessage(String message) throws RemoteException {
            Platform.runLater(() -> {
                parent.displayChatMessage(message);
            });
        }
    }

    public class ClientSideConnection {
        private Socket socket;
        private DataInputStream inputStream;
        private DataOutputStream outputStream;

        public DataOutputStream getOutputStream() {
            return outputStream;
        }

        public ClientSideConnection() {
            try {
                this.socket = new Socket("localhost", Server.SERVER_PORT);
                this.inputStream = new DataInputStream(socket.getInputStream());
                this.outputStream = new DataOutputStream(this.socket.getOutputStream());

                clientID = inputStream.readInt();
                System.out.println("Connected to server as Client " + clientID);


                Thread listeningThread = new Thread(() -> {
                    try {
                        while (true) {
                            int dataSize = inputStream.readInt();
                            byte[] data = new byte[dataSize];
                            inputStream.readFully(data);

                            Platform.runLater(() -> {
                                System.out.println("Received data: " + new String(data));

                                try {
                                    parent.recieveFromServer(data);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            });

                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                });
                listeningThread.start();

            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}