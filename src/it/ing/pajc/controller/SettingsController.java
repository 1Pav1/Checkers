package it.ing.pajc.controller;

import it.ing.pajc.Main;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.*;

/**
 * Controller of the setting page.
 */
public class SettingsController {
    private double x,y;

    /**
     * Changes the screen to the home page.
     */
    public void back() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("../graphics/Home.fxml"));
            Scene scene = new Scene(root);
            changeScene(root,scene,"Checkers board");
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Changes the current scene.
     *
     * @param root  Graphics file.
     * @param scene Graphics file scene.
     * @param title Title of the current screen.
     */
    private void changeScene(Parent root, Scene scene, String title) {
        Main.getPrimaryStage().setTitle(title);
        Main.getPrimaryStage().setScene(scene);
        root.setOnMousePressed(event -> {
            x = event.getSceneX();
            y = event.getSceneY();
        });
        root.setOnMouseDragged(event -> {
            Main.getPrimaryStage().setX(event.getScreenX() - x);
            Main.getPrimaryStage().setY(event.getScreenY() - y);
        });
    }

    /**
     * Changes to wooden style.
     * @throws IOException
     */
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

    /**
     * Changes to retro style.
     * @throws IOException
     */
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

    /**
     * Closes the current displayed.
     */
    public void close(){
        Platform.exit();
    }
}
