package it.ing.pajc.controller;

import it.ing.pajc.Main;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.util.concurrent.atomic.AtomicReference;

public class FXUtility {
    public static void changeScene(Parent root, Scene scene) {
        AtomicReference<Double> x = new AtomicReference<>((double) 0);
        AtomicReference<Double> y = new AtomicReference<>((double) 0);
        Main.getPrimaryStage().setTitle("Home");
        Main.getPrimaryStage().setScene(scene);
        root.setOnMousePressed(event -> {
            x.set(event.getSceneX());
            y.set(event.getSceneY());
        });
        root.setOnMouseDragged(event -> {
            Main.getPrimaryStage().setX(event.getScreenX() - x.get());
            Main.getPrimaryStage().setY(event.getScreenY() - y.get());
        });
    }

}
