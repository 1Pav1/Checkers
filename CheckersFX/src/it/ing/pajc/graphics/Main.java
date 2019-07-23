package it.ing.pajc.graphics;

import it.ing.pajc.logic.King;
import it.ing.pajc.logic.Man;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import it.ing.pajc.model.*;
import it.ing.pajc.model.ItalianBoard.*;
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("Home.fxml"));
        primaryStage.setTitle("CheckersFX");
        primaryStage.setScene(new Scene(root, 500, 500));
        primaryStage.show();
        Board board = new ItalianBoard();
        board.printBoardConsole();
        ((Man) board.getBoard()[2][2]).possibleMoves(((ItalianBoard) board));
    }


    public static void main(String[] args) {
        launch(args);
    }
}
