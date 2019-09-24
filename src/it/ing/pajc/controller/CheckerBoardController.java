package it.ing.pajc.controller;

import it.ing.pajc.Main;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
/**
 * Controller of the board displaying page.
 */
public class CheckerBoardController{
    private double x, y;

    /**
     * Closes the game.
     */
    public void close() {
        Platform.exit();
    }

    /**
     * Goes back to home page.
     */
    public void back() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("../graphics/Home.fxml"));
            Scene scene = new Scene(root);
            changeScene(root, scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Changes the current scene.
     *  @param root  Graphics file.
     * @param scene Graphics file scene.
     */
    private void changeScene(Parent root, Scene scene) {
        Main.getPrimaryStage().setTitle("Home");
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
}