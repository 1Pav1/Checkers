package it.ing.pajc.data.board;

import it.ing.pajc.data.movements.Position;
import it.ing.pajc.data.pieces.Empty;
import it.ing.pajc.data.pieces.Man;
import it.ing.pajc.data.pieces.Pieces;
import it.ing.pajc.data.pieces.PiecesColors;

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
        for (int posR = 0; posR < DIMENSION_INTERNATIONAL_BOARD; posR++)
            for (int posC = 0; posC < DIMENSION_INTERNATIONAL_BOARD; posC++)
                if(board[posR][posC]==null)
                    board[posR][posC]=new Empty(PiecesColors.EMPTY, new Position(posR,posC));

    }

    @Override
    public void move(Position init, Position fin) {
        board[init.getPosR()][init.getPosC()].setPosition(fin);
        board[init.getPosR()][init.getPosC()].setPlayer(PiecesColors.EMPTY);
    }

    @Override
    public void printBoardConsole(){
        for(int posR=0;posR<DIMENSION_INTERNATIONAL_BOARD;posR++) {
            for (int posC = 0; posC < DIMENSION_INTERNATIONAL_BOARD; posC++) {
                if(board[posR][posC].getPlayer()==PiecesColors.EMPTY)
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

