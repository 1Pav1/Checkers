package it.ing.pajc.homeController;

import it.ing.pajc.manager.LocalGameVsEngine;
import it.ing.pajc.manager.MultiplayerManager;
import it.ing.pajc.manager.Player;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static it.ing.pajc.controller.FXUtility.changeScene;

/**
 * Controller of the homepage.
 */
public class HomeController {

    /**
     * Creates the board and displays it.
     *
     * @throws IOException In case the graphical file is not found.
     */
    public void singlePlayer() throws IOException {
        //crea oggetto localGameManager @param contro persona
        Parent root = FXMLLoader.load(getClass().getResource("../GUI/SelectColor.fxml"));
        Scene scene = new Scene(root);
        changeScene(root, scene);
    }



    public void singlePlayerWithAI() throws IOException {
        //crea oggetto LocalGameManager @param contro pc
        Parent root = FXMLLoader.load(getClass().getResource("../GUI/CheckerBoard.fxml"));
        Scene scene = new Scene(root);
        changeScene(root, scene);
        new LocalGameVsEngine(Player.FIRST,scene);
    }


    /**
     * Opens settings file.
     *
     * @throws IOException In case the graphical file is not found.
     */
    public void settings() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../GUI/Settings.fxml"));
        Scene scene = new Scene(root);
        changeScene(root, scene);
    }


    /**
     * Opens multiplayer file.
     *
     * @throws IOException In case the graphical file is not found.
     */
    public void multiplayer() throws IOException, ExecutionException, InterruptedException {
        //crea oggetoo MultiplayerManger
        Parent root = FXMLLoader.load(getClass().getResource("../GUI/CheckerBoard.fxml"));
        Scene scene = new Scene(root);
        changeScene(root, scene);
        new MultiplayerManager(Player.FIRST,11000,scene);
    }


    /**
     * Closes the current page.
     */
    public void close() {
        Platform.exit();
    }
}
