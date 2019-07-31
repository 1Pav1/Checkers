package it.ing.pajc.data.board;

import it.ing.pajc.data.coordinates.Position;
import it.ing.pajc.data.pieces.Pieces;

public interface Board {

    int DIMENSION_ITALIAN_BOARD = 8;
    int DIMENSION_INTERNATIONAL_BOARD = 10;

    /**
     * Move a piece from a position to another
     * @param init initial position
     * @param fin final position
     */
    void move(Position init, Position fin);

    void printBoardConsole();

    /**
     * To be ovverridden.
     * @return Pieces[][]
     */
    Pieces[][] getBoard();

    /**
     * To be ovverridden.
     * @param board Of pieces
     */
    void setBoard(Pieces[][] board);
}
