package it.ing.pajc.data.pieces;

import it.ing.pajc.data.board.InternationalBoard;
import it.ing.pajc.data.board.ItalianBoard;
import it.ing.pajc.data.movements.MovementList;
import it.ing.pajc.data.movements.Position;
import it.ing.pajc.data.movements.TreeNode;

import java.util.ArrayList;

public class Man extends Pieces {
    public Man(PiecesColors player, Position pos) {
        super(player, pos);
    }

    public MovementList possibleMoves(ItalianBoard board) {
        MovementList possibleMovementList = new MovementList(this.getPosition());


        return possibleMovementList;
    }


    public void possibleMoves(InternationalBoard board) {

    }

    public void possibleCapturesUpRightAndLeft(ItalianBoard board) {
        TreeNode<Position> possibleCaptures = new TreeNode<>(this.getPosition());
        possibleCaptureUpLeft(board, possibleCaptures);
        possibleCaptureUpRight(board, possibleCaptures);
    }

    private void possibleCaptureUpRight(ItalianBoard board, TreeNode<Position> parent) {
        if (board.getBoard()[parent.getData().getPosR()][parent.getData().getPosC()].getPlayer() == PiecesColors.WHITE)
            possibleWhiteCaptureUpRight(board, parent);
        else
            possibleBlackCaptureUpRight(board, parent);

    }

    private void possibleCaptureUpLeft(ItalianBoard board, TreeNode<Position> parent) {
        if (board.getBoard()[parent.getData().getPosR()][parent.getData().getPosC()].getPlayer() == PiecesColors.WHITE)
            possibleWhiteCaptureUpLeft(board, parent);
        else
            possibleBlackCaptureUpLeft(board, parent);


    }

    private void possibleWhiteCaptureUpLeft(ItalianBoard board, TreeNode<Position> parent) {
        if ((board.getBoard()[parent.getData().getPosR() - 1][parent.getData().getPosC() - 1].getPlayer() == PiecesColors.BLACK) &&
                (board.getBoard()[parent.getData().getPosR() - 2][parent.getData().getPosC() - 2].getPlayer() == PiecesColors.EMPTY)) {
            parent.addNewChild(new Position(parent.getData().getPosR() - 2, parent.getData().getPosC() - 2));
            //board.getBoard()[parent.getData().getPosR()-1][parent.getData().getPosC()-1].setPlayer(PiecesColors.EMPTY);
        }
    }

    private void possibleWhiteCaptureUpRight(ItalianBoard board, TreeNode<Position> parent) {
        if ((board.getBoard()[parent.getData().getPosR() - 1][parent.getData().getPosC() + 1].getPlayer() == PiecesColors.BLACK) &&
                (board.getBoard()[parent.getData().getPosR() - 2][parent.getData().getPosC() + 2].getPlayer() == PiecesColors.EMPTY)) {
            parent.addNewChild(new Position(parent.getData().getPosR() - 2, parent.getData().getPosC() + 2));
        }
    }

    private void possibleBlackCaptureUpLeft(ItalianBoard board, TreeNode<Position> parent) {
        if ((board.getBoard()[parent.getData().getPosR() + 1][parent.getData().getPosC() + 1].getPlayer() == PiecesColors.WHITE) &&
                (board.getBoard()[parent.getData().getPosR() + 2][parent.getData().getPosC() + 2].getPlayer() == PiecesColors.EMPTY)) {
            parent.addNewChild(new Position(parent.getData().getPosR() + 2, parent.getData().getPosC() + 2));
        }
    }

    private void possibleBlackCaptureUpRight(ItalianBoard board, TreeNode<Position> parent) {
        if ((board.getBoard()[parent.getData().getPosR() + 1][parent.getData().getPosC() - 1].getPlayer() == PiecesColors.WHITE) &&
                (board.getBoard()[parent.getData().getPosR() + 2][parent.getData().getPosC() - 2].getPlayer() == PiecesColors.EMPTY)) {
            parent.addNewChild(new Position(parent.getData().getPosR() + 2, parent.getData().getPosC() - 2));
        }
    }

    public ArrayList<Position> possibleMovesInEmptySpaces(ItalianBoard board) {
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


