package hr.meske.finalgameattempt.networking;

import hr.meske.finalgameattempt.State.GameManager;
import javafx.application.Platform;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    private ClientSideConnection clientSideConnection;

    GameManager parent;
    private int clientID;

    public Client(GameManager parent) {
        this.parent = parent;
    }

    public void connectToServer() {
        clientSideConnection = new ClientSideConnection();
    }

    public int getClientID() {
        return clientID;
    }


    public  ClientSideConnection getClientSideConnection() {
        return clientSideConnection;
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