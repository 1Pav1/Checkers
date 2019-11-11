package it.ing.pajc.data.pieces.italian;

import it.ing.pajc.data.board.Fen;
import it.ing.pajc.data.board.ItalianBoard;
import it.ing.pajc.data.movements.*;
import it.ing.pajc.data.pieces.Empty;
import it.ing.pajc.data.pieces.King;
import it.ing.pajc.data.pieces.PiecesColors;
import it.ing.pajc.data.pieces.PiecesType;

import java.util.ArrayList;

/**
 * WhiteKing class defines white king pieces with all implemented methods.
 */
public class ItalianKing extends King {
    private GenericTreeNode<Position> rootPositions = new GenericTreeNode<>(this.getPosition());
    private GenericTree<Position> possibleMovementsList = new GenericTree<>();

    /**
     * WhiteKing's constructor giving position.
     *
     * @param pos WhiteKing's position.
     */
    public ItalianKing(Position pos, PiecesColors player) {
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
        rootPositions.removeChildren();
        if (!canCapture(board, this.getPosition())) {
            ArrayList<Position> positions = possibleMovesInEmptySpaces(board);
            for (Position position : positions) rootPositions.addChild(new GenericTreeNode<>(position));
            return possibleMovementsList;
        } else {
            allPossibleCaptures(board);
        }
        return possibleMovementsList;
    }

    /**
     * Calculates all possible captures of a piece, included multiple captures.
     * IMPORTANT: Use this method because otherwise this is going to delete unpredictable pieces.
     *
     * @param board The using board, must be an 8x8 board.
     */
    @Override
    public void allPossibleCaptures(ItalianBoard board) {
        possibleCapturesAllDirections(board, rootPositions);
        childrenPossibleCaptures(board, rootPositions);
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
            possibleCapturesAllDirections(board, parentPositions.getChildAt(i));
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
    public void possibleCapturesAllDirections(ItalianBoard board, GenericTreeNode<Position> parentPositions) {
        possibleCaptureUpLeft(board, parentPositions);
        possibleCaptureUpRight(board, parentPositions);
        possibleCaptureDownLeft(board, parentPositions);
        possibleCaptureDownRight(board, parentPositions);
    }

    /**
     * Calculates possible captures on the up left.
     *
     * @param board           The using board, must be an 8x8 board.
     * @param parentPositions starting position of the creating tree.
     */
    @Override
    public void possibleCaptureUpLeft(ItalianBoard board, GenericTreeNode<Position> parentPositions) {
        try {
            if ((board.getBoard()[parentPositions.getData().getPosR() - 1][parentPositions.getData().getPosC() - 1].getPlayer() != getPlayer()) &&
                    (board.getBoard()[parentPositions.getData().getPosR() - 1][parentPositions.getData().getPosC() - 1].getType() == PiecesType.MAN ||
                            board.getBoard()[parentPositions.getData().getPosR() - 1][parentPositions.getData().getPosC() - 1].getType() == PiecesType.KING) &&
                    (board.getBoard()[parentPositions.getData().getPosR() - 2][parentPositions.getData().getPosC() - 2].getPlayer() == PiecesColors.EMPTY)) {
                parentPositions.addChild(new GenericTreeNode<>(new Position(
                        parentPositions.getData().getPosR() - 2, parentPositions.getData().getPosC() - 2,
                        parentPositions.getData().getPosR() - 1, parentPositions.getData().getPosC() - 1)));
            }
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
    }

    /**
     * Calculates possible captures on the up right.
     *
     * @param board           The using board, must be an 8x8 board.
     * @param parentPositions starting position of the creating tree.
     */
    @Override
    public void possibleCaptureUpRight(ItalianBoard board, GenericTreeNode<Position> parentPositions) {
        try {
            if ((board.getBoard()[parentPositions.getData().getPosR() - 1][parentPositions.getData().getPosC() + 1].getPlayer() != getPlayer()) &&
                    (board.getBoard()[parentPositions.getData().getPosR() - 1][parentPositions.getData().getPosC() + 1].getType() == PiecesType.MAN ||
                            board.getBoard()[parentPositions.getData().getPosR() - 1][parentPositions.getData().getPosC() + 1].getType() == PiecesType.KING) &&
                    (board.getBoard()[parentPositions.getData().getPosR() - 2][parentPositions.getData().getPosC() + 2].getPlayer() == PiecesColors.EMPTY)) {
                parentPositions.addChild(new GenericTreeNode<>(new Position(
                        parentPositions.getData().getPosR() - 2, parentPositions.getData().getPosC() + 2,
                        parentPositions.getData().getPosR() - 1, parentPositions.getData().getPosC() + 1)));
            }
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
    }

    /**
     * Calculates possible captures on the down left.
     *
     * @param board           The using board, must be an 8x8 board.
     * @param parentPositions starting position of the creating tree.
     */
    @Override
    public void possibleCaptureDownLeft(ItalianBoard board, GenericTreeNode<Position> parentPositions) {
        try {
            if ((board.getBoard()[parentPositions.getData().getPosR() + 1][parentPositions.getData().getPosC() - 1].getPlayer() != getPlayer()) &&
                    (board.getBoard()[parentPositions.getData().getPosR() + 1][parentPositions.getData().getPosC() - 1].getType() == PiecesType.MAN ||
                            board.getBoard()[parentPositions.getData().getPosR() + 1][parentPositions.getData().getPosC() - 1].getType() == PiecesType.KING) &&
                    (board.getBoard()[parentPositions.getData().getPosR() + 2][parentPositions.getData().getPosC() - 2].getPlayer() == PiecesColors.EMPTY)) {

                parentPositions.addChild(new GenericTreeNode<>(new Position(
                        parentPositions.getData().getPosR() + 2, parentPositions.getData().getPosC() - 2,
                        parentPositions.getData().getPosR() + 1, parentPositions.getData().getPosC() - 1)));
            }
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
    }

    /**
     * Calculates possible captures on the down right.
     *
     * @param board           The using board, must be an 8x8 board.
     * @param parentPositions starting position of the creating tree.
     */
    @Override
    public void possibleCaptureDownRight(ItalianBoard board, GenericTreeNode<Position> parentPositions) {
        try {
            if ((board.getBoard()[parentPositions.getData().getPosR() + 1][parentPositions.getData().getPosC() + 1].getPlayer() != getPlayer()) &&
                    (board.getBoard()[parentPositions.getData().getPosR() + 1][parentPositions.getData().getPosC() + 1].getType() == PiecesType.MAN ||
                            board.getBoard()[parentPositions.getData().getPosR() + 1][parentPositions.getData().getPosC() + 1].getType() == PiecesType.KING) &&
                    (board.getBoard()[parentPositions.getData().getPosR() + 2][parentPositions.getData().getPosC() + 2].getPlayer() == PiecesColors.EMPTY)) {

                parentPositions.addChild(new GenericTreeNode<>(new Position(
                        parentPositions.getData().getPosR() + 2, parentPositions.getData().getPosC() + 2,
                        parentPositions.getData().getPosR() + 1, parentPositions.getData().getPosC() + 1)));
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

        if (canCaptureUpLeft(board, piece) || canCaptureUpRight(board, piece) || canCaptureDownLeft(board, piece) || canCaptureDownRight(board, piece))
            return true;
        return false;
    }

    private boolean canCaptureUpLeft(ItalianBoard board, Position piece) {
        try {
            return ((board.getBoard()[piece.getPosR() - 1][piece.getPosC() - 1].getPlayer() != getPlayer()) &&
                    (board.getBoard()[piece.getPosR() - 1][piece.getPosC() - 1].getPlayer() != PiecesColors.EMPTY) &&
                    (board.getBoard()[piece.getPosR() - 2][piece.getPosC() - 2].getPlayer() == PiecesColors.EMPTY));
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
        return false;
    }

    private boolean canCaptureUpRight(ItalianBoard board, Position piece) {
        try {
            return ((board.getBoard()[piece.getPosR() - 1][piece.getPosC() + 1].getPlayer() != getPlayer()) &&
                    (board.getBoard()[piece.getPosR() - 1][piece.getPosC() + 1].getPlayer() != PiecesColors.EMPTY) &&
                    (board.getBoard()[piece.getPosR() - 2][piece.getPosC() + 2].getPlayer() == PiecesColors.EMPTY));
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
        return false;
    }

    private boolean canCaptureDownLeft(ItalianBoard board, Position piece) {
        try {
            return ((board.getBoard()[piece.getPosR() + 1][piece.getPosC() - 1].getPlayer() != getPlayer()) &&
                    (board.getBoard()[piece.getPosR() + 1][piece.getPosC() - 1].getPlayer() != PiecesColors.EMPTY) &&
                    (board.getBoard()[piece.getPosR() + 2][piece.getPosC() - 2].getPlayer() == PiecesColors.EMPTY));
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
        return false;
    }

    private boolean canCaptureDownRight(ItalianBoard board, Position piece) {
        try {
            return ((board.getBoard()[piece.getPosR() + 1][piece.getPosC() + 1].getPlayer() != getPlayer()) &&
                    (board.getBoard()[piece.getPosR() + 1][piece.getPosC() + 1].getPlayer() != PiecesColors.EMPTY) &&
                    (board.getBoard()[piece.getPosR() + 2][piece.getPosC() + 2].getPlayer() == PiecesColors.EMPTY));
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
            return ((board.getBoard()[piece.getPosR() - 1][piece.getPosC() - 1].getPlayer() == PiecesColors.EMPTY) ||
                    (board.getBoard()[piece.getPosR() - 1][piece.getPosC() + 1].getPlayer() == PiecesColors.EMPTY) ||
                    (board.getBoard()[piece.getPosR() + 1][piece.getPosC() - 1].getPlayer() == PiecesColors.EMPTY) ||
                    (board.getBoard()[piece.getPosR() + 1][piece.getPosC() + 1].getPlayer() == PiecesColors.EMPTY));
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

        try {
            if (board.getBoard()[posRow + 1][posColumn - 1].getPlayer() == PiecesColors.EMPTY)//down left
                possibleMovementList.add(new Position(posRow + 1, posColumn - 1));
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }

        try {
            if (board.getBoard()[posRow + 1][posColumn + 1].getPlayer() == PiecesColors.EMPTY)//down right
                possibleMovementList.add(new Position(posRow + 1, posColumn + 1));
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
        return possibleMovementList;
    }

    /**
     * Gives all possible moves of a piece.
     *
     * @param board The using board, must be an 8x8 board.
     * @return the tree of all possible moves.
     */
    public GenericTree<Position> enginePossibleMoves(ItalianBoard board) {
        ItalianBoard tmpBoard = new ItalianBoard(new Fen(board.toString()), board.getPlayer());
        rootPositions.removeChildren();
        if (!canCapture(tmpBoard, this.getPosition())) {
            ArrayList<Position> positions = possibleMovesInEmptySpaces(tmpBoard);
            for (Position position : positions) rootPositions.addChild(new GenericTreeNode<>(position));
            return possibleMovementsList;
        } else {
            engineAllPossibleCaptures(tmpBoard);
        }
        return possibleMovementsList;
    }

    /**
     * Calculates all possible captures of a piece, included multiple captures.
     * IMPORTANT: Use this method because otherwise this is going to delete unpredictable pieces.
     *
     * @param board The using board, must be an 8x8 board.
     */
    public void engineAllPossibleCaptures(ItalianBoard board) {
        enginePossibleCapturesAllDirections(board, rootPositions);
        engineChildrenPossibleCaptures(board, rootPositions);
    }

    /**
     * Calculates possible captures after having captured already a piece.
     *
     * @param board           The using board, must be an 8x8 board.
     * @param parentPositions starting position of the creating tree.
     */
    public void engineChildrenPossibleCaptures(ItalianBoard board, GenericTreeNode<Position> parentPositions) {
        for (int i = 0; i < parentPositions.getNumberOfChildren(); i++) {
            enginePossibleCapturesAllDirections(board, parentPositions.getChildAt(i));
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
    public void enginePossibleCapturesAllDirections(ItalianBoard board, GenericTreeNode<Position> parentPositions) {
        enginePossibleCaptureUpLeft(board, parentPositions);
        enginePossibleCaptureUpRight(board, parentPositions);
        enginePossibleCaptureDownLeft(board, parentPositions);
        enginePossibleCaptureDownRight(board, parentPositions);
    }

    /**
     * Calculates possible captures on the up left.
     *
     * @param board           The using board, must be an 8x8 board.
     * @param parentPositions starting position of the creating tree.
     */
    public void enginePossibleCaptureUpLeft(ItalianBoard board, GenericTreeNode<Position> parentPositions) {
        try {
            if ((board.getBoard()[parentPositions.getData().getPosR() - 1][parentPositions.getData().getPosC() - 1].getPlayer() != getPlayer()) &&
                    (board.getBoard()[parentPositions.getData().getPosR() - 1][parentPositions.getData().getPosC() - 1].getType() == PiecesType.MAN ||
                            board.getBoard()[parentPositions.getData().getPosR() - 1][parentPositions.getData().getPosC() - 1].getType() == PiecesType.KING) &&
                    (board.getBoard()[parentPositions.getData().getPosR() - 2][parentPositions.getData().getPosC() - 2].getPlayer() == PiecesColors.EMPTY)) {
                parentPositions.addChild(new GenericTreeNode<>(new Position(
                        parentPositions.getData().getPosR() - 2, parentPositions.getData().getPosC() - 2,
                        parentPositions.getData().getPosR() - 1, parentPositions.getData().getPosC() - 1)));
                board.getBoard()[parentPositions.getData().getPosR() - 1][parentPositions.getData().getPosC() - 1]
                        = new Empty(new Position(parentPositions.getData().getPosR() - 1, parentPositions.getData().getPosC() - 1));
            }
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
    }

    /**
     * Calculates possible captures on the up right.
     *
     * @param board           The using board, must be an 8x8 board.
     * @param parentPositions starting position of the creating tree.
     */
    public void enginePossibleCaptureUpRight(ItalianBoard board, GenericTreeNode<Position> parentPositions) {
        try {
            if ((board.getBoard()[parentPositions.getData().getPosR() - 1][parentPositions.getData().getPosC() + 1].getPlayer() != getPlayer()) &&
                    (board.getBoard()[parentPositions.getData().getPosR() - 1][parentPositions.getData().getPosC() + 1].getType() == PiecesType.MAN ||
                            board.getBoard()[parentPositions.getData().getPosR() - 1][parentPositions.getData().getPosC() + 1].getType() == PiecesType.KING) &&
                    (board.getBoard()[parentPositions.getData().getPosR() - 2][parentPositions.getData().getPosC() + 2].getPlayer() == PiecesColors.EMPTY)) {
                parentPositions.addChild(new GenericTreeNode<>(new Position(
                        parentPositions.getData().getPosR() - 2, parentPositions.getData().getPosC() + 2,
                        parentPositions.getData().getPosR() - 1, parentPositions.getData().getPosC() + 1)));
                board.getBoard()[parentPositions.getData().getPosR() - 1][parentPositions.getData().getPosC() + 1]
                        = new Empty(new Position(parentPositions.getData().getPosR() - 1, parentPositions.getData().getPosC() + 1));
            }
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
    }

    /**
     * Calculates possible captures on the down left.
     *
     * @param board           The using board, must be an 8x8 board.
     * @param parentPositions starting position of the creating tree.
     */
    public void enginePossibleCaptureDownLeft(ItalianBoard board, GenericTreeNode<Position> parentPositions) {
        try {
            if ((board.getBoard()[parentPositions.getData().getPosR() + 1][parentPositions.getData().getPosC() - 1].getPlayer() != getPlayer()) &&
                    (board.getBoard()[parentPositions.getData().getPosR() + 1][parentPositions.getData().getPosC() - 1].getType() == PiecesType.MAN ||
                            board.getBoard()[parentPositions.getData().getPosR() + 1][parentPositions.getData().getPosC() - 1].getType() == PiecesType.KING) &&
                    (board.getBoard()[parentPositions.getData().getPosR() + 2][parentPositions.getData().getPosC() - 2].getPlayer() == PiecesColors.EMPTY)) {

                parentPositions.addChild(new GenericTreeNode<>(new Position(
                        parentPositions.getData().getPosR() + 2, parentPositions.getData().getPosC() - 2,
                        parentPositions.getData().getPosR() + 1, parentPositions.getData().getPosC() - 1)));
                board.getBoard()[parentPositions.getData().getPosR() + 1][parentPositions.getData().getPosC() - 1]
                        = new Empty(new Position(parentPositions.getData().getPosR() + 1, parentPositions.getData().getPosC() - 1));
            }
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
    }

    /**
     * Calculates possible captures on the down right.
     *
     * @param board           The using board, must be an 8x8 board.
     * @param parentPositions starting position of the creating tree.
     */
    public void enginePossibleCaptureDownRight(ItalianBoard board, GenericTreeNode<Position> parentPositions) {
        try {
            if ((board.getBoard()[parentPositions.getData().getPosR() + 1][parentPositions.getData().getPosC() + 1].getPlayer() != getPlayer()) &&
                    (board.getBoard()[parentPositions.getData().getPosR() + 1][parentPositions.getData().getPosC() + 1].getType() == PiecesType.MAN ||
                            board.getBoard()[parentPositions.getData().getPosR() + 1][parentPositions.getData().getPosC() + 1].getType() == PiecesType.KING) &&
                    (board.getBoard()[parentPositions.getData().getPosR() + 2][parentPositions.getData().getPosC() + 2].getPlayer() == PiecesColors.EMPTY)) {

                parentPositions.addChild(new GenericTreeNode<>(new Position(
                        parentPositions.getData().getPosR() + 2, parentPositions.getData().getPosC() + 2,
                        parentPositions.getData().getPosR() + 1, parentPositions.getData().getPosC() + 1)));
                board.getBoard()[parentPositions.getData().getPosR() + 1][parentPositions.getData().getPosC() + 1]
                        = new Empty(new Position(parentPositions.getData().getPosR() + 1, parentPositions.getData().getPosC() + 1));
            }
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
    }
}
