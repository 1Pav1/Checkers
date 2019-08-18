package it.ing.pajc.data.pieces;

import it.ing.pajc.data.board.InternationalBoard;
import it.ing.pajc.data.board.ItalianBoard;
import it.ing.pajc.data.movements.*;

import java.util.ArrayList;

/**
 * King abstract class defines some methods that has to be use from inherited classes.
 */
public abstract class King extends Pieces {
    /**
     * King's constructor giving position.
     *
     * @param pos King's position
     */
    public King(Position pos) {
        super(pos);
        setType(PiecesType.KING);
    }

    /**
     * Gives all possible moves of a piece.
     *
     * @param board The using board, must be an 8x8 board.
     * @return the tree of all possible moves
     */
    public abstract GenericTree possibleMoves(ItalianBoard board);

    /**
     * Gives all possible moves of a piece.
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
    public abstract void possibleCapturesAllDirections(ItalianBoard board, GenericTreeNode<Position> parent);

    /**
     * Calculates possible captures on the up left.
     *
     * @param board  The using board, must be an 8x8 board.
     * @param parent starting position of the creating tree
     */
    public abstract void possibleCaptureUpLeft(ItalianBoard board, GenericTreeNode<Position> parent);

    /**
     * Calculates possible captures on the up right.
     *
     * @param board  The using board, must be an 8x8 board.
     * @param parent starting position of the creating tree
     */
    public abstract void possibleCaptureUpRight(ItalianBoard board, GenericTreeNode<Position> parent);

    /**
     * Calculates possible captures on the down left.
     *
     * @param board  The using board, must be an 8x8 board.
     * @param parent starting position of the creating tree
     */
    public abstract void possibleCaptureDownLeft(ItalianBoard board, GenericTreeNode<Position> parent);

    /**
     * Calculates possible captures on the down right.
     *
     * @param board  The using board, must be an 8x8 board.
     * @param parent starting position of the creating tree
     */
    public abstract void possibleCaptureDownRight(ItalianBoard board, GenericTreeNode<Position> parent);

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
