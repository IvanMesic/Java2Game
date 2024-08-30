package hr.meske.finalgameattempt.State;

import hr.meske.finalgameattempt.model.*;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "GameState")
public class GameState implements Serializable {


    @XmlElement(name = "GamePhaseIndex")
    public int GamePhaseIndex = 1;

    @XmlElement(name = "PointsRed")
    public int pointsRed = 0;

    @XmlElement(name = "PointsBlue")
    public int pointsBlue = 0;

    public List<String> turnActionLog = new ArrayList<>();
    public int getGamePhaseIndex() {
        return GamePhaseIndex;
    }
    public void setGamePhaseIndex(int index) {
        GamePhaseIndex = index;
    }

    @XmlElement(name = "SoldiersLeftToPlace")
    public int soldiersLeftToPlace = 3;
    @XmlElement(name = "SoldiersLeftToMove")
    public int soldiersLeftToMove = 3;
    @XmlElement(name = "AttacksRemaining")
    public int attackRemaining = 2;

    @XmlTransient
    public String testString = "testString";

    @XmlElement(name = "CurrentPlayerTurn")
    public int currentPlayerTurn = 1;

    @XmlElement(name = "BoardPieces")
    List<BoardPiece> boardPieces = new ArrayList<>();

    public List<BoardPiece> getBoardPieces() {
        return boardPieces;
    }

    public boolean checkWin() {
        return pointsRed >= 20 || pointsBlue >= 20;
    }

    public String getWinningTeam() {
        if (pointsRed >= 20) {
            return "Red";
        } else if (pointsBlue >= 20) {
            return "Blue";
        }
        return null;
    }

    public void addSoldier(int boardPieceId, Soldier soldier) {
        boardPieces.get(boardPieceId).addSoldier(soldier);
    }

    public GameState() {
    }



    public void setBoards() {
        boardPieces.add(new BoardPiece(0,  BoardType.PURPLE));
        boardPieces.add(new BoardPiece(1,  BoardType.GREY));
        boardPieces.add(new BoardPiece(2,  BoardType.BLACK));
        boardPieces.add(new BoardPiece(3, BoardType.ORANGE));
        boardPieces.add(new BoardPiece(4,  BoardType.GREEN));
        boardPieces.add(new BoardPiece(5,  BoardType.YELLOW));
        boardPieces.add(new BoardPiece(6,  BoardType.BLUE));
        boardPieces.add(new BoardPiece(7,  BoardType.RED));
        boardPieces.add(new BoardPiece(8,  BoardType.PURPLE));
        boardPieces.add(new BoardPiece(9,  BoardType.BLACK));
        boardPieces.add(new BoardPiece(10,  BoardType.GREY));
        boardPieces.add(new BoardPiece(11,  BoardType.ORANGE));
        boardPieces.add(new BoardPiece(12,  BoardType.GREEN));
        boardPieces.add(new BoardPiece(13,  BoardType.YELLOW));
        boardPieces.add(new BoardPiece(14,  BoardType.BLUE));
        boardPieces.add(new BoardPiece(15,  BoardType.RED));


        boardPieces.get(15).neighbours.add(boardPieces.get(11));
        boardPieces.get(15).neighbours.add(boardPieces.get(14));

        boardPieces.get(14).neighbours.add(boardPieces.get(13));
        boardPieces.get(14).neighbours.add(boardPieces.get(10));
        boardPieces.get(14).neighbours.add(boardPieces.get(15));

        boardPieces.get(13).neighbours.add(boardPieces.get(14));
        boardPieces.get(13).neighbours.add(boardPieces.get(9));
        boardPieces.get(13).neighbours.add(boardPieces.get(8));
        boardPieces.get(13).neighbours.add(boardPieces.get(12));

        boardPieces.get(12).neighbours.add(boardPieces.get(8));
        boardPieces.get(12).neighbours.add(boardPieces.get(13));

        boardPieces.get(11).neighbours.add(boardPieces.get(6));
        boardPieces.get(11).neighbours.add(boardPieces.get(7));
        boardPieces.get(11).neighbours.add(boardPieces.get(10));
        boardPieces.get(11).neighbours.add(boardPieces.get(15));

        boardPieces.get(10).neighbours.add(boardPieces.get(5));
        boardPieces.get(10).neighbours.add(boardPieces.get(6));
        boardPieces.get(10).neighbours.add(boardPieces.get(9));
        boardPieces.get(10).neighbours.add(boardPieces.get(14));
        boardPieces.get(10).neighbours.add(boardPieces.get(11));

        boardPieces.get(9).neighbours.add(boardPieces.get(8));
        boardPieces.get(9).neighbours.add(boardPieces.get(13));
        boardPieces.get(9).neighbours.add(boardPieces.get(10));
        boardPieces.get(9).neighbours.add(boardPieces.get(4));
        boardPieces.get(9).neighbours.add(boardPieces.get(5));

        boardPieces.get(8).neighbours.add(boardPieces.get(9));
        boardPieces.get(8).neighbours.add(boardPieces.get(13));
        boardPieces.get(8).neighbours.add(boardPieces.get(12));

        boardPieces.get(7).neighbours.add(boardPieces.get(3));
        boardPieces.get(7).neighbours.add(boardPieces.get(6));
        boardPieces.get(7).neighbours.add(boardPieces.get(11));

        boardPieces.get(6).neighbours.add(boardPieces.get(5));
        boardPieces.get(6).neighbours.add(boardPieces.get(1));
        boardPieces.get(6).neighbours.add(boardPieces.get(2));
        boardPieces.get(6).neighbours.add(boardPieces.get(3));
        boardPieces.get(6).neighbours.add(boardPieces.get(7));
        boardPieces.get(6).neighbours.add(boardPieces.get(10));
        boardPieces.get(6).neighbours.add(boardPieces.get(11));

        boardPieces.get(5).neighbours.add(boardPieces.get(4));
        boardPieces.get(5).neighbours.add(boardPieces.get(0));
        boardPieces.get(5).neighbours.add(boardPieces.get(1));
        boardPieces.get(5).neighbours.add(boardPieces.get(6));
        boardPieces.get(5).neighbours.add(boardPieces.get(9));
        boardPieces.get(5).neighbours.add(boardPieces.get(10));

        boardPieces.get(4).neighbours.add(boardPieces.get(5));
        boardPieces.get(4).neighbours.add(boardPieces.get(0));

        boardPieces.get(3).neighbours.add(boardPieces.get(7));
        boardPieces.get(3).neighbours.add(boardPieces.get(6));
        boardPieces.get(3).neighbours.add(boardPieces.get(2));

        boardPieces.get(2).neighbours.add(boardPieces.get(1));
        boardPieces.get(2).neighbours.add(boardPieces.get(3));
        boardPieces.get(2).neighbours.add(boardPieces.get(6));

        boardPieces.get(1).neighbours.add(boardPieces.get(0));
        boardPieces.get(1).neighbours.add(boardPieces.get(5));
        boardPieces.get(1).neighbours.add(boardPieces.get(6));
        boardPieces.get(1).neighbours.add(boardPieces.get(2));

        boardPieces.get(0).neighbours.add(boardPieces.get(1));
        boardPieces.get(0).neighbours.add(boardPieces.get(5));
        boardPieces.get(0).neighbours.add(boardPieces.get(4));
    }


    public boolean boardPiecesAreNeighbours(int bpId1, int bpId2) {
        return boardPieces.get(bpId1).neighbours.contains(boardPieces.get(bpId2));
    }
}
