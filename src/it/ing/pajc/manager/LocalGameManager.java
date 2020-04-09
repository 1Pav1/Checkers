package it.ing.pajc.manager;

import it.ing.pajc.controller.Controller;
import it.ing.pajc.data.board.ItalianBoard;
import javafx.scene.Scene;

import java.io.IOException;

public class  LocalGameManager {
    private Player currentPlayer;
    private Player chosenPlayer;
    private ItalianBoard board;
    private boolean versusAI;
    private Scene scene;

    public LocalGameManager(Player chosenPlayer, boolean versusAI, Scene scene) throws IOException {
        StringBuilder fen = new StringBuilder("memememe/emememee/memememe/eeeeeeee/eeeememe/eMeMeMeM/MeMeMeMe/eMeMeMeM");
        board = new ItalianBoard(fen);
        this.chosenPlayer = chosenPlayer;
        this.currentPlayer = Player.FIRST;
        this.versusAI = versusAI;
        this.scene = scene;
        Controller.placeBoard(board, scene, chosenPlayer);
    }

    public void game(){

    }

    private void changePlayer() {
        if (!versusAI) currentPlayer = currentPlayer == Player.FIRST ? Player.SECOND : Player.FIRST;
        else currentPlayer = currentPlayer==Player.ENGINE ? chosenPlayer : Player.ENGINE;
        changePlayerFX();
    }

    private void changePlayerFX(){
        if(versusAI)
            Controller.placeBoard(board, scene, chosenPlayer);
        else{
            board.rotate();
            Controller.placeBoard(board, scene, currentPlayer);
        }
    }




}
