package it.ing.pajc.homeController;

import it.ing.pajc.Main;
import it.ing.pajc.controller.FXUtility;
import it.ing.pajc.manager.LocalGameManager;
import it.ing.pajc.manager.MultiplayerManager;
import it.ing.pajc.manager.Player;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;

import static it.ing.pajc.controller.FXUtility.changeScene;

/**
 * Controller of the homepage.
 */
public class HomeController {
    private double x, y;

    /**
     * Creates the board and displays it.
     *
     * @throws IOException In case the graphical file is not found.
     */
    public void singlePlayer() throws IOException, InterruptedException {
        //crea oggetto localGameManager @param contro persona
        Parent root = FXMLLoader.load(getClass().getResource("../GUI/CheckerBoard.fxml"));
        Scene scene = new Scene(root);
        changeScene(root, scene);
        LocalGameManager localGameManager = new LocalGameManager(Player.FIRST,false,scene);
    }



    public void singlePlayerWithAI() throws IOException, InterruptedException {
        //crea oggetto LocalGameManager @param contro pc
        Parent root = FXMLLoader.load(getClass().getResource("../GUI/CheckerBoard.fxml"));
        Scene scene = new Scene(root);
        changeScene(root, scene);
        LocalGameManager localGameManager = new LocalGameManager(Player.FIRST,false,scene);
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
        MultiplayerManager multiplayerManager = new MultiplayerManager(Player.FIRST,11000,scene);
    }


    /**
     * Closes the current page.
     */
    public void close() {
        Platform.exit();
    }
}
