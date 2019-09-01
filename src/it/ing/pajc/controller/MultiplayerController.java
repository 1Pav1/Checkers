package it.ing.pajc.controller;

import it.ing.pajc.Main;
import it.ing.pajc.data.board.Board;
import it.ing.pajc.data.board.ItalianBoard;
import it.ing.pajc.data.pieces.PiecesColors;
import it.ing.pajc.multiplayer.MultiplayerManager;
import it.ing.pajc.multiplayer.Server;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class MultiplayerController {
    private double x,y;
    public static Stage primaryStage;
    public static final int PORT = 5555;
    private Thread thread;
    public void back() {
        try {
            thread.stop();
            Parent root = FXMLLoader.load(getClass().getResource("../graphics/Home.fxml"));
            Scene scene = new Scene(root);
            Main.getPrimaryStage().setScene(scene);
            Main.getPrimaryStage().setTitle("Checker main menu");
            //we gonna drag the frame
            root.setOnMousePressed(event -> {
                x = event.getSceneX();
                y = event.getSceneY();
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
            ItalianBoard board = server.getBoard();
            StackPane layout = new StackPane();


            //board.printBoardConsole();



            FXMLLoader loader = new FXMLLoader();
            Parent root = (Parent) loader.load(getClass().getResource("../graphics/CheckerBoard.fxml"));
            //Parent root = FXMLLoader.load(getClass().getResource(""));
            CheckerBoardController cbc = loader.getController();

            Scene scene = new Scene(root);
            GridPane checkerBoard = (GridPane) scene.lookup("#grid");
            ((ItalianBoard)board).placeboard(checkerBoard, PiecesColors.BLACK);

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
            ItalianBoard board = client.getBoard();
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
        } catch (Exception e) {
        }/*
            }
        };
        thread.setName("Checkers client");
        thread.start();*/
    }
}