package it.ing.pajc.data.pieces.white;

import it.ing.pajc.data.board.InternationalBoard;
import it.ing.pajc.data.board.ItalianBoard;
import it.ing.pajc.data.movements.*;
import it.ing.pajc.data.pieces.King;
import it.ing.pajc.data.pieces.PiecesColors;

import java.util.ArrayList;

public class WhiteKing extends King {
    private GenericTreeNode<Position> root = new GenericTreeNode<>(this.getPosition());
    private GenericTree<Position> possibleMovementsList = new GenericTree<>();

    public WhiteKing(Position pos) {
        super(pos);
        this.setPlayer(PiecesColors.WHITE);
        possibleMovementsList.setRoot(root);
    }

    @Override
    public GenericTree possibleMoves(ItalianBoard board) {
        return null;
    }

    @Override
    public void possibleMoves(InternationalBoard board) {

    }

    @Override
    public GenericTree<Position> bestCaptures(ItalianBoard board) {
        return null;
    }

    @Override
    public void allPossibleCaptures(ItalianBoard board) {

    }

    @Override
    public void childrenPossibleCaptures(ItalianBoard board, GenericTreeNode<Position> parent) {

    }

    @Override
    public void possibleCapturesAllDirections(ItalianBoard board, GenericTreeNode<Position> parent) {

    }

    @Override
    public void possibleCaptureUpLeft(ItalianBoard board, GenericTreeNode<Position> parent) {

    }

    @Override
    public void possibleCaptureUpRight(ItalianBoard board, GenericTreeNode<Position> parent) {

    }

    @Override
    public void possibleCaptureDownLeft(ItalianBoard board, GenericTreeNode<Position> parent) {

    }

    @Override
    public void possibleCaptureDownRight(ItalianBoard board, GenericTreeNode<Position> parent) {

    }

    @Override
    public boolean canCapture(ItalianBoard board, Position piece) {
        return false;
    }

    @Override
    public ArrayList<Position> possibleMovesInEmptySpaces(ItalianBoard board) {
        return null;
    }
}
