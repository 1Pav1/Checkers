package it.ing.pajc.controller;

import it.ing.pajc.Main;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.io.Serializable;

public class SettingsController implements Serializable {
    private double x,y;
    public static Stage primaryStage;

    public static void setPrimaryStage(Stage primaryStage) {
        SettingsController.primaryStage = primaryStage;
    }

    public void back() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../graphics/Home.fxml"));

        Scene scene = new Scene(root);
        Main.getPrimaryStage().setScene(scene);
        Main.getPrimaryStage().setTitle("Checker main menu");

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
