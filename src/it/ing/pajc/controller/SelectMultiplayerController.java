package it.ing.pajc.controller;

import it.ing.pajc.Main;
import it.ing.pajc.manager.LocalGameManager;
import it.ing.pajc.manager.MultiplayerManager;
import it.ing.pajc.manager.Player;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static it.ing.pajc.controller.FXUtility.changeScene;

/**
 * Controller of the setting page.
 */
public class SelectMultiplayerController {
    private double x, y;



    public void createServer() throws IOException, ExecutionException, InterruptedException {
        Parent root = FXMLLoader.load(getClass().getResource("../GUI/CheckerBoard.fxml"));
        Scene scene = new Scene(root);
        changeScene(root, scene, "Board");
        MultiplayerManager multiplayerManager = new MultiplayerManager(Player.FIRST,scene);
        multiplayerManager.startServer(3333);
    }


    public void connect() throws IOException, ExecutionException, InterruptedException {
        Parent root = FXMLLoader.load(getClass().getResource("../GUI/CheckerBoard.fxml"));
        Scene scene = new Scene(root);
        changeScene(root, scene, "Board");
        MultiplayerManager multiplayerManager = new MultiplayerManager(Player.SECOND,scene);
        multiplayerManager.clientStartup(3333);
    }
    /**
     * Changes the screen to the home page.
     */
    public void back() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("../GUI/Home.fxml"));
            Scene scene = new Scene(root);
            changeScene(root, scene, "Select color");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Changes the current scene.
     *
     * @param root  Graphics file.
     * @param scene Graphics file scene.
     * @param title Title of the current screen.
     */
    private void changeScene(Parent root, Scene scene, String title) {
        Main.getPrimaryStage().setTitle(title);
        Main.getPrimaryStage().setScene(scene);
        root.setOnMousePressed(event -> {
            x = event.getSceneX();
            y = event.getSceneY();
        });
        root.setOnMouseDragged(event -> {
            Main.getPrimaryStage().setX(event.getScreenX() - x);
            Main.getPrimaryStage().setY(event.getScreenY() - y);
        });
    }


    /**
     * Closes the current displayed.
     */
    public void close() {
        Platform.exit();
    }
}
