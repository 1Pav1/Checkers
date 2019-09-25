package it.ing.pajc.controller;

import it.ing.pajc.Main;
import it.ing.pajc.data.board.MultiplayerItalianBoard;
import it.ing.pajc.data.pieces.PiecesColors;
import it.ing.pajc.multiplayer.MultiplayerManager;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

import java.io.*;

/**
 * Controller of multiplayer game.
 */
public class MultiplayerController {
    private static double x, y;
    private static Scene scene;

    /**
     * Loads the main menu.
     */
    public void back() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("../graphics/Home.fxml"));
            scene = new Scene(root);
            changeScene(root, scene, "Home");
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
    private static void changeScene(Parent root, Scene scene, String title) {
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
     * Closes the game.
     */
    public void close() {
        Platform.exit();
    }

    /**
     * Creates the server and waits for another player.
     */
    public void server() throws IOException {
        MultiplayerManager server = new MultiplayerManager(PiecesColors.BLACK, 5555);
        server.serverStartup();
    }

    /**
     * Creates the client and connects to the desired host.
     */
    public void client() {
        MultiplayerManager client = new MultiplayerManager(PiecesColors.WHITE, 5555);
        try {
            client.clientStartup();
            MultiplayerItalianBoard board = client.getBoard();
            drawBoard(board, PiecesColors.WHITE);
        } catch (Exception ignored) {
        }
    }

    /**
     * Draws the board.
     *
     * @param board The board used to play in multiplayer mode.
     * @param color The color selected by the first player.
     * @throws IOException In case the graphical file is not found.
     */
    public static void drawBoard(MultiplayerItalianBoard board, PiecesColors color) throws IOException {
        Parent root = FXMLLoader.load(MultiplayerController.class.getResource("../graphics/CheckerBoard.fxml"));
        scene = new Scene(root);
        GridPane checkerBoard = (GridPane) scene.lookup("#grid");
        board.placeBoard(checkerBoard, color);
        //to delete
        RotateTransition shake = new RotateTransition(Duration.millis(1000000200), checkerBoard);
        shake.setByAngle(100020);
        shake.setCycleCount(2);
        shake.setAutoReverse(true);
        shake.play();

        changeScene(root, scene, "Checkerboard");
    }
}