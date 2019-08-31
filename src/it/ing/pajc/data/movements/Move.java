package it.ing.pajc.data.movements;

import it.ing.pajc.data.board.ItalianBoard;
import it.ing.pajc.data.pieces.Empty;
import it.ing.pajc.data.pieces.Pieces;
import it.ing.pajc.data.pieces.PiecesColors;
import it.ing.pajc.data.pieces.PiecesType;
import it.ing.pajc.data.pieces.italian.ItalianKing;
import it.ing.pajc.data.pieces.italian.ItalianMan;
import javafx.geometry.Pos;

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
    public static ArrayList<GenericTree<Position>> generateCaptures(ItalianBoard board, PiecesColors player) {
        int posR;
        int posC;
        ArrayList<Position> piecesWhoCanMove = piecesWhoCanMove(board, player);
        ArrayList<GenericTree<Position>> allPossibleCaptures = new ArrayList<>();
        for (Position position : piecesWhoCanMove) {
            posR = position.getPosR();
            posC = position.getPosC();

            if (isKing(board.getBoard()[posR][posC].getType()))
                allPossibleCaptures.add(board.getKing(posR, posC).possibleCaptures(board));
            else
                allPossibleCaptures.add(board.getMan(posR, posC).possibleCaptures(board));
        }
        return allPossibleCaptures;
    }
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
     */
    public static void executeMove(ItalianBoard board, Position init, Position fin, List<GenericTreeNode> listPossibleCaptures) {
        for (int p = 1; p < listPossibleCaptures.size(); p++) {
            Position positionPossibleCaptures = (Position) listPossibleCaptures.get(p).getData();
            System.out.println("Captured " + positionPossibleCaptures.getPosR() + " " + positionPossibleCaptures.getPosC());
            delete(new Position(positionPossibleCaptures.getPosR(), positionPossibleCaptures.getPosC()), board);
        }
        if (board.getBoard()[init.getPosR()][init.getPosC()].getPlayer() == PiecesColors.WHITE)
            if (board.getBoard()[init.getPosR()][init.getPosC()].getType() == PiecesType.MAN)
                board.getBoard()[fin.getPosR()][fin.getPosC()] = new ItalianMan(fin, PiecesColors.WHITE);
            else
                board.getBoard()[fin.getPosR()][fin.getPosC()] = new ItalianKing(fin, PiecesColors.WHITE);

        else if (board.getBoard()[init.getPosR()][init.getPosC()].getPlayer() == PiecesColors.BLACK)
            if (board.getBoard()[init.getPosR()][init.getPosC()].getType() == PiecesType.MAN)
                board.getBoard()[fin.getPosR()][fin.getPosC()] = new ItalianMan(fin, PiecesColors.BLACK);
            else
                board.getBoard()[fin.getPosR()][fin.getPosC()] = new ItalianKing(fin, PiecesColors.BLACK);

        board.getBoard()[init.getPosR()][init.getPosC()] = new Empty(init);

        // check for new king
        if (board.getBoard()[fin.getPosR()][fin.getPosC()].getPlayer() == PiecesColors.WHITE && fin.getPosR() == 7)
            board.getBoard()[fin.getPosR()][fin.getPosC()] = new ItalianKing(new Position(fin.getPosR(), fin.getPosC()), PiecesColors.WHITE);
        else if (board.getBoard()[fin.getPosR()][fin.getPosC()].getPlayer() == PiecesColors.BLACK && fin.getPosR() == 0)
            board.getBoard()[fin.getPosR()][fin.getPosC()] = new ItalianKing(new Position(fin.getPosR(), fin.getPosC()), PiecesColors.BLACK);

    }

    public static void executeMove(ItalianBoard board, GenericTreeNode<Position> listPossibleMoves, GenericTreeNode<Position> listPossibleCaptures) {
        if (!canCapture(board, listPossibleMoves.getData().getPosR(), listPossibleMoves.getData().getPosC())) {
            if (!isKing(board.getBoard()[listPossibleMoves.getData().getPosR()][listPossibleMoves.getData().getPosC()].getType())) {
                board.getBoard()[listPossibleMoves.getChildAt(0).getData().getPosR()][listPossibleMoves.getChildAt(0).getData().getPosC()] = new ItalianMan(new Position(listPossibleMoves.getChildAt(0).getData().getPosR(), listPossibleMoves.getChildAt(0).getData().getPosC()), board.getBoard()[listPossibleMoves.getData().getPosR()][listPossibleMoves.getData().getPosC()].getPlayer());
                delete(board.getBoard()[listPossibleMoves.getData().getPosR()][listPossibleMoves.getData().getPosC()].getPosition(), board);
            } else {
                board.getBoard()[listPossibleMoves.getChildAt(0).getData().getPosR()][listPossibleMoves.getChildAt(0).getData().getPosC()] = new ItalianKing(new Position(listPossibleMoves.getChildAt(0).getData().getPosR(), listPossibleMoves.getChildAt(0).getData().getPosC()), board.getBoard()[listPossibleMoves.getData().getPosR()][listPossibleMoves.getData().getPosC()].getPlayer());
                delete(board.getBoard()[listPossibleMoves.getData().getPosR()][listPossibleMoves.getData().getPosC()].getPosition(), board);
            }
        } else {
            executeCapture(board, listPossibleMoves, listPossibleCaptures);
        }
    }

    public static void executeCapture(ItalianBoard board, GenericTreeNode<Position> listPossibleMoves, GenericTreeNode<Position> listPossibleCaptures) {
        if (listPossibleMoves.hasChildren()) {
            if (!isKing(board.getBoard()[listPossibleMoves.getData().getPosR()][listPossibleMoves.getData().getPosC()].getType())) {
                board.getBoard()[listPossibleMoves.getChildAt(0).getData().getPosR()][listPossibleMoves.getChildAt(0).getData().getPosC()] = new ItalianMan(new Position(listPossibleMoves.getChildAt(0).getData().getPosR(), listPossibleMoves.getChildAt(0).getData().getPosC()), board.getBoard()[listPossibleMoves.getData().getPosR()][listPossibleMoves.getData().getPosC()].getPlayer());
                delete(board.getBoard()[listPossibleMoves.getData().getPosR()][listPossibleMoves.getData().getPosC()].getPosition(), board);
                delete(listPossibleCaptures.getChildAt(0).getData(), board);
            } else {
                board.getBoard()[listPossibleMoves.getChildAt(0).getData().getPosR()][listPossibleMoves.getChildAt(0).getData().getPosC()] = new ItalianKing(new Position(listPossibleMoves.getChildAt(0).getData().getPosR(), listPossibleMoves.getChildAt(0).getData().getPosC()), board.getBoard()[listPossibleMoves.getData().getPosR()][listPossibleMoves.getData().getPosC()].getPlayer());
                delete(board.getBoard()[listPossibleMoves.getData().getPosR()][listPossibleMoves.getData().getPosC()].getPosition(), board);
                delete(listPossibleCaptures.getChildAt(0).getData(), board);
            }
            executeCapture(board, listPossibleMoves.getChildAt(0), listPossibleCaptures.getChildAt(0));
        }
    }
    public static ArrayList<Position> sequentialMoves (ItalianBoard board, GenericTreeNode < Position > listPossibleMoves,ArrayList<Position> moves){
        moves.add(listPossibleMoves.getChildAt(0).getData());
        if(listPossibleMoves.getChildAt(0).hasChildren())
            sequentialMoves(board,listPossibleMoves.getChildAt(0),moves);
        return moves;
    }

    /**
     * Deletes the piece in the given position and replaces it with an empty object.
     *
     * @param position of the piece wanted to be deleted
     */
    public static void delete(Position position, ItalianBoard board) {
        board.getBoard()[position.getPosR()][position.getPosC()] = new Empty(position);
    }


    public static boolean isKing(PiecesType pezzo) {
        return (pezzo == PiecesType.KING);
    }

} // class Move
