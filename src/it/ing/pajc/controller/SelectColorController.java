package it.ing.pajc.controller;

import it.ing.pajc.Main;
import it.ing.pajc.data.pieces.PiecesColors;
import it.ing.pajc.singleplayer.SinglePlayerManager;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;

import java.io.IOException;

/**
 * Controller of the setting page.
 */
public class SelectColorController {
    private double x, y;



    private static boolean secondPlayerIsAI;

    public static boolean isSecondPlayerIsAI() {
        return secondPlayerIsAI;
    }

    public static void setSecondPlayerIsAI(boolean secondPlayerIsAI) {
        SelectColorController.secondPlayerIsAI = secondPlayerIsAI;
    }
    /**
     * Changes the screen to the home page.
     */
    public void back() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("../graphics/Home.fxml"));
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
        PiecesColors color = PiecesColors.WHITE;
        SinglePlayerManager singlePlayerManager = new SinglePlayerManager(color,color,secondPlayerIsAI);
        sendInfoToCheckerboard(singlePlayerManager);
    }

    public void black() throws IOException {
        PiecesColors color = PiecesColors.BLACK;
        SinglePlayerManager singlePlayerManager = new SinglePlayerManager(color,color,secondPlayerIsAI);
        sendInfoToCheckerboard(singlePlayerManager);
    }

    public void sendInfoToCheckerboard(SinglePlayerManager singlePlayerManager) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../graphics/CheckerBoard.fxml"));
        Scene scene = new Scene(root);
        GridPane checkerBoard = (GridPane) scene.lookup("#grid");
        TextArea textArea = (TextArea) scene.lookup("#textArea");
        CheckerBoardController.setGridPane(checkerBoard);
        CheckerBoardController.setTextArea(textArea);
        CheckerBoardController.setSinglePlayerManager(singlePlayerManager);
        CheckerBoardController.start();

        changeScene(root, scene, "CheckerBoard");
    }
}
