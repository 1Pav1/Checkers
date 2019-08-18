package it.ing.pajc.controller;

import it.ing.pajc.Main;
import it.ing.pajc.data.board.ItalianBoard;
import it.ing.pajc.data.pieces.PiecesColors;
import it.ing.pajc.multiplayer.Server;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class SettingsController implements Serializable {
    private double x,y;
    public static Stage primaryStage;

    public static void setPrimaryStage(Stage primaryStage) {
        SettingsController.primaryStage = primaryStage;
    }

    public void back() {
        try {
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
}
