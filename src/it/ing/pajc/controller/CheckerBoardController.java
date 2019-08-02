package it.ing.pajc.controller;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.IOException;

public class CheckerBoardController {
    private double x,y;
    public static Stage primaryStage;


    public static void setPrimaryStage(Stage primaryStage) {
        HomeController.primaryStage = primaryStage;
    }

    public void close(){
        Platform.exit();
    }

    public void back() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../graphics/Home.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Checker main menu");
        //we gonna drag the frame
        root.setOnMousePressed(event -> {
            x = event.getSceneX();
            y = event.getSceneY();
        });

    }

    public static void method(int i, int j , Circle circle){

    }


}
