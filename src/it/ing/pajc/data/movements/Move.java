package it.ing.pajc.data.movements;

import it.ing.pajc.controller.CheckerBoardController;
import it.ing.pajc.data.board.ItalianBoard;
import it.ing.pajc.data.pieces.Empty;
import it.ing.pajc.data.pieces.PiecesColors;
import it.ing.pajc.data.pieces.PiecesType;
import it.ing.pajc.data.pieces.italian.ItalianKing;
import it.ing.pajc.data.pieces.italian.ItalianMan;

import java.util.*;

public class Move {

    /**
     * Generates moves of all pieces of a player.
     *
     * @param board  The using board.
     * @param player The actual player.
     * @return an arrayList of all possible moves of pieces.
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

    /**
     * Generates moves of all pieces of a player.
     *
     * @param board  The using board.
     * @param player The actual player.
     * @return an arrayList of all possible moves of pieces.
     */
    public static ArrayList<GenericTree<Position>> generateCaptures(ItalianBoard board, PiecesColors player) {
        int posR;
        int posC;
        ArrayList<Position> piecesWhoCanMove = piecesWhoCanMove(board, player);
        ArrayList<GenericTree<Position>> allPossibleCaptures = new ArrayList<>();
        for (Position position : piecesWhoCanMove) {
            posR = position.getPosR();
            posC = position.getPosC();

            if (isKing(board.getBoard()[posR][posC].getType()))
                allPossibleCaptures.add(board.getKing(posR, posC).possibleMoves(board));
            else
                allPossibleCaptures.add(board.getMan(posR, posC).possibleMoves(board));
        }
        return allPossibleCaptures;
    }

    /**
     * First check if pieces can captures and adds them in arrayList,
     * if none can captures do the same for movements else return an empty arrayList.
     *
     * @param board  The using board.
     * @param player The actual player.
     * @return an arrayList with all positions of pieces which can move or capture.
     */
    public static ArrayList<Position> piecesWhoCanMove(ItalianBoard board, PiecesColors player) {
        ArrayList<Position> pieces = new ArrayList<>();
        for (int posR = 0; posR < 8; posR++)
            for (int posC = 0; posC < 8; posC++) {
                if (board.getBoard()[posR][posC].getPlayer() == player) {
                    if (canCapture(board, posR, posC))
                        pieces.add(new Position(posR, posC));
                }
            }
        if (pieces.isEmpty())
            for (int posR = 0; posR < 8; posR++)
                for (int posC = 0; posC < 8; posC++) {
                    if (board.getBoard()[posR][posC].getPlayer() == player) {
                        if (canWalk(board, posR, posC))
                            pieces.add(new Position(posR, posC));
                    }
                }
        return pieces;
    }

    /**
     * Checks if a piece can capture or move.
     *
     * @param piecesWhoCanMove list of pieces position who can capture or move.
     * @param pieceToCheck     if is inside the piecesWhoCanMove list.
     * @return true if the piece is inside the list, false otherwise.
     */
    public static boolean canDoSomething(ArrayList<Position> piecesWhoCanMove, Position pieceToCheck) {
        for (int i = 0; i < piecesWhoCanMove.size(); i++) {
            if (piecesWhoCanMove.get(i).getPosition() == pieceToCheck)
                return true;
        }
        return false;
    }

    //TODO ci penso io
    public static ArrayList<Position> possibleFinalMoves() {
        return null;
    }

    /**
     * Gives informations about moves possibilities of a piece.
     *
     * @param board The using board.
     * @return true if all the pieces on the board can't move and capture, false otherwise.
     */
    public static boolean noMovesLeft(ItalianBoard board, PiecesColors player) {
        for (int posR = 0; posR < 8; posR++)
            for (int posC = 0; posC < 8; posC++)
                if (board.getBoard()[posR][posC].getPlayer() == player)
                    if (canWalk(board, posR, posC) || canCapture(board, posR, posC))
                        return false;
        return true;
    }

    /**
     * Check if a piece can capture.
     *
     * @param board the given one.
     * @param posR  of the piece.
     * @param posC  of the piece.
     * @return true if can capture, otherwise return false.
     */
    public static boolean canCapture(ItalianBoard board, int posR, int posC) {
        boolean result = false;
        if (board.getBoard()[posR][posC].getType() == PiecesType.MAN)
            result = board.getMan(posR, posC).canCapture(board, new Position(posR, posC));
        else if (board.getBoard()[posR][posC].getType() == PiecesType.KING)
            result = board.getKing(posR, posC).canCapture(board, new Position(posR, posC));
        return result;
    }

    public static boolean canCapture(ItalianBoard board, Position pos) {
        int posR = pos.getPosR();
        int posC = pos.getPosC();
        boolean result = false;
        if (board.getBoard()[posR][posC].getType() == PiecesType.MAN)
            result = board.getMan(posR, posC).canCapture(board, new Position(posR, posC));
        else if (board.getBoard()[posR][posC].getType() == PiecesType.KING)
            result = board.getKing(posR, posC).canCapture(board, new Position(posR, posC));
        return result;
    }

    /**
     * Checks if is man or king and gets the positions where it can move.
     *
     * @param board the current board.
     * @param posR  row positions.
     * @param posC  columns positions.
     * @return true if it can walk.
     */
    private static boolean canWalk(ItalianBoard board, int posR, int posC) {
        boolean result = false;
        if (board.getBoard()[posR][posC].getType() == PiecesType.MAN)
            result = board.getMan(posR, posC).canMove(board, new Position(posR, posC));
        else if (board.getBoard()[posR][posC].getType() == PiecesType.KING)
            result = board.getKing(posR, posC).canMove(board, new Position(posR, posC));
        return result;
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

    public static void executeMove(Position init, Position fin, ItalianBoard italianBoard) {
        if (canCapture(italianBoard, init)) {
            deleteCaptured(fin, italianBoard);
            CheckerBoardController.addToTextArea(fin.getPosition() + "\n");
        } else
            CheckerBoardController.addToTextArea("Moving to :" + fin + "\n");
        if (checkKingTransformation(fin) || (italianBoard.getBoard()[init.getPosR()][init.getPosC()]).getType() == PiecesType.KING)
            italianBoard.getBoard()[fin.getPosR()][fin.getPosC()] = new ItalianKing(fin, italianBoard.getPlayer());
        else
            italianBoard.getBoard()[fin.getPosR()][fin.getPosC()] = new ItalianMan(fin, italianBoard.getPlayer());
        delete(init, italianBoard);
    }

    private static boolean checkKingTransformation(Position pos) {
        if (pos.getPosR() == 0)
            return true;
        return false;
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
    public static ArrayList<Position> sequentialMoves(ItalianBoard board, GenericTreeNode<Position> listPossibleMoves, ArrayList<Position> moves) {
        moves.add(listPossibleMoves.getChildAt(0).getData());
        if (listPossibleMoves.getChildAt(0).hasChildren())
            sequentialMoves(board, listPossibleMoves.getChildAt(0), moves);
        return moves;
    }

    /**
     * Deletes the piece in the given position and replaces it with an empty object.
     *
     * @param position of the piece wanted to be deleted.
     */
    public static void delete(Position position, ItalianBoard board) {
        board.getBoard()[position.getPosR()][position.getPosC()] = new Empty(position);
    }
    /**
     * Deletes the piece in the given position and replaces it with an empty object.
     *
     * @param position of the piece wanted to be deleted.
     */
    public static void deleteCaptured(Position position, ItalianBoard board) {
        board.getBoard()[position.getcPosR()][position.getcPosC()] = new Empty(position);
    }

    /**
     * Check if a piece is a king.
     *
     * @param piece to check.
     * @return true if this is a king, else return false.
     */
    private static boolean isKing(PiecesType piece) {
        return (piece == PiecesType.KING);
    }
}
