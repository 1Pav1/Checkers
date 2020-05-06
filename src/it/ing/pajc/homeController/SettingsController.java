package it.ing.pajc.homeController;

import it.ing.pajc.Main;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;

/**
 * Controller of the setting page.
 */
public class SettingsController {
    private double x, y;

    /**
     * Changes the screen to the home page.
     */
    public void back() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("../GUI/Home.fxml"));
            Scene scene = new Scene(root);
            changeScene(root, scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Changes the current scene.
     *  @param root  Graphics file.
     * @param scene Graphics file scene.
     */
    private void changeScene(Parent root, Scene scene) {
        Main.getPrimaryStage().setTitle("Checkers board");
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
     *
     * @throws IOException If input is not found.
     */
    public void woodStyle() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../GUI/Home.fxml"));
        Scene scene = new Scene(root);
        scene.setUserAgentStylesheet("../graphics/woodStyle.css");

        root = FXMLLoader.load(getClass().getResource("../GUI/Multiplayer.fxml"));
        scene = new Scene(root);
        scene.setUserAgentStylesheet("../graphics/woodStyle.css");

        root = FXMLLoader.load(getClass().getResource("../GUI/CheckerBoard.fxml"));
        scene = new Scene(root);
        scene.setUserAgentStylesheet("../graphics/woodStyle.css");

        root = FXMLLoader.load(getClass().getResource("../GUI/Settings.fxml"));
        scene = new Scene(root);
        scene.setUserAgentStylesheet("../graphics/woodStyle.css");
    }

    /**
     * Changes to retro style.
     *
     * @throws IOException If no input it found.
     */
    public void retroStyle() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../GUI/Home.fxml"));
        Scene scene = new Scene(root);
        scene.setUserAgentStylesheet("../graphics/retroStyle.css");

        root = FXMLLoader.load(getClass().getResource("../GUI/Multiplayer.fxml"));
        scene = new Scene(root);
        scene.setUserAgentStylesheet("../graphics/retroStyle.css");

        root = FXMLLoader.load(getClass().getResource("../GUI/CheckerBoard.fxml"));
        scene = new Scene(root);
        scene.setUserAgentStylesheet("../graphics/retroStyle.css");

        root = FXMLLoader.load(getClass().getResource("../GUI/Settings.fxml"));
        scene = new Scene(root);
        scene.setUserAgentStylesheet("../graphics/retroStyle.css");
    }

    /**
     * Closes the current displayed.
     */
    public void close() {
        Platform.exit();
    }
}
