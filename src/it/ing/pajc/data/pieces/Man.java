package it.ing.pajc.data.pieces;

import it.ing.pajc.data.board.InternationalBoard;
import it.ing.pajc.data.board.ItalianBoard;
import it.ing.pajc.data.movements.*;

import java.util.ArrayList;

public class Man extends Pieces {
    private GenericTreeNode<Position> root = new GenericTreeNode<>(this.getPosition());
    private GenericTree<Position> possibleMovementsList = new GenericTree<>();

    public Man(PiecesColors player, Position pos) {
        super(player, pos);
        possibleMovementsList.setRoot(root);

    }
    //TO USE
    public GenericTree possibleMoves(ItalianBoard board) {
        if (!canCapture(board, this.getPosition())) {
            ArrayList<Position> positions = possibleMovesInEmptySpaces(board);
            for (int i = 0; i < positions.size(); i++)
                root.addChild(new GenericTreeNode<>(positions.get(i)));
            return possibleMovementsList;
        }
        else{
            allPossibleCaptures(board);//to modify, it doesn't take in consideration of rules

        }
        return possibleMovementsList;
    }


    public void possibleMoves(InternationalBoard board) {
    }

    public void bestCaptures(ItalianBoard board){
        allPossibleCaptures(board);
        possibleMovementsList.getNumberOfNodes();


    }
    private void allPossibleCaptures(ItalianBoard board) {
        possibleCapturesUpRightAndLeft(board, root);
        childrenPossibleCaptures(board, root);
    }

    private void childrenPossibleCaptures(ItalianBoard board, GenericTreeNode<Position> parent) {
        for (int i = 0; i < parent.getNumberOfChildren(); i++) {
            possibleCapturesUpRightAndLeft(board, parent.getChildAt(i));
            if (canCapture(board, parent.getChildAt(i).getData()))
                childrenPossibleCaptures(board, parent.getChildAt(i));
        }
    }

    private void possibleCapturesUpRightAndLeft(ItalianBoard board, GenericTreeNode<Position> parent) {
        possibleCaptureUpLeft(board, parent);
        possibleCaptureUpRight(board, parent);
    }

    private void possibleCaptureUpRight(ItalianBoard board, GenericTreeNode<Position> parent) {
        if (board.getBoard()[parent.getData().getPosR()][parent.getData().getPosC()].getPlayer() == PiecesColors.WHITE)
            possibleWhiteCaptureUpRight(board, parent);
        else if (board.getBoard()[parent.getData().getPosR()][parent.getData().getPosC()].getPlayer() == PiecesColors.BLACK)
            possibleBlackCaptureUpRight(board, parent);

    }

    private void possibleCaptureUpLeft(ItalianBoard board, GenericTreeNode<Position> parent) {
        if (board.getBoard()[parent.getData().getPosR()][parent.getData().getPosC()].getPlayer() == PiecesColors.WHITE)
            possibleWhiteCaptureUpLeft(board, parent);
        else if (board.getBoard()[parent.getData().getPosR()][parent.getData().getPosC()].getPlayer() == PiecesColors.BLACK)
            possibleBlackCaptureUpLeft(board, parent);
    }

    private void possibleWhiteCaptureUpLeft(ItalianBoard board, GenericTreeNode<Position> parent) {
        if ((board.getBoard()[parent.getData().getPosR() - 1][parent.getData().getPosC() - 1].getPlayer() == PiecesColors.BLACK) &&
                (board.getBoard()[parent.getData().getPosR() - 2][parent.getData().getPosC() - 2].getPlayer() == PiecesColors.EMPTY)) {
            parent.addChild(new GenericTreeNode<>(new Position(parent.getData().getPosR() - 2, parent.getData().getPosC() - 2)));
            //board.getBoard()[parent.getData().getPosR()-1][parent.getData().getPosC()-1].setPlayer(PiecesColors.EMPTY);
        }
    }

    private void possibleWhiteCaptureUpRight(ItalianBoard board, GenericTreeNode<Position> parent) {
        if ((board.getBoard()[parent.getData().getPosR() - 1][parent.getData().getPosC() + 1].getPlayer() == PiecesColors.BLACK) &&
                (board.getBoard()[parent.getData().getPosR() - 2][parent.getData().getPosC() + 2].getPlayer() == PiecesColors.EMPTY)) {
            parent.addChild(new GenericTreeNode<>(new Position(parent.getData().getPosR() - 2, parent.getData().getPosC() + 2)));
        }
    }

    private void possibleBlackCaptureUpLeft(ItalianBoard board, GenericTreeNode<Position> parent) {
        if ((board.getBoard()[parent.getData().getPosR() + 1][parent.getData().getPosC() + 1].getPlayer() == PiecesColors.WHITE) &&
                (board.getBoard()[parent.getData().getPosR() + 2][parent.getData().getPosC() + 2].getPlayer() == PiecesColors.EMPTY)) {
            parent.addChild(new GenericTreeNode<>(new Position(parent.getData().getPosR() + 2, parent.getData().getPosC() + 2)));
        }
    }

    private void possibleBlackCaptureUpRight(ItalianBoard board, GenericTreeNode<Position> parent) {
        if ((board.getBoard()[parent.getData().getPosR() + 1][parent.getData().getPosC() - 1].getPlayer() == PiecesColors.WHITE) &&
                (board.getBoard()[parent.getData().getPosR() + 2][parent.getData().getPosC() - 2].getPlayer() == PiecesColors.EMPTY)) {
            parent.addChild(new GenericTreeNode<>(new Position(parent.getData().getPosR() + 2, parent.getData().getPosC() - 2)));
        }
    }

    private boolean canCapture(ItalianBoard board, Position piece) {
        if (board.getBoard()[piece.getPosR()][piece.getPosC()].getPlayer() == PiecesColors.WHITE) {
            return ((board.getBoard()[piece.getPosR() - 1][piece.getPosC() - 1].getPlayer() == PiecesColors.BLACK) &&
                    (board.getBoard()[piece.getPosR() - 2][piece.getPosC() - 2].getPlayer() == PiecesColors.EMPTY)) ||
                    ((board.getBoard()[piece.getPosR() - 1][piece.getPosC() + 1].getPlayer() == PiecesColors.BLACK) &&
                            (board.getBoard()[piece.getPosR() - 2][piece.getPosC() + 2].getPlayer() == PiecesColors.EMPTY));
        } else if (board.getBoard()[piece.getPosR()][piece.getPosC()].getPlayer() == PiecesColors.BLACK) {
            return ((board.getBoard()[piece.getPosR() + 1][piece.getPosC() + 1].getPlayer() == PiecesColors.WHITE) &&
                    (board.getBoard()[piece.getPosR() + 2][piece.getPosC() + 2].getPlayer() == PiecesColors.EMPTY)) ||
                    ((board.getBoard()[piece.getPosR() + 1][piece.getPosC() - 1].getPlayer() == PiecesColors.WHITE) &&
                            (board.getBoard()[piece.getPosR() + 2][piece.getPosC() - 2].getPlayer() == PiecesColors.EMPTY));
        }
        return false;
    }

    private ArrayList<Position> possibleMovesInEmptySpaces(ItalianBoard board) {
        ArrayList<Position> possibleMovementList;

        if (getPlayer() == PiecesColors.WHITE) {
            possibleMovementList = possibleWhiteMovesInEmptySpaces(board);
        } else {
            possibleMovementList = possibleBlackMovesInEmptySpaces(board);
        }
        return possibleMovementList;
    }

    private ArrayList<Position> possibleWhiteMovesInEmptySpaces(ItalianBoard board) {
        ArrayList<Position> possibleMovementList = new ArrayList<>();

        int posRow = this.getPosition().getPosR();
        int posColumn = this.getPosition().getPosC();
        try {
            if (board.getBoard()[posRow - 1][posColumn - 1].getPlayer() == PiecesColors.EMPTY)//up left
                possibleMovementList.add(new Position(posRow - 1, posColumn - 1));

            if (board.getBoard()[posRow - 1][posColumn + 1].getPlayer() == PiecesColors.EMPTY)//up right
                possibleMovementList.add(new Position(posRow - 1, posColumn + 1));
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
        return possibleMovementList;
    }

    private ArrayList<Position> possibleBlackMovesInEmptySpaces(ItalianBoard board) {
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


