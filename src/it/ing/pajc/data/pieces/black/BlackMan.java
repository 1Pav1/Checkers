package it.ing.pajc.data.pieces.black;

import it.ing.pajc.data.board.InternationalBoard;
import it.ing.pajc.data.board.ItalianBoard;
import it.ing.pajc.data.movements.GenericTree;
import it.ing.pajc.data.movements.GenericTreeNode;
import it.ing.pajc.data.movements.Position;
import it.ing.pajc.data.pieces.Man;
import it.ing.pajc.data.pieces.PiecesColors;


import java.util.ArrayList;

/**
 * BlackMan class defines black man pieces with all implemented methods.
 */
public class BlackMan extends Man {
    private GenericTreeNode<Position> root = new GenericTreeNode<>(this.getPosition());
    private GenericTree<Position> possibleMovementsList = new GenericTree<>();

    /**
     * BlackMan's constructor giving position.
     *
     * @param pos BlackMan's position
     */
    public BlackMan(Position pos) {
        super(pos);
        this.setPlayer(PiecesColors.BLACK);
        possibleMovementsList.setRoot(root);
    }

    /**
     * Gives all possible moves of a piece.
     *
     * @param board The using board, must be an 8x8 board.
     * @return the tree of all possible moves
     */
    @Override
    public GenericTree possibleMoves(ItalianBoard board) {
        root.removeChildren();
        if (!canCapture(board, this.getPosition())) {
            ArrayList<Position> positions = possibleMovesInEmptySpaces(board);
            for (int i = 0; i < positions.size(); i++)
                root.addChild(new GenericTreeNode<>(positions.get(i)));
            return possibleMovementsList;
        } else {
            allPossibleCaptures(board);//to modify, it doesn't take in consideration of rules
        }
        return possibleMovementsList;
    }

    /**
     * Gives all possible moves of a piece without taking care about legal moves.
     *
     * @param board The using board, must be a 10x10 board.
     */
    @Override
    public void possibleMoves(InternationalBoard board) {
    }

    /**
     * Gives only the legal moves of a piece.
     *
     * @param board The using board, must be an 8x8 board.
     * @return the tree of legal moves
     */
    @Override
    public GenericTree<Position> bestCaptures(ItalianBoard board) {
        allPossibleCaptures(board);
        possibleMovementsList.getNumberOfNodes();
        return possibleMovementsList;
    }

    /**
     * Calculates all possible captures of a piece, included multiple captures.
     *
     * @param board The using board, must be an 8x8 board.
     */
    @Override
    public void allPossibleCaptures(ItalianBoard board) {
        possibleCapturesUpRightAndLeft(board, root);
        childrenPossibleCaptures(board, root);

    }

    /**
     * Calculates possible captures after having captured already a piece.
     *
     * @param board  The using board, must be an 8x8 board.
     * @param parent starting position of the creating tree
     */
    @Override
    public void childrenPossibleCaptures(ItalianBoard board, GenericTreeNode<Position> parent) {
        for (int i = 0; i < parent.getNumberOfChildren(); i++) {
            possibleCapturesUpRightAndLeft(board, parent.getChildAt(i));
            if (canCapture(board, parent.getChildAt(i).getData()))
                childrenPossibleCaptures(board, parent.getChildAt(i));
        }
    }

    /**
     * Calculates all possible captures in all directions where the piece can move.
     *
     * @param board  The using board, must be an 8x8 board.
     * @param parent starting position of the creating tree
     */
    @Override
    public void possibleCapturesUpRightAndLeft(ItalianBoard board, GenericTreeNode<Position> parent) {
        possibleCaptureUpLeft(board, parent);
        possibleCaptureUpRight(board, parent);
    }

    /**
     * Calculates possible captures on the left.
     *
     * @param board  The using board, must be an 8x8 board.
     * @param parent starting position of the creating tree
     */
    @Override
    public void possibleCaptureUpLeft(ItalianBoard board, GenericTreeNode<Position> parent) {
        try {
            if ((board.getBoard()[parent.getData().getPosR() - 1][parent.getData().getPosC() - 1].getPlayer() == PiecesColors.WHITE) &&
                    (board.getBoard()[parent.getData().getPosR() - 2][parent.getData().getPosC() - 2].getPlayer() == PiecesColors.EMPTY)) {
                parent.addChild(new GenericTreeNode<>(new Position(parent.getData().getPosR() - 2, parent.getData().getPosC() - 2)));
                //board.getBoard()[parent.getData().getPosR()-1][parent.getData().getPosC()-1].setPlayer(PiecesColors.EMPTY);
            }
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
    }

    /**
     * Calculates possible captures on the right.
     *
     * @param board  The using board, must be an 8x8 board.
     * @param parent starting position of the creating tree
     */
    @Override
    public void possibleCaptureUpRight(ItalianBoard board, GenericTreeNode<Position> parent) {
        try {
            if ((board.getBoard()[parent.getData().getPosR() - 1][parent.getData().getPosC() + 1].getPlayer() == PiecesColors.WHITE) &&
                    (board.getBoard()[parent.getData().getPosR() - 2][parent.getData().getPosC() + 2].getPlayer() == PiecesColors.EMPTY)) {
                parent.addChild(new GenericTreeNode<>(new Position(parent.getData().getPosR() - 2, parent.getData().getPosC() + 2)));
            }
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
    }

    /**
     * Check if the piece can capture at least a piece.
     *
     * @param board The using board, must be an 8x8 board.
     * @param piece Position of the piece in question
     * @return a boolean true if can capture, false otherwise
     */
    @Override
    public boolean canCapture(ItalianBoard board, Position piece) {
        try {
            return ((board.getBoard()[piece.getPosR() - 1][piece.getPosC() - 1].getPlayer() == PiecesColors.WHITE) &&
                    (board.getBoard()[piece.getPosR() - 2][piece.getPosC() - 2].getPlayer() == PiecesColors.EMPTY)) ||
                    ((board.getBoard()[piece.getPosR() - 1][piece.getPosC() + 1].getPlayer() == PiecesColors.WHITE) &&
                            (board.getBoard()[piece.getPosR() - 2][piece.getPosC() + 2].getPlayer() == PiecesColors.EMPTY));
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