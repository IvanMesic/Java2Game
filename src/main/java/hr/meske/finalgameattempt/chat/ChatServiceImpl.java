package hr.meske.finalgameattempt.chat;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class ChatServiceImpl extends UnicastRemoteObject implements ChatService {
    private List<ChatClient> clients;

    public ChatServiceImpl() throws RemoteException {
        clients = new ArrayList<>();
    }

    @Override
    public synchronized void sendMessage(String username, String message) throws RemoteException {
        String fullMessage = username + ": " + message;
        for (ChatClient client : clients) {
            client.receiveMessage(fullMessage);
        }
    }

    @Override
    public synchronized void registerClient(ChatClient client) throws RemoteException {
        clients.add(client);
    }

    @Override
    public synchronized void unregisterClient(ChatClient client) throws RemoteException {
        clients.remove(client);
    }
}
