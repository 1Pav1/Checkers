package it.ing.pajc.data.board;

import it.ing.pajc.data.pieces.Pieces;

public interface Board {
    int DIMENSION_ITALIAN_BOARD = 8;

    /**
     * Print the board on the console to be overridden.
     */
    void printBoardConsole();

    /**
     * Getter to be overridden.
     * @return Pieces[][]
     */
    Pieces[][] getBoard();
}
