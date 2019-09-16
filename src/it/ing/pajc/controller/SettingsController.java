package it.ing.pajc.controller;

import it.ing.pajc.Main;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.*;

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
            root.setOnMouseDragged(event -> {
                Main.getPrimaryStage().setX(event.getScreenX() - x);
                Main.getPrimaryStage().setY(event.getScreenY() - y);
            });
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void woodStyle() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../graphics/Home.fxml"));
        Scene scene = new Scene(root);
        AnchorPane anchorPane = (AnchorPane) scene.lookup("");
        scene.setUserAgentStylesheet("../graphics/woodStyle.css");

        root = FXMLLoader.load(getClass().getResource("../graphics/Multiplayer.fxml"));
        scene = new Scene(root);
        scene.setUserAgentStylesheet("../graphics/woodStyle.css");

        root = FXMLLoader.load(getClass().getResource("../graphics/CheckerBoard.fxml"));
        scene = new Scene(root);
        scene.setUserAgentStylesheet("../graphics/woodStyle.css");

        root = FXMLLoader.load(getClass().getResource("../graphics/Settings.fxml"));
        scene = new Scene(root);
        scene.setUserAgentStylesheet("../graphics/woodStyle.css");
    }

    public void retroStyle() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../graphics/Home.fxml"));
        Scene scene = new Scene(root);
        scene.setUserAgentStylesheet("../graphics/retroStyle.css");

        root = FXMLLoader.load(getClass().getResource("../graphics/Multiplayer.fxml"));
        scene = new Scene(root);
        scene.setUserAgentStylesheet("../graphics/retroStyle.css");

        root = FXMLLoader.load(getClass().getResource("../graphics/CheckerBoard.fxml"));
        scene = new Scene(root);
        scene.setUserAgentStylesheet("../graphics/retroStyle.css");

        root = FXMLLoader.load(getClass().getResource("../graphics/Settings.fxml"));
        scene = new Scene(root);
        scene.setUserAgentStylesheet("../graphics/retroStyle.css");
    }

    public void close(){
        Platform.exit();
    }
}
