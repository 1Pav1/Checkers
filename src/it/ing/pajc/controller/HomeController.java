package it.ing.pajc.controller;

import it.ing.pajc.data.pieces.Man;
import it.ing.pajc.data.board.Board;
import it.ing.pajc.data.board.ItalianBoard;
import it.ing.pajc.data.pieces.PiecesColors;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class HomeController {
    private double x, y;

    public static Stage primaryStage;


    public static void setPrimaryStage(Stage primaryStage) {
        HomeController.primaryStage = primaryStage;
    }

    public void  singlePlayer() throws IOException {

        Board board = new ItalianBoard();
        board.printBoardConsole();
        ((Man) board.getBoard()[2][2]).possibleMoves(((ItalianBoard) board));

        StackPane layout = new StackPane();



        Parent root = FXMLLoader.load(getClass().getResource("../graphics/CheckerBoard.fxml"));
        Scene scene = new Scene(root);
        GridPane checkerBoard = (GridPane) scene.lookup("#grid");
        ((ItalianBoard)board).placeboard(checkerBoard, PiecesColors.WHITE);

        primaryStage.setTitle("CheckerBoard");
        primaryStage.setScene(scene);
        //we gonna drag the frame
        root.setOnMousePressed(event -> {
            x = event.getSceneX();
            y = event.getSceneY();
        });

        root.setOnMouseDragged(event -> {
            primaryStage.setX(event.getScreenX() - x);
            primaryStage.setY(event.getScreenY() - y);
        });

    }

    public void settings() throws IOException {



        Parent root = FXMLLoader.load(getClass().getResource("../graphics/Settings.fxml"));
        Scene scene = new Scene(root);

        primaryStage.setTitle("Settings");
        primaryStage.setScene(scene);
        //we gonna drag the frame
        root.setOnMousePressed(event -> {
            x = event.getSceneX();
            y = event.getSceneY();
        });

        root.setOnMouseDragged(event -> {
            primaryStage.setX(event.getScreenX() - x);
            primaryStage.setY(event.getScreenY() - y);
        });
    }

    public void close(){
        Platform.exit();
    }
}
