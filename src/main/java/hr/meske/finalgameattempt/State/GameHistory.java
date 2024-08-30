package hr.meske.finalgameattempt.State;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "GameHistory")
public class GameHistory implements Serializable {

    @XmlElement(name = "GameStates")
    private List<GameState> gameStates = new ArrayList<>();

    public List<GameState> getTurns() {
        return gameStates;
    }

    public int getNumberOfTurns() {
        return gameStates.size();
    }

    public GameState getTurn(int index) {
        if (index >= 0 && index < gameStates.size()) {
            return gameStates.get(index);
        }
        return null;
    }
    public void addTurn(GameState gameState) {
        this.gameStates.add(gameState);
    }
}
