package it.ing.pajc.manager;

import it.ing.pajc.controller.Controller;
import it.ing.pajc.controller.Move;
import it.ing.pajc.data.board.ItalianBoard;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;

import static it.ing.pajc.controller.FXUtility.changeScene;

//TODO GUARDAMI -------------------------------------> GESTISCI I TURNI E PENSA SE FARE UN'ALTRA CLASSE PER CONTRO ENGINE ANCHE SE NON è ITALIANO PERò SI CAPISCE DAI NO COSA DICI AH RISPONDI
//TODO ALLORA
public class LocalGameManager {
    private Player currentPlayer;
    private final ItalianBoard board;
    private final Scene scene;

    public LocalGameManager(Player chosenPlayer, Scene scene) {
        StringBuilder fen = new StringBuilder("meeeeeee/eeeeeeee/eeeeeeee/eeeeeeee/eeeeeeee/eKeKeKeK/MeMeMeMe/eMeMeMeM");
        board = new ItalianBoard(fen);
        this.currentPlayer = Player.FIRST;
        this.scene = scene;
        Controller.placeBoard(board, scene, chosenPlayer);
        gameVsPerson();

    }

    public void gameVsPerson() {
        Controller.timeToChangePlayer = new SimpleBooleanProperty(false);
        Controller.timeToChangePlayer.addListener((observable, oldValue, newValue) -> {
            if (!oldValue) {
                changePlayer();
                System.err.println(currentPlayer);
            }
        });

    }

    private void changePlayer() {
        Controller.timeToChangePlayer.setValue(false);
        currentPlayer = currentPlayer == Player.FIRST ? Player.SECOND : Player.FIRST;
        changePlayerFX();
    }

    private void changePlayerFX() {
            board.rotate();
            checkLost();
            Controller.placeBoard(board, scene, currentPlayer);
    }


    private void checkLost() {
        if (!Move.canSomebodyDoSomething(board, currentPlayer)) {
            Parent root = null;
            try {
                if (currentPlayer != Player.FIRST)
                    root = FXMLLoader.load(getClass().getResource("../GUI/WhiteWins.fxml"));
                else
                    root = FXMLLoader.load(getClass().getResource("../GUI/BlackWins.fxml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            assert root != null;
            Scene scene = new Scene(root);
            changeScene(root, scene);
        }
    }

}
