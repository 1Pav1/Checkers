package it.ing.pajc.homeController;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;

import static it.ing.pajc.controller.FXUtility.changeScene;

/**
 * Controller of the homepage.
 */
public class HomeController {

    /**
     * Create the object localGameManager vs person
     *
     * @throws IOException In case the graphical file is not found.
     */
    public void singlePlayer() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../GUI/SelectColor.fxml"));
        Scene scene = new Scene(root);
        changeScene(root, scene);
    }

    /**
     * Create the object localGameManager vs pc
     *
     * @throws IOException In case the graphical file is not found.
     */
    public void singlePlayerWithAI() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../GUI/SelectColorVsAI.fxml"));
        Scene scene = new Scene(root);
        changeScene(root, scene);
    }

    /**
     * Opens multiplayer file.
     *
     * @throws IOException In case the graphical file is not found.
     */
    public void multiplayer() throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("../GUI/SelectMultiplayer.fxml"));
        Scene scene = new Scene(root);
        changeScene(root, scene);
    }


    /**
     * Closes the current page.
     */
    public void close() {
        Platform.exit();
    }
}
