package it.ing.pajc;

import javafx.application.Application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.io.File;


public class Main extends Application {
    private static Stage primaryStage;
    private double x, y;

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Getter
     *
     * @return the private stage
     */
    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * Play the music
     *
     * @param musicLocation the path
     */
    private static void playMusic(String musicLocation) {
        try {
            File musicPath = new File(musicLocation);

            if (musicPath.exists()) {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
                Clip clip = AudioSystem.getClip();
                clip.open(audioInput);
                clip.start();
                clip.loop(Clip.LOOP_CONTINUOUSLY);
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                double gain = 1.0;
                float dB = (float) (Math.log(gain) / Math.log(10.0) * 20.0);
                gainControl.setValue(dB);
            } else {
                System.out.println("Can't find file");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Start of the game
     * @param primaryStage taken in consideration
     * @throws Exception Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        Main.primaryStage = primaryStage;

        Parent root = FXMLLoader.load(getClass().getResource("GUI/Home.fxml"));
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

        String filepath = "src/it/ing/pajc/Audio/Background.wav";

        playMusic(filepath);
    }
}