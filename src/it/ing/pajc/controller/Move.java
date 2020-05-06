package it.ing.pajc.controller;

import it.ing.pajc.data.board.Board;
import it.ing.pajc.data.board.ItalianBoard;
import it.ing.pajc.data.movements.Position;
import it.ing.pajc.data.pieces.PieceType;
import it.ing.pajc.data.pieces.PlaceType;
import it.ing.pajc.manager.Player;

import java.util.*;

public class Move {

    public static void executeMove(Position init, Position fin, ItalianBoard board) {
        PlaceType place = board.getBoard()[init.getPosR()][init.getPosC()].getPlace();
        if (CheckPossibleMovements.canCapture(board, init.getPosR(),init.getPosC()))
            deleteCaptured(fin, board);
        if (checkKingTransformation(fin) || (board.getBoard()[init.getPosR()][init.getPosC()]).getPiece() == PieceType.KING)
            board.getBoard()[fin.getPosR()][fin.getPosC()].setPiece(PieceType.KING);
        else
            board.getBoard()[fin.getPosR()][fin.getPosC()].setPiece(PieceType.MAN);

        board.getBoard()[fin.getPosR()][fin.getPosC()].setPlace(place);
        delete(init, board);
    }

    /**
     * Generates moves of all pieces of a player.
     *
     * @param board  The using board.
     * @param player The actual player.
     * @return an arrayList of all possible moves of pieces.
     */
    public static ArrayList<Position> generateMoves(ItalianBoard board, PlaceType player) {
        int posR;
        int posC;
        ArrayList<Position> piecesWhoCanMove = piecesWhoCanMove(board, player);
        ArrayList<Position> allPossibleMoves = new ArrayList<>();
        for (Position position : piecesWhoCanMove) {
            posR = position.getPosR();
            posC = position.getPosC();

            allPossibleMoves.addAll(CheckPossibleMovements.allPossibleMoves(board, posR, posC));
        }
        return allPossibleMoves;
    }

    /**
     * Generates moves of all pieces of a player.
     *
     * @param board  The using board.
     * @param player The actual player.
     * @return an arrayList of all possible moves of pieces.
     */
    public static ArrayList<Position> generateCaptures(ItalianBoard board, PlaceType player) {
        int posR;
        int posC;
        ArrayList<Position> piecesWhoCanMove = piecesWhoCanMove(board, player);
        ArrayList<Position> allPossibleCaptures = new ArrayList<>();
        for (Position position : piecesWhoCanMove) {
            posR = position.getPosR();
            posC = position.getPosC();

            allPossibleCaptures.addAll(CheckPossibleMovements.allPossibleCaptures(board, posR, posC));


        }
        return allPossibleCaptures;
    }

    /**
     * Checks if a piece can capture or move.
     *
     * @param piecesWhoCanMove list of pieces position who can capture or move.
     * @param pieceToCheck     if is inside the piecesWhoCanMove list.
     * @return true if the piece is inside the list, false otherwise.
     */
    public static boolean canDoSomething(ArrayList<Position> piecesWhoCanMove, Position pieceToCheck) {
        for (Position position : piecesWhoCanMove) {
            if (position == pieceToCheck)
                return true;
        }
        return false;
    }


    public static boolean canSomebodyDoSomething(ItalianBoard board, Player player){
        for(int i=0;i< Board.DIMENSION_ITALIAN_BOARD;i++)
            for(int j=0;j< Board.DIMENSION_ITALIAN_BOARD;j++)
                if(PlaceType.confrontPlayer(board.getBoard()[i][j].getPlace(), player) && (!CheckPossibleMovements.allPossibleMoves(board,i,j).isEmpty()))
                    return true;
        return  false;
    }
    /**
     * First check if pieces can captures and adds them in arrayList,
     * if none can captures do the same for movements else return an empty arrayList.
     *
     * @param board  The using board.
     * @param player The actual player.
     * @return an arrayList with all positions of pieces which can move or capture.
     */
    public static ArrayList<Position> piecesWhoCanMove(ItalianBoard board, PlaceType player) {
        ArrayList<Position> pieces = new ArrayList<>();
        for (int posR = 0; posR < 8; posR++)
            for (int posC = 0; posC < 8; posC++) {
                if (board.getBoard()[posR][posC].getPlace() == player) {
                    if (CheckPossibleMovements.canCapture(board, posR, posC))
                        pieces.add(new Position(posR, posC));
                }
            }
        if (pieces.isEmpty())
            for (int posR = 0; posR < 8; posR++)
                for (int posC = 0; posC < 8; posC++) {
                    if (board.getBoard()[posR][posC].getPlace() == player) {
                        if (CheckPossibleMovements.canCapture(board, posR, posC))
                            pieces.add(new Position(posR, posC));
                    }
                }
        return pieces;
    }


    /**
     * Checks if the position is inside the board.
     *
     * @param position of the piece to check.
     * @return true if the position is inside the board, false otherwise.
     */
    public static boolean inRange(Position position) {
        return (position.getPosR() > -1 && position.getPosR() < 8 && position.getPosC() > -1 && position.getPosC() < 8);
    }



    private static boolean checkKingTransformation(Position pos) {
        return pos.getPosR() == 0;
    }


    /**
     * Esegue la mossa in input ed eventualmente "mangia"
     *//*
    public static void executeMove(ItalianBoard board, Position init, Position fin, List<GenericTreeNode> listPossibleMovesAndCaptures) {
        CheckerBoardController.addToTextArea(board.getBoard()[init.getPosR()][init.getPosC()].getPlayer()+" moved from " + init.getPosR() + " " + init.getPosC()+" "+ "-> " + fin.getPosR() + " " +fin.getPosC()+"\n");
        for (int p = 1; p < listPossibleMovesAndCaptures.size(); p++) {
            Position positionPossibleCaptures = (Position) listPossibleMovesAndCaptures.get(p).getData();
            if (canCapture(board, positionPossibleCaptures.getPosR(), positionPossibleCaptures.getPosC())) {
                System.out.println("Captured " + ((MoveAndCapturedPosition) positionPossibleCaptures).getcPosR() + " " + ((MoveAndCapturedPosition) positionPossibleCaptures).getcPosC());

                System.out.println("Moved " + positionPossibleCaptures.getPosR() + " " + positionPossibleCaptures.getPosC());

                delete(((MoveAndCapturedPosition) positionPossibleCaptures).getCapturedPosition(), board);

            } else {
                System.out.println("Moved " + positionPossibleCaptures.getPosR() + " " + positionPossibleCaptures.getPosC());
            }
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
        delete(init, board);
        // check for new king
        if (board.getBoard()[fin.getPosR()][fin.getPosC()].getPlayer() == PiecesColors.WHITE && fin.getPosR() == 0)
            board.getBoard()[fin.getPosR()][fin.getPosC()] = new ItalianKing(new Position(fin.getPosR(), fin.getPosC()), PiecesColors.WHITE);
        else if (board.getBoard()[fin.getPosR()][fin.getPosC()].getPlayer() == PiecesColors.BLACK && fin.getPosR() == 0)
            board.getBoard()[fin.getPosR()][fin.getPosC()] = new ItalianKing(new Position(fin.getPosR(), fin.getPosC()), PiecesColors.BLACK);

    }

    public static void executeMove(ItalianBoard board, GenericTreeNode<Position> listPossibleMoves) {
        if (!canCapture(board, listPossibleMoves.getData().getPosR(), listPossibleMoves.getData().getPosC())) {
            if (!isKing(board.getBoard()[listPossibleMoves.getData().getPosR()][listPossibleMoves.getData().getPosC()].getType())) {
                board.getBoard()[listPossibleMoves.getChildAt(0).getData().getPosR()][listPossibleMoves.getChildAt(0).getData().getPosC()] =
                        new ItalianMan(new Position(listPossibleMoves.getChildAt(0).getData().getPosR(), listPossibleMoves.getChildAt(0).getData().getPosC()),
                                board.getBoard()[listPossibleMoves.getData().getPosR()][listPossibleMoves.getData().getPosC()].getPlayer());
                delete(board.getBoard()[listPossibleMoves.getData().getPosR()][listPossibleMoves.getData().getPosC()].getPosition(), board);
            } else {
                board.getBoard()[listPossibleMoves.getChildAt(0).getData().getPosR()][listPossibleMoves.getChildAt(0).getData().getPosC()] =
                        new ItalianKing(new Position(listPossibleMoves.getChildAt(0).getData().getPosR(), listPossibleMoves.getChildAt(0).getData().getPosC()),
                                board.getBoard()[listPossibleMoves.getData().getPosR()][listPossibleMoves.getData().getPosC()].getPlayer());
                delete(board.getBoard()[listPossibleMoves.getData().getPosR()][listPossibleMoves.getData().getPosC()].getPosition(), board);
            }
        } else {
            executeCapture(board, listPossibleMoves);
        }
    }

    public static void executeCapture(ItalianBoard board, GenericTreeNode<Position> listPossibleMoves) {
        if (listPossibleMoves.hasChildren()) {
            if (!isKing(board.getBoard()[listPossibleMoves.getData().getPosR()][listPossibleMoves.getData().getPosC()].getType())) {
                board.getBoard()[listPossibleMoves.getChildAt(0).getData().getPosR()][listPossibleMoves.getChildAt(0).getData().getPosC()] =
                        new ItalianMan(new Position(listPossibleMoves.getChildAt(0).getData().getPosR(),
                                listPossibleMoves.getChildAt(0).getData().getPosC()), board.getBoard()[listPossibleMoves.getData().getPosR()][listPossibleMoves.getData().getPosC()].getPlayer());
                delete(board.getBoard()[listPossibleMoves.getData().getPosR()][listPossibleMoves.getData().getPosC()].getPosition(), board);
                delete(((MoveAndCapturedPosition) listPossibleMoves.getChildAt(0).getData()).getCapturedPosition(), board);
            } else {
                board.getBoard()[listPossibleMoves.getChildAt(0).getData().getPosR()][listPossibleMoves.getChildAt(0).getData().getPosC()] =
                        new ItalianKing(new Position(listPossibleMoves.getChildAt(0).getData().getPosR(),
                                listPossibleMoves.getChildAt(0).getData().getPosC()), board.getBoard()[listPossibleMoves.getData().getPosR()][listPossibleMoves.getData().getPosC()].getPlayer());
                delete(board.getBoard()[listPossibleMoves.getData().getPosR()][listPossibleMoves.getData().getPosC()].getPosition(), board);
                delete(((MoveAndCapturedPosition) listPossibleMoves.getChildAt(0).getData()).getCapturedPosition(), board);
            }
            executeCapture(board, listPossibleMoves.getChildAt(0));
        }
    }*/



    public static void delete(Position position, ItalianBoard board) {
        board.getBoard()[position.getPosR()][position.getPosC()].setPlace(PlaceType.EMPTY);
        board.getBoard()[position.getPosR()][position.getPosC()].setPiece(null);
    }

    public static void deleteCaptured(Position position, ItalianBoard board) {
        board.getBoard()[position.getcPosR()][position.getcPosC()].setPlace(PlaceType.EMPTY);
        board.getBoard()[position.getcPosR()][position.getcPosC()].setPiece(null);
    }

    private static boolean isKing(ItalianBoard board, int posR, int posC) {
        return (board.getBoard()[posR][posC].getPiece() == PieceType.KING);
    }
}
