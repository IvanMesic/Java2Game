package hr.meske.finalgameattempt;

import hr.meske.finalgameattempt.State.GameManager;
import hr.meske.finalgameattempt.State.GameState;
import hr.meske.finalgameattempt.networking.Client;
import hr.meske.finalgameattempt.networking.Server;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Board.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();

        GameManager gameManager = new GameManager();

        gameManager.setHelloController(fxmlLoader.<HelloController>getController());

        fxmlLoader.<HelloController>getController().setGameManager(gameManager);

        gameManager.initButtons();

    }



    private static void launchClient() {
        Application.launch(HelloApplication.class);
    }
}