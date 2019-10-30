package it.ing.pajc;
import javafx.application.Application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;


import javafx.scene.media.AudioClip;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.net.URL;


public class Main extends Application {
    private double x, y;
    private static Stage primaryStage;

    public static Stage getPrimaryStage() {
        return primaryStage;
    }




    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;

        Parent root = FXMLLoader.load(getClass().getResource("graphics/Home.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Checker main menu");
        primaryStage.initStyle(StageStyle.UNDECORATED);
        //we gonna drag the frame
        root.setOnMousePressed(event -> {
            x = event.getSceneX();
            y = event.getSceneY();
        });

        root.setOnMouseDragged(event -> {
            primaryStage.setX(event.getScreenX() - x);
            primaryStage.setY(event.getScreenY() - y);
        });
        primaryStage.show();
        /*AudioClip note = new AudioClip(this.getClass().getResource("Kabza.mp3").toString());
        note.play();



        String source = new File("Kabza.mp3").toURI().toString();
        Media media = null;
        media = new Media(source);
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();

         */
    }


    public static void main(String[] args) {
        launch(args);
    }
}