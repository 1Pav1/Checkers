package it.ing.pajc.controller;

import it.ing.pajc.Main;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.Serializable;

public class CheckerBoardController implements Serializable {
    private double x,y;

    public void close(){
        Platform.exit();
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
            System.out.println("ciao");
            e.printStackTrace();
        }


    }

    public static void method(int i, int j , Circle circle){

    }


}
