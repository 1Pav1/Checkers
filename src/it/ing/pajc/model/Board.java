package it.ing.pajc.model;
import it.ing.pajc.logic.*;

import java.awt.*;

public interface Board {

    int DIMENSION_ITALIAN_BOARD = 8;
    int DIMENSION_INTERNATIONAL_BOARD = 10;
    void move(Position init, Position fin);
    void printBoardConsole();

    /**
     * To be ovverridden
     * @return Pieces[][]
     */
    Pieces[][] getBoard();

    /**
     * To be ovverridden
     * @param board
     */
    void setBoard(Pieces[][] board);


}
