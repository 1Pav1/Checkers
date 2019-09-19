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
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;

public class MultiplayerController {
    private static double x,y;
    public static Stage primaryStage;
    public static final int PORT = 5555;
    private static Scene scene;
    private Thread thread;

    public void back() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("../graphics/Home.fxml"));
            scene = new Scene(root);
            Main.getPrimaryStage().setScene(scene);
            Main.getPrimaryStage().setTitle("Checker main menu");
            //we gonna drag the frame
            root.setOnMousePressed(event -> {
                x = event.getSceneX();
                y = event.getSceneY();
            });
            root.setOnMouseDragged(event -> {
                Main.getPrimaryStage().setX(event.getScreenX() - x);
                Main.getPrimaryStage().setY(event.getScreenY() - y);
            });
        }catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void close(){
        Platform.exit();
    }

    public void server(){

        MultiplayerManager server = new MultiplayerManager(PiecesColors.BLACK,5555);

        /*thread = new Thread(){
            public void run(){*/
        try {
            server.serverStartup();
            MultiplayerItalianBoard board = server.getBoard();
            StackPane layout = new StackPane();
            drawBoard(board,PiecesColors.BLACK);

            //board.printBoardConsole();

        } catch (IOException e) {
        }/*
            }
        };
        thread.setName("Checkers server");
        thread.start();
*/

    }

    public void client(){
        MultiplayerManager client = new MultiplayerManager(PiecesColors.WHITE, 5555);

        /*thread = new Thread() {
            public void run() {
*/
        try {
            client.clientStartup();
            MultiplayerItalianBoard board = client.getBoard();
            StackPane layout = new StackPane();
            drawBoard(board,PiecesColors.WHITE);
        } catch (Exception e) {
        }/*
            }
        };
        thread.setName("Checkers client");
        thread.start();*/
    }

    public static void drawBoard(MultiplayerItalianBoard board,PiecesColors color) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        Parent root = (Parent) loader.load(MultiplayerController.class.getResource("../graphics/CheckerBoard.fxml"));
        //Parent root = FXMLLoader.load(getClass().getResource(""));
        CheckerBoardController cbc = loader.getController();

        scene = new Scene(root);
        GridPane checkerBoard = (GridPane) scene.lookup("#grid");
        board.placeBoard(checkerBoard, color);
        RotateTransition shake = new RotateTransition(Duration.millis(200), checkerBoard);
        shake.setByAngle(20);
        shake.setCycleCount(2);
        shake.setAutoReverse(true);
        shake.play();


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
}