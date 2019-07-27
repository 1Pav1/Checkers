package it.ing.pajc.graphics;

import it.ing.pajc.logic.Man;
import it.ing.pajc.model.Board;
import it.ing.pajc.model.ItalianBoard;
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



        Parent root = FXMLLoader.load(getClass().getResource("CheckerBoard.fxml"));
        Scene scene = new Scene(root);
        GridPane checkerBoard = (GridPane) scene.lookup("#grid");
        ((ItalianBoard)board).placeboard(checkerBoard);

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

    public void close(){
        Platform.exit();
    }
}
