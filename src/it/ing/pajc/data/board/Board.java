package it.ing.pajc.data.board;

import it.ing.pajc.data.pieces.Pieces;

public interface Board extends java.io.Serializable {

    long serialversionUID = 1L;

    int DIMENSION_ITALIAN_BOARD = 8;

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
