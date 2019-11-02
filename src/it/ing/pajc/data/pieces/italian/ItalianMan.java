package it.ing.pajc.data.pieces.italian;

import it.ing.pajc.controller.CheckerBoardController;
import it.ing.pajc.data.board.ItalianBoard;
import it.ing.pajc.data.movements.*;
import it.ing.pajc.data.pieces.Man;
import it.ing.pajc.data.pieces.PiecesColors;
import it.ing.pajc.data.pieces.PiecesType;

import java.awt.desktop.SystemEventListener;
import java.util.ArrayList;

/**
 * WhiteMan class defines white man pieces with all implemented methods.
 */
public class ItalianMan extends Man {
    private GenericTreeNode<Position> rootPositions = new GenericTreeNode<>(this.getPosition());
    private GenericTree<Position> possibleMovementsList = new GenericTree<>();

    /**
     * WhiteMan's constructor giving position.
     *
     * @param pos WhiteMan's position.
     */
    public ItalianMan(Position pos, PiecesColors player) {
        super(pos);
        this.setPlayer(player);
        possibleMovementsList.setRoot(rootPositions);
    }

    /**
     * Gives all possible moves of a piece.
     *
     * @param board The using board, must be an 8x8 board.
     * @return the tree of all possible moves.
     */
    @Override
    public GenericTree<Position> possibleMoves(ItalianBoard board) {
        GenericTree<Position> result;
        rootPositions.removeChildren();
        if (!canCapture(board, this.getPosition())) {
            ArrayList<Position> positions = possibleMovesInEmptySpaces(board);
            for (Position position : positions) rootPositions.addChild(new GenericTreeNode<>(position));
            result = possibleMovementsList;
        } else {
            allPossibleCaptures(board);
            result = possibleMovementsList;
        }
        return result;
    }

    /**
     * Calculates all possible captures of a piece, included multiple captures.
     *
     * @param board The using board, must be an 8x8 board.
     */
    @Override
    public void allPossibleCaptures(ItalianBoard board) {
        possibleCapturesUpRightAndLeft(board, rootPositions);
        childrenPossibleCaptures(board, rootPositions);
        System.out.println(possibleMovementsList);
    }

    /**
     * Calculates possible captures after having captured already a piece.
     *
     * @param board           The using board, must be an 8x8 board.
     * @param parentPositions starting position of the creating tree.
     */
    @Override
    public void childrenPossibleCaptures(ItalianBoard board, GenericTreeNode<Position> parentPositions) {
        for (int i = 0; i < parentPositions.getNumberOfChildren(); i++) {
            possibleCapturesUpRightAndLeft(board, parentPositions.getChildAt(i));
            if (canCapture(board, parentPositions.getChildAt(i).getData()))
                childrenPossibleCaptures(board, parentPositions.getChildAt(i));
        }
    }

    /**
     * Calculates all possible captures in all directions where the piece can move.
     *
     * @param board           The using board, must be an 8x8 board.
     * @param parentPositions starting position of the creating tree.
     */
    @Override
    public void possibleCapturesUpRightAndLeft(ItalianBoard board, GenericTreeNode<Position> parentPositions) {
        possibleCaptureUpLeft(board, parentPositions);
        possibleCaptureUpRight(board, parentPositions);
    }

    /**
     * Calculates possible captures on the left.
     *
     * @param board           The using board, must be an 8x8 board.
     * @param parentPositions starting position of the creating tree.
     */
    @Override
    public void possibleCaptureUpLeft(ItalianBoard board, GenericTreeNode<Position> parentPositions) {
        try {
            if ((board.getBoard()[parentPositions.getData().getPosR() - 1][parentPositions.getData().getPosC() - 1].getPlayer() != getPlayer()) &&
                    (board.getBoard()[parentPositions.getData().getPosR() - 1][parentPositions.getData().getPosC() - 1].getType() == PiecesType.MAN) &&
                    (board.getBoard()[parentPositions.getData().getPosR() - 2][parentPositions.getData().getPosC() - 2].getPlayer() == PiecesColors.EMPTY)) {
                parentPositions.addChild(new GenericTreeNode<>(new MoveAndCapturedPosition(
                        parentPositions.getData().getPosR() - 2, parentPositions.getData().getPosC() - 2,
                        parentPositions.getData().getPosR() - 1, parentPositions.getData().getPosC() - 1)));
                /*CheckerBoardController.createClickEventForDeletion(
                        new Position(parentPositions.getData().getPosR() - 1, parentPositions.getData().getPosC() -1),
                        new Position(parentPositions.getData().getPosR() - 2, parentPositions.getData().getPosC() - 2));*/
            }
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
    }

    /**
     * Calculates possible captures on the right.
     *
     * @param board           The using board, must be an 8x8 board.
     * @param parentPositions starting position of the creating tree.
     */
    @Override
    public void possibleCaptureUpRight(ItalianBoard board, GenericTreeNode<Position> parentPositions) {
        try {
            if ((board.getBoard()[parentPositions.getData().getPosR() - 1][parentPositions.getData().getPosC() + 1].getPlayer() != getPlayer()) &&
                    (board.getBoard()[parentPositions.getData().getPosR() - 1][parentPositions.getData().getPosC() + 1].getType() == PiecesType.MAN) &&
                    (board.getBoard()[parentPositions.getData().getPosR() - 2][parentPositions.getData().getPosC() + 2].getPlayer() == PiecesColors.EMPTY)) {
                parentPositions.addChild(new GenericTreeNode<>(new MoveAndCapturedPosition(
                        parentPositions.getData().getPosR() - 2, parentPositions.getData().getPosC() + 2,
                        parentPositions.getData().getPosR() - 1, parentPositions.getData().getPosC() + 1)));

                /*CheckerBoardController.createClickEventForDeletion(
                        new Position(parentPositions.getData().getPosR() - 1, parentPositions.getData().getPosC() + 1),
                        new Position(parentPositions.getData().getPosR() - 2, parentPositions.getData().getPosC() + 2));*/
            }
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
    }

    /**
     * Check if the piece can capture at least a piece.
     *
     * @param board The using board, must be an 8x8 board.
     * @param piece Position of the piece in question.
     * @return a boolean true if can capture, false otherwise.
     */
    @Override
    public boolean canCapture(ItalianBoard board, Position piece) {
        if (canCaptureRight(board, piece) || canCaptureLeft(board, piece))
            return true;
        return false;
    }

    /**
     * Checks whether the piece can capture on the right.
     *
     * @param board The using board, must be an 8x8 board.
     * @param piece Position of the piece in question.
     * @return a boolean true if can capture, false otherwise.
     */
    public boolean canCaptureRight(ItalianBoard board, Position piece) {
        try {
            return ((board.getBoard()[piece.getPosR() - 1][piece.getPosC() - 1].getPlayer() != getPlayer()) &&
                    (board.getBoard()[piece.getPosR() - 1][piece.getPosC() - 1].getType() == PiecesType.MAN) &&
                    (board.getBoard()[piece.getPosR() - 2][piece.getPosC() - 2].getPlayer() == PiecesColors.EMPTY));
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
        return false;
    }

    /**
     * Checks whether the piece can capture on the left.
     *
     * @param board The using board, must be an 8x8 board.
     * @param piece Position of the piece in question.
     * @return a boolean true if can capture, false otherwise.
     */
    public boolean canCaptureLeft(ItalianBoard board, Position piece) {
        try {
            return ((board.getBoard()[piece.getPosR() - 1][piece.getPosC() + 1].getPlayer() != getPlayer()) &&
                    (board.getBoard()[piece.getPosR() - 1][piece.getPosC() + 1].getType() == PiecesType.MAN) &&
                    (board.getBoard()[piece.getPosR() - 2][piece.getPosC() + 2].getPlayer() == PiecesColors.EMPTY));
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
        return false;
    }

    /**
     * Check if the piece can move without captures.
     *
     * @param board The using board, must be an 8x8 board.
     * @param piece Position of the piece in question.
     * @return a boolean true if can move, false otherwise.
     */
    public boolean canMove(ItalianBoard board, Position piece) {
        try {
            if (!canCapture(board, piece))
                return ((board.getBoard()[piece.getPosR() - 1][piece.getPosC() - 1].getPlayer() == PiecesColors.EMPTY) ||
                        (board.getBoard()[piece.getPosR() - 1][piece.getPosC() + 1].getPlayer() == PiecesColors.EMPTY));
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
        return false;
    }

    /**
     * Gives all possible moves when the piece can't capture.
     *
     * @param board The using board, must be an 8x8 board.
     * @return an ArrayList of all possible moves.
     */
    @Override
    public ArrayList<Position> possibleMovesInEmptySpaces(ItalianBoard board) {
        ArrayList<Position> possibleMovementList = new ArrayList<>();

        int posRow = this.getPosition().getPosR();
        int posColumn = this.getPosition().getPosC();
        try {
            if (board.getBoard()[posRow - 1][posColumn - 1].getPlayer() == PiecesColors.EMPTY)//up left
                possibleMovementList.add(new Position(posRow - 1, posColumn - 1));
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
        try {
            if (board.getBoard()[posRow - 1][posColumn + 1].getPlayer() == PiecesColors.EMPTY)//up right
                possibleMovementList.add(new Position(posRow - 1, posColumn + 1));
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
        return possibleMovementList;
    }
}