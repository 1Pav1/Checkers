package it.ing.pajc.data.board;

import it.ing.pajc.data.movements.Position;
import it.ing.pajc.data.pieces.Pieces;

import java.io.Serializable;


public interface Board extends java.io.Serializable {

    public static final long serialversionUID = 1L;

    int DIMENSION_ITALIAN_BOARD = 8;

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
