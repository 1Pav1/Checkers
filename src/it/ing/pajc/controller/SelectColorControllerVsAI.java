package it.ing.pajc.controller;

import it.ing.pajc.Main;
import it.ing.pajc.manager.LocalGameManager;
import it.ing.pajc.manager.LocalGameVsEngine;
import it.ing.pajc.manager.Player;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;

import java.io.IOException;

import static it.ing.pajc.controller.FXUtility.changeScene;

/**
 * Controller of the setting page.
 */
public class SelectColorControllerVsAI {
    private double x, y;

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

    public void white() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../GUI/CheckerBoard.fxml"));
        Scene scene = new Scene(root);
        changeScene(root, scene,"board");
        new LocalGameVsEngine(Player.FIRST,scene);
    }

    public void black() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../GUI/CheckerBoard.fxml"));
        Scene scene = new Scene(root);
        changeScene(root, scene,"board");
        new LocalGameVsEngine(Player.SECOND,scene);
    }

}