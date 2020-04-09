package it.ing.pajc.homeController;

import it.ing.pajc.Main;
import it.ing.pajc.manager.LocalGameManager;
import it.ing.pajc.manager.Player;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.atomic.AtomicReference;

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
    public void singlePlayer() throws IOException {
        //crea oggetto localGameManager @param contro persona
        Parent root = FXMLLoader.load(getClass().getResource("../GUI/CheckerBoard.fxml"));
        Scene scene = new Scene(root);
        changeScene(root, scene);
        LocalGameManager localGameManager = new LocalGameManager(Player.FIRST,false,scene);
    }



    public void singlePlayerWithAI() throws IOException {
        //crea oggetto LocalGameManager @param contro pc
        Parent root = FXMLLoader.load(getClass().getResource("../GUI/CheckerBoard.fxml"));
        Scene scene = new Scene(root);
        changeScene(root, scene);
        LocalGameManager localGameManager = new LocalGameManager(Player.FIRST,true,scene);
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

    private static void changeScene(Parent root, Scene scene) {
        AtomicReference<Double> x = new AtomicReference<>((double) 0);
        AtomicReference<Double> y = new AtomicReference<>((double) 0);
        Main.getPrimaryStage().setTitle("Home");
        Main.getPrimaryStage().setScene(scene);
        root.setOnMousePressed(event -> {
            x.set(event.getSceneX());
            y.set(event.getSceneY());
        });
        root.setOnMouseDragged(event -> {
            Main.getPrimaryStage().setX(event.getScreenX() - x.get());
            Main.getPrimaryStage().setY(event.getScreenY() - y.get());
        });
    }
    /**
     * Opens multiplayer file.
     *
     * @throws IOException In case the graphical file is not found.
     */
    public void multiplayer() throws IOException {
        //crea oggetoo MultiplayerManger
    }


    /**
     * Closes the current page.
     */
    public void close() {
        Platform.exit();
    }
}
