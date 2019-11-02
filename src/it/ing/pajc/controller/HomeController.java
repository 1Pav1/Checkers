package it.ing.pajc.controller;

import it.ing.pajc.Main;
import it.ing.pajc.data.pieces.PiecesColors;
import it.ing.pajc.singleplayer.SinglePlayerManager;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.io.Serializable;

/**
 * Controller of the homepage.
 */
public class HomeController implements Serializable {
    private double x, y;

    /**
     * Creates the board and displays it.
     *
     * @throws IOException In case the graphical file is not found.
     */
    public void singlePlayer() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../graphics/SelectColor.fxml"));
        Scene scene = new Scene(root);
        SelectColorController.setSecondPlayerIsAI(false);
        changeScene(root, scene, "Select color ");
    }



    public void singlePlayerWithAI() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../graphics/SelectColor.fxml"));
        Scene scene = new Scene(root);
        SelectColorController.setSecondPlayerIsAI(true);
        changeScene(root, scene, "Select color ");
    }


    /**
     * Opens settings file.
     *
     * @throws IOException In case the graphical file is not found.
     */
    public void settings() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../graphics/Settings.fxml"));
        Scene scene = new Scene(root);
        changeScene(root, scene, "Settings");
    }

    /**
     * Opens multiplayer file.
     *
     * @throws IOException In case the graphical file is not found.
     */
    public void multiplayer() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../graphics/Multiplayer.fxml"));
        Scene scene = new Scene(root);
        changeScene(root, scene, "Multiplayer");
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
     * Closes the current page.
     */
    public void close() {
        Platform.exit();
    }
}
