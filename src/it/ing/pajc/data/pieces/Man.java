package it.ing.pajc.data.pieces;

import it.ing.pajc.data.board.InternationalBoard;
import it.ing.pajc.data.board.ItalianBoard;
import it.ing.pajc.data.movements.*;

import java.util.ArrayList;

/**
 * Man abstract class defines some methods that has to be use from inherited classes.
 */
public abstract class Man extends Pieces {
    /**
     * Man's constructor giving position.
     *
     * @param pos Man's position
     */
    public Man(Position pos) {
        super(pos);
        setType(PiecesType.MAN);
    }

    /**
     * Gives all possible moves of a piece.
     *
     * @param board The using board, must be an 8x8 board.
     * @return the tree of all possible moves
     */
    public abstract GenericTree possibleMoves(ItalianBoard board);

    /**
     * Gives all possible moves of a piece without taking care about legal moves.
     *
     * @param board The using board, must be a 10x10 board.
     */
    public abstract void possibleMoves(InternationalBoard board);

    /**
     * Gives only the legal moves of a piece.
     *
     * @param board The using board, must be an 8x8 board.
     * @return the tree of legal moves
     */
    public abstract GenericTree<Position> bestCaptures(ItalianBoard board);

    /**
     * Calculates all possible captures of a piece, included multiple captures.
     *
     * @param board The using board, must be an 8x8 board.
     */
    public abstract void allPossibleCaptures(ItalianBoard board);

    /**
     * Calculates possible captures after having captured already a piece.
     *
     * @param board  The using board, must be an 8x8 board.
     * @param parent starting position of the creating tree
     */
    public abstract void childrenPossibleCaptures(ItalianBoard board, GenericTreeNode<Position> parent);

    /**
     * Calculates all possible captures in all directions where the piece can move.
     *
     * @param board  The using board, must be an 8x8 board.
     * @param parent starting position of the creating tree
     */
    public abstract void possibleCapturesUpRightAndLeft(ItalianBoard board, GenericTreeNode<Position> parent);

    /**
     * Calculates possible captures on the left.
     *
     * @param board  The using board, must be an 8x8 board.
     * @param parent starting position of the creating tree
     */
    public abstract void possibleCaptureUpLeft(ItalianBoard board, GenericTreeNode<Position> parent);

    /**
     * Calculates possible captures on the right.
     *
     * @param board  The using board, must be an 8x8 board.
     * @param parent starting position of the creating tree
     */
    public abstract void possibleCaptureUpRight(ItalianBoard board, GenericTreeNode<Position> parent);

    /**
     * Check if the piece can capture at least a piece.
     *
     * @param board The using board, must be an 8x8 board.
     * @param piece Position of the piece in question
     * @return a boolean true if can capture, false otherwise
     */
    public abstract boolean canCapture(ItalianBoard board, Position piece);

    /**
     * Gives all possible moves when the piece can't capture.
     *
     * @param board The using board, must be an 8x8 board.
     * @return an ArrayList of all possible moves.
     */
    public abstract ArrayList<Position> possibleMovesInEmptySpaces(ItalianBoard board);
}