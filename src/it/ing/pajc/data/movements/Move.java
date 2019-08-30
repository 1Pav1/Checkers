package it.ing.pajc.data.movements;

import it.ing.pajc.data.board.ItalianBoard;
import it.ing.pajc.data.pieces.PiecesColors;
import it.ing.pajc.data.pieces.PiecesType;
import it.ing.pajc.data.pieces.italian.ItalianKing;
import it.ing.pajc.engine.Engine;

import java.util.*;

public class Move {

/*
    public static Position [] getEatablePieces(){

    }

 */
    /**
     * Generates moves of all pieces.
     *
     * @param board  The using board
     * @param player The actual player
     * @return an arrayList of all possible moves of pieces
     */
    public static ArrayList<GenericTree<Position>> generateMoves(ItalianBoard board, PiecesColors player) {
        int posR;
        int posC;
        ArrayList<Position> piecesWhoCanMove = piecesWhoCanMove(board, player);
        ArrayList<GenericTree<Position>> allPossibleMoves = new ArrayList<>();
        for (Position position : piecesWhoCanMove) {
            posR = position.getPosR();
            posC = position.getPosC();

            if (isKing(board.getBoard()[posR][posC].getType()))
                allPossibleMoves.add(board.getKing(posR, posC).possibleMoves(board));
            else
                allPossibleMoves.add(board.getMan(posR, posC).possibleMoves(board));
        }
        return allPossibleMoves;
    }
    //USIAMO NO MOVES LEFT PER CONTROLLARE SE NESSUN PEZZO SI PUO MUOVERE

    /**
     * First check if pieces can captures and adds them in arrayList, if none can captures do the same for movements else return an empty arrayList
     *
     * @param board  The using board
     * @param player The actual player
     * @return an arrayList with all positions of pieces which can move or capture
     */
    public static ArrayList<Position> piecesWhoCanMove(ItalianBoard board, PiecesColors player) {
        ArrayList<Position> pieces = new ArrayList<>();
        for (int posR = 0; posR < 8; posR++)
            for (int posC = 0; posC < 8; posC++) {
                if (board.getBoard()[posR][posC].getPlayer() == player) {
                    if (isKing(board.getBoard()[posR][posC].getType()) && canCapture(board, posR, posC))
                        pieces.add(new Position(posR, posC));
                    else
                        pieces.add(new Position(posR, posC));
                }
            }
        if (pieces.isEmpty())
            for (int posR = 0; posR < 8; posR++)
                for (int posC = 0; posC < 8; posC++) {
                    if (board.getBoard()[posR][posC].getPlayer() == player) {
                        if (isKing(board.getBoard()[posR][posC].getType()) && canWalk(board, posR, posC))
                            pieces.add(new Position(posR, posC));
                        else
                            pieces.add(new Position(posR, posC));
                    }
                }
        return pieces;
    }

    /**
     * Gives informations about moves posibilities of a piece.
     *
     * @param board  The using board
     * @param toMove Player's color
     * @return true if a piece can't move and capture, false otherwise
     */
    public static boolean noMovesLeft(ItalianBoard board, PiecesColors toMove) {
        System.out.println("*****");
        for (int posR = 0; posR < 8; posR++)
            for (int posC = 0; posC < 8; posC++)
                if ((float) (posR + posC) / 2 == (float) (posR + posC) / 2) {
                    if (toMove == PiecesColors.WHITE &&
                            board.getBoard()[posR][posC].getPlayer() == PiecesColors.WHITE) {
                        if (canWalk(board, posR, posC)) return false;
                        else if (canCapture(board, posR, posC)) return false;
                    } else if (toMove == PiecesColors.BLACK &&
                            board.getBoard()[posR][posC].getPlayer() == PiecesColors.BLACK) {
                        if (canWalk(board, posR, posC)) return false;
                        else if (canCapture(board, posR, posC)) return false;
                    }
                } // if and for
        return true;
    }

    // examines a board position to see if the piece indicated at (x,y)
    // can make a(nother) capture
    static boolean canCapture(ItalianBoard board, int posR, int posC) {
        boolean result = false;
        if (board.getBoard()[posR][posC].getType() == PiecesType.MAN)
            result = board.getMan(posR, posC).canCapture(board, new Position(posR, posC));
        else if (board.getBoard()[posR][posC].getType() == PiecesType.KING)
            result = board.getKing(posR, posC).canCapture(board, new Position(posR, posC));
        return result;
    } // canCapture()

    // canWalk() returns true if the piece on (posR,posC) can make a
    // legal non-capturing move
    static boolean canWalk(ItalianBoard board, int posR, int posC) {
        boolean result = false;
        if (board.getBoard()[posR][posC].getType() == PiecesType.MAN)
            result = board.getMan(posR, posC).canMove(board, new Position(posR, posC));
        else if (board.getBoard()[posR][posC].getType() == PiecesType.KING)
            result = board.getKing(posR, posC).canMove(board, new Position(posR, posC));
        return result;
    } // canWalk()

    private static boolean isEmpty(ItalianBoard board, int posR, int posC) {
        if (posR > -1 && posR < 8 && posC > -1 && posC < 8)
            return board.getBoard()[posR][posC].getPlayer() == PiecesColors.EMPTY;
        return false;
    } // isEmpty()

    // checkers that i and j are between 0 and 7 inclusive
    public static boolean inRange(int posR, int posC) {
        return (posR > -1 && posR < 8 && posC > -1 && posC < 8);
    }


    /**
     * Esegue la mossa in input ed eventualmente "mangia"
     *
     * @param board
     * @param startR
     * @param startC
     * @param endR
     * @param endC
     */
    public static void eseguiMossa(ItalianBoard board, int startR, int startC, int endR, int endC) {
        if (Math.abs(startR - endR) == 2) { //sto mangiando?
            board.getBoard()[startR + (endR - startR) / 2][startC + (endC - startC) / 2].setPlayer(PiecesColors.EMPTY); //mangio In realtà mi sono accorto che cambio solo il colore
        }
        board.getBoard()[endR][endC] = board.getBoard()[startR][startC]; //sposto la pedina nella nuova posizione
        board.getBoard()[startR][startC].setPlayer(PiecesColors.EMPTY);  //libero la posizione di partenza

        // check for new king
        if (board.getBoard()[endR][endC].getPlayer() == PiecesColors.WHITE && endR == 7)
            board.getBoard()[endR][endC] = new ItalianKing(new Position(endR, endC), PiecesColors.WHITE);
        else if (board.getBoard()[endR][endC].getPlayer() == PiecesColors.BLACK && endR == 0)
            board.getBoard()[endR][endC] = new ItalianKing(new Position(endR, endC), PiecesColors.BLACK);
    }

    public static void eseguiMossa(ItalianBoard board, Vector<int[]> m) {

        for (int i = 1; i < m.size(); i++) {
            if (Math.abs(m.get(i - 1)[1] - m.get(i)[1]) == 2) { //sto mangiando?
                board.getBoard()[m.get(i - 1)[0] + (m.get(i)[0] - m.get(i - 1)[0]) / 2][m.get(i - 1)[1] + (m.get(i)[1] - m.get(i - 1)[1]) / 2].setPlayer(PiecesColors.EMPTY); //mangio
            }
            board.getBoard()[m.get(i)[0]][m.get(i)[1]] = board.getBoard()[m.get(i - 1)[0]][m.get(i - 1)[1]];
            board.getBoard()[m.get(i - 1)[0]][m.get(i - 1)[1]].setPlayer(PiecesColors.EMPTY);
            //controllo se diventa dama
            if (board.getBoard()[m.get(i)[0]][m.get(i)[1]].getPlayer() == PiecesColors.WHITE && m.get(i)[0] == 7)
                board.getBoard()[m.get(i)[0]][m.get(i)[1]] = new ItalianKing(new Position(m.get(i)[0], m.get(i)[1]), PiecesColors.WHITE);
            else if (board.getBoard()[m.get(i)[0]][m.get(i)[1]].getPlayer() == PiecesColors.BLACK && m.get(i)[0] == 0)
                board.getBoard()[m.get(i)[0]][m.get(i)[1]] = new ItalianKing(new Position(m.get(i)[0], m.get(i)[1]), PiecesColors.BLACK);
        }

    }//RIFACCIO QUELLO SOPRA
    /*
    public static void eseguiMossa(ItalianBoard board, GenericTree<Position> m) {
        List<GenericTreeNode<Position>> list = m.build(GenericTreeTraversalOrderEnum.PRE_ORDER);
        for (int i = 1; i < list.size(); i++) {
            if (Math.abs(list.get(i-1).getData().getPosR() - list.get(i).getData().getPosR()) == 2) { //sto mangiando?
                board.getBoard()[list.get(i - 1).getData().getPosR() + (m.get(i)[0] - m.get(i - 1)[0]) / 2][m.get(i - 1)[1] + (m.get(i)[1] - m.get(i - 1)[1]) / 2].setPlayer(PiecesColors.EMPTY); //mangio
            }
            board.getBoard()[m.get(i)[0]][m.get(i)[1]] = board.getBoard()[m.get(i - 1)[0]][m.get(i - 1)[1]];
            board.getBoard()[m.get(i - 1)[0]][m.get(i - 1)[1]].setPlayer(PiecesColors.EMPTY);
            //controllo se diventa dama
            if (board.getBoard()[m.get(i)[0]][m.get(i)[1]].getPlayer() == PiecesColors.WHITE && m.get(i)[0] == 7)
                board.getBoard()[m.get(i)[0]][m.get(i)[1]] = new ItalianKing(new Position(m.get(i)[0], m.get(i)[1]), PiecesColors.WHITE);
            else if (board.getBoard()[m.get(i)[0]][m.get(i)[1]].getPlayer() == PiecesColors.BLACK && m.get(i)[0] == 0)
                board.getBoard()[m.get(i)[0]][m.get(i)[1]] = new ItalianKing(new Position(m.get(i)[0], m.get(i)[1]), PiecesColors.BLACK);
        }

    }

     */


    public static boolean isKing(PiecesType pezzo) {
        return (pezzo == PiecesType.KING);
    }

} // class Move
