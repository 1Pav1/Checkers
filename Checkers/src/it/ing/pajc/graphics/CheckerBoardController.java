package it.ing.pajc.graphics;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
        Parent root = FXMLLoader.load(getClass().getResource("Home.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Checker main menu");
        //we gonna drag the frame
        root.setOnMousePressed(event -> {
            x = event.getSceneX();
            y = event.getSceneY();
        });



    }
}
