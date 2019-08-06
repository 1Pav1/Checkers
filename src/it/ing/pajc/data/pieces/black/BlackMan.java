package it.ing.pajc.data.pieces.black;

import it.ing.pajc.data.board.InternationalBoard;
import it.ing.pajc.data.board.ItalianBoard;
import it.ing.pajc.data.movements.GenericTree;
import it.ing.pajc.data.movements.GenericTreeNode;
import it.ing.pajc.data.movements.Position;
import it.ing.pajc.data.pieces.Man;
import it.ing.pajc.data.pieces.PiecesColors;

import java.util.ArrayList;

public class BlackMan extends Man {
    private GenericTreeNode<Position> root = new GenericTreeNode<>(this.getPosition());
    private GenericTree<Position> possibleMovementsList = new GenericTree<>();

    public BlackMan(Position pos) {
        super(pos);
        this.setPlayer(PiecesColors.BLACK);
        possibleMovementsList.setRoot(root);
    }

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

    public void possibleMoves(InternationalBoard board) {
    }

    public GenericTree<Position> bestCaptures(ItalianBoard board) {
        allPossibleCaptures(board);
        possibleMovementsList.getNumberOfNodes();
        return possibleMovementsList;
    }

    public void allPossibleCaptures(ItalianBoard board) {
        possibleCapturesUpRightAndLeft(board, root);
        childrenPossibleCaptures(board, root);
    }

    public void childrenPossibleCaptures(ItalianBoard board, GenericTreeNode<Position> parent) {
        for (int i = 0; i < parent.getNumberOfChildren(); i++) {
            possibleCapturesUpRightAndLeft(board, parent.getChildAt(i));
            if (canCapture(board, parent.getChildAt(i).getData()))
                childrenPossibleCaptures(board, parent.getChildAt(i));
        }
    }

    public void possibleCapturesUpRightAndLeft(ItalianBoard board, GenericTreeNode<Position> parent) {
        possibleCaptureUpLeft(board, parent);
        possibleCaptureUpRight(board, parent);
    }

    public void possibleCaptureUpLeft(ItalianBoard board, GenericTreeNode<Position> parent) {
        try {
            if ((board.getBoard()[parent.getData().getPosR() + 1][parent.getData().getPosC() + 1].getPlayer() == PiecesColors.WHITE) &&
                    (board.getBoard()[parent.getData().getPosR() + 2][parent.getData().getPosC() + 2].getPlayer() == PiecesColors.EMPTY)) {
                parent.addChild(new GenericTreeNode<>(new Position(parent.getData().getPosR() + 2, parent.getData().getPosC() + 2)));
            }
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
    }

    public void possibleCaptureUpRight(ItalianBoard board, GenericTreeNode<Position> parent) {
        try {
            if ((board.getBoard()[parent.getData().getPosR() + 1][parent.getData().getPosC() - 1].getPlayer() == PiecesColors.WHITE) &&
                    (board.getBoard()[parent.getData().getPosR() + 2][parent.getData().getPosC() - 2].getPlayer() == PiecesColors.EMPTY)) {
                parent.addChild(new GenericTreeNode<>(new Position(parent.getData().getPosR() + 2, parent.getData().getPosC() - 2)));
            }
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
    }

    public boolean canCapture(ItalianBoard board, Position piece) {
        try {
            return ((board.getBoard()[piece.getPosR() + 1][piece.getPosC() + 1].getPlayer() == PiecesColors.WHITE) &&
                    (board.getBoard()[piece.getPosR() + 2][piece.getPosC() + 2].getPlayer() == PiecesColors.EMPTY)) ||
                    ((board.getBoard()[piece.getPosR() + 1][piece.getPosC() - 1].getPlayer() == PiecesColors.WHITE) &&
                            (board.getBoard()[piece.getPosR() + 2][piece.getPosC() - 2].getPlayer() == PiecesColors.EMPTY));
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
        return false;
    }

    public ArrayList<Position> possibleMovesInEmptySpaces(ItalianBoard board) {
        ArrayList<Position> possibleMovementList = new ArrayList<>();

        int posRow = this.getPosition().getPosR();
        int posColumn = this.getPosition().getPosC();
        try {
            if (board.getBoard()[posRow + 1][posColumn + 1].getPlayer() == PiecesColors.EMPTY)//up left
                possibleMovementList.add(new Position(posRow + 1, posColumn + 1));

            if (board.getBoard()[posRow + 1][posColumn - 1].getPlayer() == PiecesColors.EMPTY)//up right
                possibleMovementList.add(new Position(posRow + 1, posColumn - 1));
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
        return possibleMovementList;
    }
}
