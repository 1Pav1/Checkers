package it.ing.pajc.data.pieces;

import it.ing.pajc.data.board.InternationalBoard;
import it.ing.pajc.data.board.ItalianBoard;
import it.ing.pajc.data.movements.*;

import java.util.ArrayList;

public abstract class King extends Pieces {

    public King(Position pos) {
        super(pos);
        setType(PiecesType.KING);
    }

    public abstract GenericTree possibleMoves(ItalianBoard board);

    public abstract void possibleMoves(InternationalBoard board);

    public abstract GenericTree<Position> bestCaptures(ItalianBoard board);

    public abstract void allPossibleCaptures(ItalianBoard board);

    public abstract void childrenPossibleCaptures(ItalianBoard board, GenericTreeNode<Position> parent);

    public abstract void possibleCapturesAllDirections(ItalianBoard board, GenericTreeNode<Position> parent);

    public abstract void possibleCaptureUpLeft(ItalianBoard board, GenericTreeNode<Position> parent);

    public abstract void possibleCaptureUpRight(ItalianBoard board, GenericTreeNode<Position> parent);

    public abstract void possibleCaptureDownLeft(ItalianBoard board, GenericTreeNode<Position> parent);

    public abstract void possibleCaptureDownRight(ItalianBoard board, GenericTreeNode<Position> parent);

    public abstract boolean canCapture(ItalianBoard board, Position piece);

    public abstract ArrayList<Position> possibleMovesInEmptySpaces(ItalianBoard board);
}
