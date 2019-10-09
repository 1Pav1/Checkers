package it.ing.pajc.data.board;

import it.ing.pajc.data.pieces.*;
import it.ing.pajc.multiplayer.MultiplayerManager;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import java.io.IOException;

public class MultiplayerItalianBoard extends ItalyBoard {
    private Pieces[][] piecesBoard;
    private MultiplayerManager multiplayerManager;

    /**
     * Disables all clickable events.
     */
    public void lock() {
        for (int x = 0; x < DIMENSION_ITALIAN_BOARD; x++) {
            for (int y = 0; y < DIMENSION_ITALIAN_BOARD; y++) {
                piecesBoard[x][y].setDisable(true);
            }
        }
    }

    /**
     * Constructor multyplayerItalianBoard.
     *
     * @param fen                String of positions.
     * @param player             Player's color.
     * @param multiplayerManager Manages the interactions between server and client.
     */
    public MultiplayerItalianBoard(Fen fen, PiecesColors player, MultiplayerManager multiplayerManager) {
        super(fen, player);
        this.multiplayerManager = multiplayerManager;
        piecesBoard = super.getPiecesBoard();

    }


    /**
     * Lock's the board and then sends it to the second player.
     *
     * @throws IOException when the connection is interrupted.
     */
    private void sendAndWait() throws IOException {
        lock();
        multiplayerManager.sendFen();
    }
}
