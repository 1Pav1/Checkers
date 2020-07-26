package it.ing.pajc.controller;

import it.ing.pajc.Main;
import it.ing.pajc.manager.MultiplayerManager;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;

/**
 * Controller of the setting page.
 */
public class SelectMultiplayerController {
    private double x, y;

    /**
     * Create the server
     *
     * @throws IOException by selectColorMultiplayer.fxml
     */
    public void createServer() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../GUI/SelectColorMultiplayer.fxml"));
        Scene scene = new Scene(root);
        changeScene(root, scene, "Select color");
    }

    /**
     * Connect the server
     *
     * @throws IOException by CheckerBoard.fxml
     */
    public void connect() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../GUI/CheckerBoard.fxml"));
        Scene scene = new Scene(root);
        changeScene(root, scene, "Board");
        MultiplayerManager multiplayerManager = new MultiplayerManager(scene);
        multiplayerManager.startClient(3333);
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
