package it.ing.pajc.manager;

import it.ing.pajc.controller.Controller;
import it.ing.pajc.data.board.ItalianBoard;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import static it.ing.pajc.controller.FXUtility.changeScene;

//TODO GUARDAMI -------------------------------------> GESTISCI I TURNI E PENSA SE FARE UN'ALTRA CLASSE PER CONTRO ENGINE ANCHE SE NON è ITALIANO PERò SI CAPISCE DAI NO COSA DICI AH RISPONDI
//TODO ALLORA
public class LocalGameManager {
    private Player currentPlayer;
    private Player chosenPlayer;
    private ItalianBoard board;
    private boolean versusAI;
    private Scene scene;

    public LocalGameManager(Player chosenPlayer, boolean versusAI, Scene scene) throws IOException, InterruptedException {
        StringBuilder fen = new StringBuilder("memememe/emememem/memememe/eeeeeeee/eeeeeeee/eMeMeMeM/MeMeMeMe/eMeMeMeM");
        board = new ItalianBoard(fen);
        this.chosenPlayer = chosenPlayer;
        this.currentPlayer = Player.FIRST;
        this.versusAI = versusAI;
        this.scene = scene;
        Controller.placeBoard(board, scene, chosenPlayer);
        gameVsPerson();

    }

    public void gameVsPerson() throws InterruptedException {
        Controller.timeToChangePlayer = new SimpleBooleanProperty(false);
        Controller.timeToChangePlayer.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (oldValue == false) {
                    if(EndCondition.checkWin(board,currentPlayer)){
                        Parent root = null;
                        try {
                            root = FXMLLoader.load(getClass().getResource("../GUI/YouWon.fxml"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Scene scene = new Scene(root);
                        changeScene(root, scene);
                    }

                    changePlayer();
                    System.err.println(currentPlayer);
                }
            }
        });

    }

    public void gameVsAI() {
        Controller.placeBoard(board, scene, chosenPlayer);
        changePlayer();
    }

    private void changePlayer() {
        Controller.timeToChangePlayer.setValue(false);
        if (!versusAI) currentPlayer = currentPlayer == Player.FIRST ? Player.SECOND : Player.FIRST;
        else currentPlayer = currentPlayer == Player.ENGINE ? chosenPlayer : Player.ENGINE;
        changePlayerFX();
    }

    private void changePlayerFX() {
        if (versusAI)
            Controller.placeBoard(board, scene, chosenPlayer);
        else {
            board.rotate();
            Controller.placeBoard(board, scene, currentPlayer);
        }
    }

}
