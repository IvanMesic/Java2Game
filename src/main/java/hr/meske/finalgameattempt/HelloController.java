package hr.meske.finalgameattempt;

import hr.meske.finalgameattempt.State.GameManager;
import hr.meske.finalgameattempt.State.GameState;
import hr.meske.finalgameattempt.model.BoardPiece;
import hr.meske.finalgameattempt.model.Soldier;
import hr.meske.finalgameattempt.model.Team;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HelloController {


    SerializationController serializationController = new SerializationController();

    private int subTurn = 1;
    private int firstBoardPieceSelected;
    private Map<Integer, FlowPane> boardPieceToFlowPaneMap;

    GameManager gameManager = null;
    public void setGameManager(GameManager gameManager) {
        this.gameManager = gameManager;
        gameManager.getGameState().SetBoards();
    }

    public void initialize() {

        btnAddSoldier.setDisable(true);
        btnAttack.setDisable(true);
        btnMove.setDisable(true);
        btnPlay.setDisable(true);

        boardPieceToFlowPaneMap = new HashMap<>();
        boardPieceToFlowPaneMap.put(0, fpPurpleOne);
        boardPieceToFlowPaneMap.put(1, fpBlackOne);
        boardPieceToFlowPaneMap.put(2, fpGreyOne);
        boardPieceToFlowPaneMap.put(3, fpOrangeOne);
        boardPieceToFlowPaneMap.put(4, fpGreenOne);
        boardPieceToFlowPaneMap.put(5, fpYellowOne);
        boardPieceToFlowPaneMap.put(6, fpBlueOne);
        boardPieceToFlowPaneMap.put(7, fpRedOne);
        boardPieceToFlowPaneMap.put(8, fpPurpleTwo);
        boardPieceToFlowPaneMap.put(9, fpBlackTwo);
        boardPieceToFlowPaneMap.put(10, fpGreyTwo);
        boardPieceToFlowPaneMap.put(11, fpOrangeTwo);
        boardPieceToFlowPaneMap.put(12, fpGreenTwo);
        boardPieceToFlowPaneMap.put(13, fpYellowTwo);
        boardPieceToFlowPaneMap.put(14, fpBlueTwo);
        boardPieceToFlowPaneMap.put(15, fpRedTwo);
    }

    @FXML
    private VBox mainVBox;

    @FXML
    private AnchorPane mainAnchorPane;
    @FXML
    private Button btnSaveGame;
    @FXML
    private Button btnLoadGame;
    @FXML
    private Button btnAddSoldier;
    @FXML
    private Button btnMove;
    @FXML
    private Button btnAttack;
    @FXML
    private Button btnPlay;

    @FXML
    private FlowPane fpPurpleOne;
    @FXML
    private FlowPane fpBlackOne;
    @FXML
    private FlowPane fpOrangeOne;
    @FXML
    private FlowPane fpRedOne;
    @FXML
    private FlowPane fpBlueOne;
    @FXML
    private FlowPane fpYellowOne;
    @FXML
    private FlowPane fpGreenOne;
    @FXML
    private FlowPane fpPurpleTwo;
    @FXML
    private FlowPane fpBlackTwo;
    @FXML
    private FlowPane fpGreyTwo;
    @FXML
    private FlowPane fpRedTwo;
    @FXML
    private FlowPane fpBlueTwo;
    @FXML
    private FlowPane fpGreyOne;
    @FXML
    private FlowPane fpOrangeTwo;
    @FXML
    private FlowPane fpGreenTwo;
    @FXML
    private FlowPane fpYellowTwo;


    @FXML
    private Label welcomeText;
    @FXML
    private Label placeHolderText;
    @FXML
    private TextArea textArea;


    public void setTextArea(String text) {
        welcomeText.setText(text);
    }

    public void onHelloButtonClick(ActionEvent actionEvent) throws IOException {


    }

    @FXML
    public void onBtnSaveGameClicked() {
        GameState gameState = gameManager.getGameState();
        serializationController.saveGameStateToXML(gameState);
    }

    @FXML
    public void onBtnLoadGameClicked() {
        File[] savedFiles = serializationController.listSavedGameFiles();
        if (savedFiles.length == 0) {
            showAlert("No Saved Games", "There are no saved games available.");
            return;
        }

        ChoiceDialog<File> dialog = new ChoiceDialog<>(savedFiles[0], savedFiles);
        dialog.setTitle("Load Game");
        dialog.setHeaderText("Select a game to load:");
        dialog.setContentText("Saved Games:");

        dialog.showAndWait().ifPresent(selectedFile -> {
            GameState loadedGameState = serializationController.loadGameStateFromXML(selectedFile.getAbsolutePath());
            if (loadedGameState != null) {
                gameManager.setGameState(loadedGameState);
                drawBoard();
                try {
                    gameManager.sendToServer();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                showAlert("Game Loaded", "Game state loaded successfully.");
            } else {
                showAlert("Load Failed", "Failed to load the game state.");
            }
        });
    }
    public void onFlowPanePressed(MouseEvent event) throws IOException {

        FlowPane flowPane = (FlowPane) event.getSource();
        String id = flowPane.getId();
        int boardPieceId = boardPieceToFlowPaneMap.entrySet().stream().filter(e -> e.getValue().equals(flowPane)).findFirst().get().getKey();


        switch (gameManager.getGameState().getGamePhaseIndex()){
            case 1:
                addSoldier(boardPieceId);
                break;
            case 2:
                moveSoldier(boardPieceId);
                break;
            case 3:
                attack(boardPieceId);
                break;
        }
    }

    private void attack(int boardPieceId) {

        if (gameManager.getGameState().attackRemaining <= 0) {
            showAlert("No Attacks Left", "You have no attacks remaining.");
            return;
        }

        if (subTurn == 1) {
            if (gameManager.getGameState().getBoardPieces().get(boardPieceId).getSoldiers().getFirst().getTeam().ordinal() + 1 != gameManager.getGameState().currentPlayerTurn) {
                showAlert("Invalid Action", "You can't attack with enemy soldiers.");
                return;
            }
            firstBoardPieceSelected = boardPieceId;
            subTurn = 2;
        } else {
            if (gameManager.getGameState().getBoardPieces().get(boardPieceId).getSoldiers().getFirst().getTeam().ordinal() + 1 == gameManager.getGameState().currentPlayerTurn) {
                showAlert("Invalid Action", "You can't damage your own troops.");
                return;
            }
            if (!gameManager.boardPiecesAreNeighbours(firstBoardPieceSelected, boardPieceId)) {
                showAlert("Invalid Move", "The selected board pieces are not adjacent.");
                return;
            }

            int attackingSoldiersAttack = gameManager.getGameState().getBoardPieces().get(firstBoardPieceSelected).getSoldiers().stream().mapToInt(Soldier::getAttack).sum();
            int numberOfDefendingSoldiers = gameManager.getGameState().getBoardPieces().get(boardPieceId).getSoldiers().size();

            int damage = attackingSoldiersAttack / numberOfDefendingSoldiers;

            gameManager.getGameState().getBoardPieces().get(boardPieceId).getSoldiers().forEach(soldier -> {
                soldier.deduceHP(damage);
            });

            for (BoardPiece boardPiece : gameManager.getGameState().getBoardPieces()) {
                boardPiece.getSoldiers().removeIf(soldier -> soldier.getHp() <= 0);
            }

            gameManager.getGameState().getBoardPieces().get(boardPieceId).getSoldiers().forEach(soldier -> {
                System.out.println(soldier.getHp());
            });

            subTurn = 1;
            gameManager.getGameState().attackRemaining--;

            showAlert("Attack Completed", "You have " + gameManager.getGameState().attackRemaining + " attacks remaining.");
        }
    }

    private void moveSoldier(int boardPieceId) {
        if (subTurn == 1) {
            if (gameManager.getGameState().getBoardPieces().get(boardPieceId).getSoldiers().getFirst().getTeam().ordinal() + 1 != gameManager.getGameState().currentPlayerTurn) {
                showAlert("Invalid Action", "You can only move your own soldiers.");
                return;
            }
            firstBoardPieceSelected = boardPieceId;
            subTurn = 2;
            showAlert("Select Destination", "Select a destination board piece.");

            String boardPieceIdString = boardPieceToFlowPaneMap.getOrDefault(boardPieceId, null).toString();

        } else {
            if (gameManager.getGameState().getBoardPieces().get(boardPieceId).getSoldiers().getFirst().getTeam().ordinal() + 1 != gameManager.getGameState().currentPlayerTurn) {
                return;
            }

            if (gameManager.boardPiecesAreNeighbours(firstBoardPieceSelected, boardPieceId)) {
                gameManager.getGameState().getBoardPieces().get(boardPieceId).getSoldiers().addAll(gameManager.getGameState().getBoardPieces().get(firstBoardPieceSelected).getSoldiers());
            } else {
                showAlert("Invalid Move", "The selected pieces aren't neighbours. Please try again.");
                return;
            }

            gameManager.getGameState().getBoardPieces().get(firstBoardPieceSelected).getSoldiers().clear();
            gameManager.getGameState().soldiersLeftToMove--;
            subTurn = 1;

        }
    }

    private void addSoldier(int boardPieceId) {

        if (gameManager.getGameState().soldiersLeftToPlace <= 0) {
            showAlert("No Soldiers Left", "You have no soldiers left to place.");
            return;
        }
        if (!gameManager.getGameState().getBoardPieces().get(boardPieceId).getSoldiers().isEmpty()) {
            if (gameManager.getGameState().getBoardPieces().get(boardPieceId).getSoldiers().getFirst().getTeam().ordinal() + 1 == gameManager.oppositeTurnIndex() || gameManager.getGameState().soldiersLeftToPlace == 0) {
                showAlert("Invalid Placement", "You can't place a soldier on an enemy board piece.");
                return;
            }
        }
        gameManager.getGameState().addSoldier(boardPieceId, new Soldier(gameManager.getGameState().currentPlayerTurn == 1 ? Team.RED : Team.BLUE));
        gameManager.getGameState().soldiersLeftToPlace--;

    }

    public void drawBoard() {
        for (int i = 0; i < 16; i++) {
            FlowPane flowPane = boardPieceToFlowPaneMap.get(i);
            flowPane.getChildren().clear();
            for (Soldier soldier : gameManager.getGameState().getBoardPieces().get(i).getSoldiers()) {
                String emoji = soldier.getTeam() == Team.RED ? "🐝" : "🥷🏻";
                flowPane.getChildren().add(new Label(emoji));
            }
        }
    }

    public void onBtnPlayClicked(MouseEvent event) throws IOException {

        gameManager.resetCounters();
        if (gameManager.getGameState().currentPlayerTurn == 1) {
            gameManager.getGameState().currentPlayerTurn = 2;
        } else {
            gameManager.getGameState().currentPlayerTurn = 1;
            if (gameManager.getGameState().getGamePhaseIndex() == 1) {
                gameManager.getGameState().setGamePhaseIndex(2);
            } else if (gameManager.getGameState().getGamePhaseIndex() == 2) {
                gameManager.getGameState().setGamePhaseIndex(3);
            } else if (gameManager.getGameState().getGamePhaseIndex() == 3) {
                gameManager.getGameState().setGamePhaseIndex(1);
            }
        }

        showAlert("Turn Ended", "You have ended your turn.");
        gameManager.sendToServer();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    public void onBtnAddSoldierClicked(MouseEvent event) {
    }

    public void onBtnAttackClicked(MouseEvent event) {
    }

    public void onBtnMoveSoldierClicked(MouseEvent event) {
    }

    public void enableButtons() {
        btnPlay.setDisable(false);

        if (gameManager.getGameState().getGamePhaseIndex() == 1) {
            btnAddSoldier.setDisable(false);
        } else if (gameManager.getGameState().getGamePhaseIndex() == 2) {
            btnMove.setDisable(false);
        } else if (gameManager.getGameState().getGamePhaseIndex() == 3) {
            btnAttack.setDisable(false);
        }

    }

    public void disableButtons() {

        btnAddSoldier.setDisable(true);
        btnMove.setDisable(true);
        btnAttack.setDisable(true);
        btnPlay.setDisable(true);
    }

    public void onFlowPaneReleased(MouseEvent event) {

        drawBoard();
    }
}