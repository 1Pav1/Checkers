package it.ing.pajc.graphics;

import it.ing.pajc.logic.Man;
import it.ing.pajc.logic.MovementList;
import it.ing.pajc.logic.Position;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
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

    public static void showPossibleMoves(GridPane grid, Position position){
        /*
        MovementList list = ((Man)(board[x][y])).possibleMoves(this);
        for (Position position : list.getPossibleMoves())
            boardFX[position.getPosC()][position.getPosR()].setFill(Color.rgb(0,255,0));
            */

    }
}
