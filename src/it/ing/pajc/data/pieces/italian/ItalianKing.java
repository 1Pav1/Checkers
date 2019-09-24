package it.ing.pajc.data.pieces.italian;

import it.ing.pajc.data.board.ItalianBoard;
import it.ing.pajc.data.movements.*;
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
    private GenericTreeNode<Position> rootCaptures = new GenericTreeNode<>(this.getPosition());
    private GenericTree<Position> possibleCapturesList = new GenericTree<>();

    /**
     * WhiteKing's constructor giving position.
     *
     * @param pos WhiteKing's position.
     */
    public ItalianKing(Position pos, PiecesColors player) {
        super(pos);
        this.setPlayer(player);
        possibleMovementsList.setRoot(rootPositions);
        possibleCapturesList.setRoot(rootCaptures);
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
     * Gives all possible captures of a piece.
     *
     * @param board The using board, must be an 8x8 board.
     * @return the tree of all possible captures.
     */
    @Override
    public GenericTree<Position> possibleCaptures(ItalianBoard board) {
        rootCaptures.removeChildren();
        try {
            if (canCapture(board, this.getPosition())) {
                allPossibleCaptures(board);
            }
        } catch (Exception ignored) {
        }
        return possibleCapturesList;
    }

    /**
     * Calculates all possible captures of a piece, included multiple captures.
     *
     * @param board The using board, must be an 8x8 board.
     */
    @Override
    public void allPossibleCaptures(ItalianBoard board) {
        possibleCapturesAllDirections(board, rootPositions, rootCaptures);
        childrenPossibleCaptures(board, rootPositions, rootCaptures);
    }

    /**
     * Calculates possible captures after having captured already a piece.
     *
     * @param board           The using board, must be an 8x8 board.
     * @param parentPositions starting position of the creating tree.
     * @param parentCaptures  starting position of the creating tree.
     */
    @Override
    public void childrenPossibleCaptures(ItalianBoard board, GenericTreeNode<Position> parentPositions, GenericTreeNode<Position> parentCaptures) {
        for (int i = 0; i < parentPositions.getNumberOfChildren(); i++) {
            possibleCapturesAllDirections(board, parentPositions.getChildAt(i), parentCaptures.getChildAt(i));
            if (canCapture(board, parentPositions.getChildAt(i).getData()))
                childrenPossibleCaptures(board, parentPositions.getChildAt(i), parentCaptures.getChildAt(i));
        }
    }

    /**
     * Calculates all possible captures in all directions where the piece can move.
     *
     * @param board           The using board, must be an 8x8 board.
     * @param parentPositions starting position of the creating tree.
     * @param parentCaptures  starting position of the creating tree.
     */
    @Override
    public void possibleCapturesAllDirections(ItalianBoard board, GenericTreeNode<Position> parentPositions, GenericTreeNode<Position> parentCaptures) {
        possibleCaptureUpLeft(board, parentPositions, parentCaptures);
        possibleCaptureUpRight(board, parentPositions, parentCaptures);
        possibleCaptureDownLeft(board, parentPositions, parentCaptures);
        possibleCaptureDownRight(board, parentPositions, parentCaptures);
    }

    /**
     * Calculates possible captures on the up left.
     *
     * @param board           The using board, must be an 8x8 board.
     * @param parentPositions starting position of the creating tree.
     * @param parentCaptures  starting position of the creating tree.
     */
    @Override
    public void possibleCaptureUpLeft(ItalianBoard board, GenericTreeNode<Position> parentPositions, GenericTreeNode<Position> parentCaptures) {
        try {
            if ((board.getBoard()[parentPositions.getData().getPosR() - 1][parentPositions.getData().getPosC() - 1].getPlayer() != getPlayer()) &&
                    (board.getBoard()[parentPositions.getData().getPosR() - 1][parentPositions.getData().getPosC() - 1].getType() == PiecesType.MAN ||
                            board.getBoard()[parentPositions.getData().getPosR() - 1][parentPositions.getData().getPosC() - 1].getType() == PiecesType.KING) &&
                    (board.getBoard()[parentPositions.getData().getPosR() - 2][parentPositions.getData().getPosC() - 2].getPlayer() == PiecesColors.EMPTY)) {
                parentPositions.addChild(new GenericTreeNode<>(new Position(parentPositions.getData().getPosR() - 2, parentPositions.getData().getPosC() - 2)));
                parentCaptures.addChild(new GenericTreeNode<>(new Position(parentPositions.getData().getPosR() - 1, parentPositions.getData().getPosC() - 1)));
            }
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
    }

    /**
     * Calculates possible captures on the up right.
     *
     * @param board           The using board, must be an 8x8 board.
     * @param parentPositions starting position of the creating tree.
     * @param parentCaptures  starting position of the creating tree.
     */
    @Override
    public void possibleCaptureUpRight(ItalianBoard board, GenericTreeNode<Position> parentPositions, GenericTreeNode<Position> parentCaptures) {
        try {
            if ((board.getBoard()[parentPositions.getData().getPosR() - 1][parentPositions.getData().getPosC() + 1].getPlayer() != getPlayer()) &&
                    (board.getBoard()[parentPositions.getData().getPosR() - 1][parentPositions.getData().getPosC() + 1].getType() == PiecesType.MAN ||
                            board.getBoard()[parentPositions.getData().getPosR() - 1][parentPositions.getData().getPosC() + 1].getType() == PiecesType.KING) &&
                    (board.getBoard()[parentPositions.getData().getPosR() - 2][parentPositions.getData().getPosC() + 2].getPlayer() == PiecesColors.EMPTY)) {
                parentPositions.addChild(new GenericTreeNode<>(new Position(parentPositions.getData().getPosR() - 2, parentPositions.getData().getPosC() + 2)));
                parentCaptures.addChild(new GenericTreeNode<>(new Position(parentPositions.getData().getPosR() - 1, parentPositions.getData().getPosC() + 1)));
            }
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
    }

    /**
     * Calculates possible captures on the down left.
     *
     * @param board           The using board, must be an 8x8 board.
     * @param parentPositions starting position of the creating tree.
     * @param parentCaptures  starting position of the creating tree.
     */
    @Override
    public void possibleCaptureDownLeft(ItalianBoard board, GenericTreeNode<Position> parentPositions, GenericTreeNode<Position> parentCaptures) {
        try {
            if ((board.getBoard()[parentPositions.getData().getPosR() + 1][parentPositions.getData().getPosC() - 1].getPlayer() != getPlayer()) &&
                    (board.getBoard()[parentPositions.getData().getPosR() + 1][parentPositions.getData().getPosC() - 1].getType() == PiecesType.MAN ||
                            board.getBoard()[parentPositions.getData().getPosR() + 1][parentPositions.getData().getPosC() - 1].getType() == PiecesType.KING) &&
                    (board.getBoard()[parentPositions.getData().getPosR() + 2][parentPositions.getData().getPosC() - 2].getPlayer() == PiecesColors.EMPTY)) {
                parentPositions.addChild(new GenericTreeNode<>(new Position(parentPositions.getData().getPosR() + 2, parentPositions.getData().getPosC() - 2)));
                parentCaptures.addChild(new GenericTreeNode<>(new Position(parentPositions.getData().getPosR() + 1, parentPositions.getData().getPosC() - 1)));
            }
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
    }

    /**
     * Calculates possible captures on the down right.
     *
     * @param board           The using board, must be an 8x8 board.
     * @param parentPositions starting position of the creating tree.
     * @param parentCaptures  starting position of the creating tree.
     */
    @Override
    public void possibleCaptureDownRight(ItalianBoard board, GenericTreeNode<Position> parentPositions, GenericTreeNode<Position> parentCaptures) {
        try {
            if ((board.getBoard()[parentPositions.getData().getPosR() + 1][parentPositions.getData().getPosC() + 1].getPlayer() != getPlayer()) &&
                    (board.getBoard()[parentPositions.getData().getPosR() + 1][parentPositions.getData().getPosC() + 1].getType() == PiecesType.MAN ||
                            board.getBoard()[parentPositions.getData().getPosR() + 1][parentPositions.getData().getPosC() + 1].getType() == PiecesType.KING) &&
                    (board.getBoard()[parentPositions.getData().getPosR() + 2][parentPositions.getData().getPosC() + 2].getPlayer() == PiecesColors.EMPTY)) {
                parentPositions.addChild(new GenericTreeNode<>(new Position(parentPositions.getData().getPosR() + 2, parentPositions.getData().getPosC() + 2)));
                parentCaptures.addChild(new GenericTreeNode<>(new Position(parentPositions.getData().getPosR() + 1, parentPositions.getData().getPosC() + 1)));
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
        try {
            return ((board.getBoard()[piece.getPosR() - 1][piece.getPosC() - 1].getPlayer() != getPlayer()) &&
                    (board.getBoard()[piece.getPosR() - 1][piece.getPosC() - 1].getPlayer() != PiecesColors.EMPTY) &&
                    (board.getBoard()[piece.getPosR() - 2][piece.getPosC() - 2].getPlayer() == PiecesColors.EMPTY));
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }

        try {
            return ((board.getBoard()[piece.getPosR() - 1][piece.getPosC() + 1].getPlayer() != getPlayer()) &&
                    (board.getBoard()[piece.getPosR() - 1][piece.getPosC() - 1].getPlayer() != PiecesColors.EMPTY) &&
                    (board.getBoard()[piece.getPosR() - 2][piece.getPosC() - 2].getPlayer() == PiecesColors.EMPTY));
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }

        try {
            return ((board.getBoard()[piece.getPosR() + 1][piece.getPosC() - 1].getPlayer() != getPlayer()) &&
                            (board.getBoard()[piece.getPosR() - 1][piece.getPosC() - 1].getPlayer() != PiecesColors.EMPTY) &&
                            (board.getBoard()[piece.getPosR() - 2][piece.getPosC() - 2].getPlayer() == PiecesColors.EMPTY));
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }

        try {
            return ((board.getBoard()[piece.getPosR() + 1][piece.getPosC() + 1].getPlayer() != getPlayer()) &&
                            (board.getBoard()[piece.getPosR() - 1][piece.getPosC() - 1].getPlayer() != PiecesColors.EMPTY) &&
                            (board.getBoard()[piece.getPosR() - 2][piece.getPosC() - 2].getPlayer() == PiecesColors.EMPTY));
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
            return ((board.getBoard()[piece.getPosR() - 1][piece.getPosC() - 1].getPlayer() == PiecesColors.EMPTY) &&
                    (board.getBoard()[piece.getPosR() - 1][piece.getPosC() + 1].getPlayer() == PiecesColors.EMPTY) &&
                    (board.getBoard()[piece.getPosR() + 1][piece.getPosC() - 1].getPlayer() == PiecesColors.EMPTY) &&
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
}
