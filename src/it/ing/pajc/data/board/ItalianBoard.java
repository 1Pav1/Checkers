package it.ing.pajc.data.board;

import it.ing.pajc.data.pieces.*;

import java.util.Arrays;

/**
 * Creates and manage ItalianBoard
 */
public class ItalianBoard implements Board {
    private Square[][] piecesBoard;


    public ItalianBoard(StringBuilder fen) {
        piecesBoard = Fen.fenToMultidimensionalArray(fen);
        //Per copiare un array multidimensionale fatto per bene
        //for(int i=0;i<Board.DIMENSION_ITALIAN_BOARD;i++)
        //System.arraycopy([i],0,piecesBoard[i],0,Board.DIMENSION_ITALIAN_BOARD);
        printBoardConsole();
    }

    /**
     * Print the board, only console.
     */
    @Override
    public void printBoardConsole() {
        for (int posR = 0; posR < DIMENSION_ITALIAN_BOARD; posR++) {
            for (int posC = 0; posC < DIMENSION_ITALIAN_BOARD; posC++) {
                if (piecesBoard[posR][posC].getPlace() == PlaceType.EMPTY)
                    System.out.print("[ ]");
                else {
                    if (piecesBoard[posR][posC].getPlace() == PlaceType.BLACK) {
                        if (piecesBoard[posR][posC].getPiece() == PieceType.MAN)
                            System.out.print("[m]");
                        else
                            System.out.print("[k]");
                    } else if (piecesBoard[posR][posC].getPlace() == PlaceType.WHITE) {
                        if (piecesBoard[posR][posC].getPiece() == PieceType.MAN)
                            System.out.print("[M]");
                        else
                            System.out.print("[K]");
                    }
                }
            }
            System.out.println(" ");
        }
        System.out.println();
    }

    public void rotate(){
        StringBuilder fen = Fen.multidimensionalArrayToFen(piecesBoard);
        fen = fen.reverse();
        piecesBoard = Fen.fenToMultidimensionalArray(fen);
    }


    public Square [][] getBoard() {
        return piecesBoard;
    }

    public StringBuilder getFen(){
        return Fen.multidimensionalArrayToFen(piecesBoard);
    }

    @Override
    public String toString() {
        return "ItalianBoard{" +
                "piecesBoard=" + Arrays.toString(piecesBoard) +
                '}';
    }
}

