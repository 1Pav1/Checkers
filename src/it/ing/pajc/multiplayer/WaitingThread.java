package it.ing.pajc.multiplayer;

import it.ing.pajc.controller.MultiplayerController;
import it.ing.pajc.data.board.Board;
import it.ing.pajc.data.board.MultiplayerItalianBoard;
import it.ing.pajc.data.pieces.PiecesColors;

import java.io.BufferedReader;
import java.io.IOException;

public class WaitingThread implements Runnable {
    private MultiplayerItalianBoard board;
    private BufferedReader in;
    private PiecesColors color;
    private MultiplayerManager multiplayerManager;
    public WaitingThread(MultiplayerItalianBoard board, BufferedReader in, PiecesColors color,MultiplayerManager multiplayerManager) {
        this.board = board;
        this.in = in;
        this.color = color;
        this.multiplayerManager = multiplayerManager;
    }

    private String readFen(){
        String fen = null;
        try {
            fen = in.readLine();
        } catch (Exception e) {
            readFen();
        }
        return fen;
    }

    @Override
    public void run() {
        String fen = readFen();
        board = new MultiplayerItalianBoard(fen,color,multiplayerManager);
        try {
            MultiplayerController.drawBoard(board,color);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
