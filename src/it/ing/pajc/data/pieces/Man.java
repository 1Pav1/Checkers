package it.ing.pajc.data.pieces;

import it.ing.pajc.data.board.InternationalBoard;
import it.ing.pajc.data.board.ItalianBoard;
import it.ing.pajc.data.movements.*;

import java.util.ArrayList;

public abstract class Man extends Pieces {

    public Man(Position pos) {
        super(pos);
        setType(PiecesType.MAN);
    }

    public  abstract GenericTree possibleMoves(ItalianBoard board);

    public abstract void possibleMoves(InternationalBoard board);

    public abstract GenericTree<Position> bestCaptures(ItalianBoard board);

    public abstract void allPossibleCaptures(ItalianBoard board);

    public abstract void childrenPossibleCaptures(ItalianBoard board, GenericTreeNode<Position> parent);

    public abstract void possibleCapturesUpRightAndLeft(ItalianBoard board, GenericTreeNode<Position> parent);

    public abstract void possibleCaptureUpLeft(ItalianBoard board, GenericTreeNode<Position> parent);

    public abstract void possibleCaptureUpRight(ItalianBoard board, GenericTreeNode<Position> parent);

    public abstract boolean canCapture(ItalianBoard board,Position piece);

    public abstract ArrayList<Position> possibleMovesInEmptySpaces(ItalianBoard board);
}