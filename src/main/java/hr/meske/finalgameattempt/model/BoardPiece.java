package hr.meske.finalgameattempt.model;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "BoardPiece")
public class BoardPiece implements Serializable {

    public BoardPiece(int id, BoardType type) {
        this.id = id;
        this.type = type;
    }

    public BoardPiece() {}

    @XmlElement(name = "BoardType")
    public BoardType type;

    @XmlAttribute
    public int id;

    public int getBoardId() {
        return id;
    }

    @XmlTransient
    public List<BoardPiece> neighbours = new ArrayList<>();
    @XmlElement(name = "Soldiers")
    public List<Soldier> soldiers = new ArrayList<>();

    public List<Soldier> getSoldiers() {
        return soldiers;
    }

    public void addNeighbor(BoardPiece neighbor) {
        neighbours.add(neighbor);
    }

    public List<BoardPiece> getNeighbours() {
        return neighbours;
    }

    public void addSoldier(Soldier soldier) {
        soldiers.add(soldier);
    }

    public void removeSoldier(Soldier soldier) {
        soldiers.remove(soldier);
    }

    public void AddSoldiers(List<Soldier> soldiers) {
        this.soldiers.addAll(soldiers);
    }

    public void removeSoldiers(int numOfSoldiersToRemove) {
        this.soldiers.remove(numOfSoldiersToRemove);
    }
}
