package it.ing.pajc.controller;

import it.ing.pajc.Main;
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
import java.io.Serializable;

public class HomeController implements Serializable {
    private double x, y;


    public void  singlePlayer() throws IOException {
        PiecesColors color = PiecesColors.BLACK;
        ItalianBoard board = new ItalianBoard("memememe/emememem/memememe/eeeeeeee/eeeeeeee/eMeMeMeM/MeMeMeMe/eMeMeMeM",color);
        //board.printBoardConsole();

        StackPane layout = new StackPane();


        FXMLLoader loader = new FXMLLoader();
        Parent root = (Parent) loader.load(getClass().getResource("../graphics/CheckerBoard.fxml"));
        //Parent root = FXMLLoader.load(getClass().getResource(""));
        CheckerBoardController cbc = loader.getController();

        Scene scene = new Scene(root);
        GridPane checkerBoard = (GridPane) scene.lookup("#grid");
        board.placeboard(checkerBoard, PiecesColors.WHITE);

        Main.getPrimaryStage().setTitle("CheckerBoard");
        Main.getPrimaryStage().setScene(scene);
        //we gonna drag the frame
        root.setOnMousePressed(event -> {
            x = event.getSceneX();
            y = event.getSceneY();
        });

        root.setOnMouseDragged(event -> {
            Main.getPrimaryStage().setX(event.getScreenX() - x);
            Main.getPrimaryStage().setY(event.getScreenY() - y);
        });

    }

    public void settings() throws IOException {



        Parent root = FXMLLoader.load(getClass().getResource("../graphics/YouWon.fxml"));
        Scene scene = new Scene(root);

        Main.getPrimaryStage().setTitle("Settings");
        Main.getPrimaryStage().setScene(scene);
        //we gonna drag the frame
        root.setOnMousePressed(event -> {
            x = event.getSceneX();
            y = event.getSceneY();
        });

        root.setOnMouseDragged(event -> {
            Main.getPrimaryStage().setX(event.getScreenX() - x);
            Main.getPrimaryStage().setY(event.getScreenY() - y);
        });
    }

    public void multiplayer() throws IOException {



        Parent root = FXMLLoader.load(getClass().getResource("../graphics/Multiplayer.fxml"));
        Scene scene = new Scene(root);

        Main.getPrimaryStage().setTitle("Multiplayer");
        Main.getPrimaryStage().setScene(scene);
        //we gonna drag the frame
        root.setOnMousePressed(event -> {
            x = event.getSceneX();
            y = event.getSceneY();
        });

        root.setOnMouseDragged(event -> {
            Main.getPrimaryStage().setX(event.getScreenX() - x);
            Main.getPrimaryStage().setY(event.getScreenY() - y);
        });
    }

    public void close(){
        Platform.exit();
    }
}
