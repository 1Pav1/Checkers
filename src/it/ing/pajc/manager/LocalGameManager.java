package it.ing.pajc.manager;

import it.ing.pajc.controller.Controller;
import it.ing.pajc.movements.Move;
import it.ing.pajc.data.board.ItalianBoard;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;

import static it.ing.pajc.controller.FXUtility.changeScene;

public class LocalGameManager {
    private final ItalianBoard board;
    private final Scene scene;
    private Player currentPlayer;

    /**
     * Constructor of the localGameManager
     *
     * @param chosenPlayer before the start of the game
     * @param scene        selected
     */
    public LocalGameManager(Player chosenPlayer, Scene scene) {
        StringBuilder fen = new StringBuilder("MeMeMeMe/eMeMeMeM/MeMeMeMe/eeeeeeee/eeeeeeee/emememem/memememe/emememem");
        board = new ItalianBoard(fen);
        board.rotate();
        this.currentPlayer = Player.WHITE_PLAYER;
        this.scene = scene;
        Controller.placeBoard(board, scene, chosenPlayer);
        Controller.activateTurnIndicator(scene);

        Controller.changeTurnIndicator(scene, "Whites turn", 1);

        gameVsPerson();

    }

    /**
     * Starts the game
     */
    private void gameVsPerson() {
        Controller.timeToChangePlayer = new SimpleBooleanProperty(false);
        Controller.timeToChangePlayer.addListener((observable, oldValue, newValue) -> {
            if (!oldValue) {
                changePlayer();
                System.err.println(currentPlayer);
            }
        });

    }

    /**
     * Change turn
     */
    private void changePlayer() {
        Controller.timeToChangePlayer.setValue(false);
        currentPlayer = currentPlayer == Player.WHITE_PLAYER ? Player.BLACK_PLAYER : Player.WHITE_PLAYER;
        if (currentPlayer == Player.WHITE_PLAYER)
            Controller.changeTurnIndicator(scene, "White turn", 1);
        else
            Controller.changeTurnIndicator(scene, "Black turn", 0);
        changePlayerFX();
    }

    /**
     * Change player graphically
     */
    private void changePlayerFX() {
        board.rotate();
        checkLost();
        Controller.placeBoard(board, scene, currentPlayer);
    }

    /**
     * Check if the game is lost
     */
    private void checkLost() {
        if (!Move.canSomebodyDoSomething(board, currentPlayer)) {
            Parent root = null;
            try {
                if (currentPlayer != Player.WHITE_PLAYER)
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
