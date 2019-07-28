package it.ing.pajc.model;

import it.ing.pajc.logic.Man;
import it.ing.pajc.logic.Pieces;
import it.ing.pajc.logic.PiecesColors;
import it.ing.pajc.logic.Position;

public class InternationalBoard implements Board{

    private Pieces[][] board;
    @Override
    public Pieces[][] getBoard() {
        return board;
    }

    @Override
    public void setBoard(Pieces[][] board) {
        this.board = board;
    }

    public InternationalBoard() {
        board = new Pieces[DIMENSION_INTERNATIONAL_BOARD][DIMENSION_INTERNATIONAL_BOARD];
        for (int posR = 0; posR < 4; posR++)
            for (int posC = 0; posC < DIMENSION_INTERNATIONAL_BOARD; posC++)
                if ((posC + posR) % 2 == 0)
                    board[posR][posC] = new Man(PiecesColors.BLACK,new Position(posR,posC));
        for (int posR = 6; posR < DIMENSION_INTERNATIONAL_BOARD; posR++)
            for (int posC = 0; posC < DIMENSION_INTERNATIONAL_BOARD; posC++)
                if ((posC + posR) % 2 == 0)
                    board[posR][posC] = new Man(PiecesColors.WHITE,new Position(posR,posC));
    }

    @Override
    public void move(Position init, Position fin) {
        board[fin.getPosR()][fin.getPosC()] = board[init.getPosR()][init.getPosC()];
        board[init.getPosR()][init.getPosC()] = null;
    }

    @Override
    public void printBoardConsole(){
        for(int posR=0;posR<DIMENSION_INTERNATIONAL_BOARD;posR++) {
            for (int posC = 0; posC < DIMENSION_INTERNATIONAL_BOARD; posC++) {
                if(board[posR][posC]==null)
                    System.out.print("[ ]");
                else {
                    if (board[posR][posC].getPlayer() == PiecesColors.BLACK)
                        System.out.print("[m]");
                    else if (board[posR][posC].getPlayer() == PiecesColors.WHITE)
                        System.out.print("[M]");

                }
            }
            System.out.println(" ");
        }
    }

}

