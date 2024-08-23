package hr.meske.finalgameattempt.State;

import hr.meske.finalgameattempt.HelloController;
import hr.meske.finalgameattempt.SerializationController;
import hr.meske.finalgameattempt.model.BoardPiece;
import hr.meske.finalgameattempt.model.GamePhase;
import hr.meske.finalgameattempt.model.Team;
import hr.meske.finalgameattempt.networking.Client;

import java.io.IOException;

public class GameManager {


    boolean isFirstRun = true;

    SerializationController serializationController = new SerializationController();

    HelloController helloController = null;

    public void setHelloController(HelloController helloController) {
        this.helloController = helloController;
    }
    Client client;
    GameState state;

    public GameManager() {
        client = new Client(this);
        client.connectToServer();
        state = new GameState();
    }
    public GameState getGameState() {
        return state;
    }
    public void setGameState(GameState state) {
        this.state = state;
    }

    public void sendToServer() throws IOException {

        byte[] bytes = serializationController.serializeIntoBytes(state);
        client.getClientSideConnection().getOutputStream().writeInt(bytes.length);
        client.getClientSideConnection().getOutputStream().write(bytes);
        client.getClientSideConnection().getOutputStream().flush();

        System.out.println("Sent to server");
    }


    public void recieveFromServer(byte[] data) throws IOException {


        state = serializationController.deserializeAndLoad(data);

        helloController.disableButtons();

        if (getGameState().currentPlayerTurn == client.getClientID()) {
            helloController.enableButtons();
        }


        helloController.drawBoard();


        System.out.println("Received from server");

    }

    public void initButtons() {

        int clientId = client.getClientID();

        if (clientId == 1) {
            helloController.enableButtons();
        } else {
            helloController.disableButtons();
        }
    }

    public boolean boardPiecesAreNeighbours(int bpId1, int bpId2){
        return getGameState().boardPiecesAreNeighbours(bpId1, bpId2);
    }


    public int oppositeTurnIndex(){
        if (getGameState().currentPlayerTurn == 1) {
            return 2;
        } else {
            return 1;
        }

    }


    public void resetCounters() {
        getGameState().attackRemaining = 2;
        getGameState().soldiersLeftToPlace = 3;
        getGameState().soldiersLeftToMove = 2;
    }
}
