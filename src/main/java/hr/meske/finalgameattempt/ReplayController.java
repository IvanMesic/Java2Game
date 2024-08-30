package hr.meske.finalgameattempt;

import hr.meske.finalgameattempt.State.GameHistory;
import hr.meske.finalgameattempt.State.GameState;
import hr.meske.finalgameattempt.model.Soldier;
import hr.meske.finalgameattempt.model.Team;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ReplayController {

    @FXML
    private FlowPane fpPurpleOne, fpBlackOne, fpOrangeOne, fpRedOne, fpBlueOne, fpYellowOne, fpGreenOne, fpPurpleTwo, fpBlackTwo, fpGreyTwo, fpRedTwo, fpBlueTwo, fpGreyOne, fpOrangeTwo, fpGreenTwo, fpYellowTwo;

    @FXML
    private Button btnLoadHistory, btnPrevious, btnNext, btnPlay, btnPause;

    @FXML
    private ListView<String> logListView;

    private Map<Integer, FlowPane> boardPieceToFlowPaneMap;
    private ObservableList<String> displayedLogs = FXCollections.observableArrayList();

    private GameHistory gameHistory;
    private int currentTurnIndex = 0;
    private boolean isPlaying = false;

    private SerializationController serializationController = new SerializationController();

    public void initialize() {
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

        logListView.setItems(displayedLogs);
        disableReplayButtons();
    }

    @FXML
    public void onBtnLoadHistoryClicked() {
        File[] historyFiles = serializationController.listGameHistoryFiles();
        if (historyFiles.length == 0) {
            showAlert("No Saved Histories", "There are no saved game histories available.");
            return;
        }

        ChoiceDialog<File> dialog = new ChoiceDialog<>(historyFiles[0], historyFiles);
        dialog.setTitle("Load Game History");
        dialog.setHeaderText("Select a game history to load:");
        dialog.setContentText("Saved Game Histories:");

        dialog.showAndWait().ifPresent(selectedFile -> {
            gameHistory = serializationController.loadGameHistoryFromXML(selectedFile.getAbsolutePath());
            if (gameHistory != null) {
                currentTurnIndex = 0;
                displayTurn(currentTurnIndex);
                enableReplayButtons();
                showAlert("Game History Loaded", "Game history loaded successfully.");
            } else {
                showAlert("Load Failed", "Failed to load the game history.");
            }
        });
    }

    private void displayTurn(int turnIndex) {
        if (gameHistory == null || turnIndex < 0 || turnIndex >= gameHistory.getTurns().size()) {
            return;
        }

        currentTurnIndex = turnIndex;
        GameState gameState = gameHistory.getTurns().get(turnIndex);

        // UI updates must be run on the JavaFX Application Thread
        Platform.runLater(() -> {
            // Update the board based on the gameState
            for (int i = 0; i < 16; i++) {
                FlowPane flowPane = boardPieceToFlowPaneMap.get(i);
                flowPane.getChildren().clear();
                for (Soldier soldier : gameState.getBoardPieces().get(i).getSoldiers()) {
                    String emoji = soldier.getTeam() == Team.RED ? "🐝" : "🥷🏻";
                    flowPane.getChildren().add(new Label(emoji));
                }
            }

            // Update the logs
            displayedLogs.clear();
            displayedLogs.addAll(gameState.turnActionLog);
        });
    }

    @FXML
    public void onBtnPreviousClicked(MouseEvent event) {
        if (currentTurnIndex > 0) {
            displayTurn(currentTurnIndex - 1);
        }
    }

    @FXML
    public void onBtnNextClicked(MouseEvent event) {
        if (currentTurnIndex < gameHistory.getTurns().size() - 1) {
            displayTurn(currentTurnIndex + 1);
        }
    }

    @FXML
    public void onBtnPlayClicked(MouseEvent event) {
        isPlaying = true;
        new Thread(() -> {
            while (isPlaying && currentTurnIndex < gameHistory.getTurns().size() - 1) {
                try {
                    Thread.sleep(1000);  // Adjust the delay as needed
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                displayTurn(currentTurnIndex + 1);
            }
        }).start();
    }

    @FXML
    public void onBtnPauseClicked(MouseEvent event) {
        isPlaying = false;
    }

    private void disableReplayButtons() {
        btnPrevious.setDisable(true);
        btnNext.setDisable(true);
        btnPlay.setDisable(true);
        btnPause.setDisable(true);
    }

    private void enableReplayButtons() {
        btnPrevious.setDisable(false);
        btnNext.setDisable(false);
        btnPlay.setDisable(false);
        btnPause.setDisable(false);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();

    }
    public void onFlowPanePressed (MouseEvent event) {
    }

    public void onFlowPaneReleased (MouseEvent event) {
    }

}
