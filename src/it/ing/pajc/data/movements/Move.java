package it.ing.pajc.data.movements;

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
                allPossibleCaptures.add(board.getKing(posR, posC).possibleCaptures(board));
            else
                allPossibleCaptures.add(board.getMan(posR, posC).possibleCaptures(board));
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
     * Gives informations about moves possibilities of a piece.
     *
     * @param board The using board.
     * @return true if a piece can't move and capture, false otherwise.
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
    private static boolean canCapture(ItalianBoard board, int posR, int posC) {
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
     * @param posR Row position.
     * @param posC Column position.
     * @return true if the position is inside the board, false otherwise.
     */
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
     * Check if a piece is a king.
     *
     * @param piece to check.
     * @return true if this is a king, else return false.
     */
    private static boolean isKing(PiecesType piece) {
        return (piece == PiecesType.KING);
    }
}
