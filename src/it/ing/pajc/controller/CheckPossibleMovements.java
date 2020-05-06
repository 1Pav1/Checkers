package it.ing.pajc.controller;

import it.ing.pajc.data.board.Board;
import it.ing.pajc.data.board.ItalianBoard;
import it.ing.pajc.data.movements.*;
import it.ing.pajc.data.pieces.PlaceType;
import it.ing.pajc.data.pieces.PieceType;
import it.ing.pajc.manager.Player;

import java.util.ArrayList;

public class CheckPossibleMovements {


    public static ArrayList<Position> allPossibleMoves(ItalianBoard board, int posR, int posC) {
        ArrayList<Position> positions;
        if (!canCapture(board, posR, posC)) {
            positions = possibleMovesInEmptySpaces(board, posR, posC);
        } else {
            positions = allPossibleCaptures(board, posR, posC);
        }
        return positions;
    }

    public static boolean canSomebodyCapture(ItalianBoard board, Player player){
        for(int i=0;i<Board.DIMENSION_ITALIAN_BOARD;i++){
            for(int j=0;j<Board.DIMENSION_ITALIAN_BOARD;j++){
                if(PlaceType.confrontPlayer(board.getBoard()[i][j].getPlace(), player) && board.getBoard()[i][j].getPlace()!=PlaceType.EMPTY && canCapture(board, i, j))
                    return true;
            }
        }
        return  false;
    }



    public static ArrayList<Position> allPossibleCaptures(ItalianBoard board, int posR, int posC) {
        ArrayList<Position> captures = new ArrayList<>();
        if (possibleCaptureUpLeft(board, posR, posC) != null) captures.add(possibleCaptureUpLeft(board, posR, posC));
        if (possibleCaptureUpRight(board, posR, posC) != null) captures.add(possibleCaptureUpRight(board, posR, posC));
        if (board.getBoard()[posR][posC].getPiece() == PieceType.KING) {
            if (possibleCaptureDownLeft(board, posR, posC) != null)
                captures.add(possibleCaptureDownLeft(board, posR, posC));
            if (possibleCaptureDownRight(board, posR, posC) != null)
                captures.add(possibleCaptureDownRight(board, posR, posC));
        }
        return captures;
    }


    public static Position possibleCaptureUpLeft(ItalianBoard board, int posR, int posC) {
        try {
            if ((board.getBoard()[posR - 1][posC - 1].getPlace()!=PlaceType.EMPTY) &&
                (board.getBoard()[posR - 1][posC - 1].getPlace() != board.getBoard()[posR][posC].getPlace()) &&
                    (board.getBoard()[posR - 1][posC - 1].getPiece() == PieceType.MAN || board.getBoard()[posR][posC].getPiece()==PieceType.KING) &&
                    (board.getBoard()[posR - 2][posC - 2].getPlace() == PlaceType.EMPTY)) {
                return new Position(posR - 2, posC - 2, posR - 1, posC - 1);
            }
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
        return null;
    }


    public static Position possibleCaptureUpRight(ItalianBoard board, int posR, int posC) {
        try {
            if ((board.getBoard()[posR - 1][posC + 1].getPlace()!=PlaceType.EMPTY) &&
                    (board.getBoard()[posR - 1][posC +1].getPlace() != board.getBoard()[posR][posC].getPlace()) &&
                    (board.getBoard()[posR - 1][posC + 1].getPiece() == PieceType.MAN || board.getBoard()[posR][posC].getPiece()==PieceType.KING) &&
                    (board.getBoard()[posR - 2][posC + 2].getPlace() == PlaceType.EMPTY)){
                return new Position(posR - 2, posC + 2, posR - 1, posC + 1);
            }
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
        return null;
    }


    public static Position possibleCaptureDownLeft(ItalianBoard board, int posR, int posC) {
        try {
            if ((board.getBoard()[posR + 1][posC - 1].getPlace()!=PlaceType.EMPTY) &&
                    (board.getBoard()[posR + 1][posC - 1].getPlace() != board.getBoard()[posR][posC].getPlace()) &&
                    (board.getBoard()[posR + 1][posC - 1].getPiece() == PieceType.MAN ||
                            board.getBoard()[posR + 1][posC - 1].getPiece() == PieceType.KING) &&
                    (board.getBoard()[posR + 2][posC - 2].getPlace() == PlaceType.EMPTY)) {
                return new Position(posR + 2, posC - 2, posR + 1, posC - 1);
            }
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
        return null;
    }


    public static Position possibleCaptureDownRight(ItalianBoard board, int posR, int posC) {
        try {
            if ((board.getBoard()[posR + 1][posC + 1].getPlace()!=PlaceType.EMPTY) &&
                    (board.getBoard()[posR + 1][posC + 1].getPlace() != board.getBoard()[posR][posC].getPlace()) &&
                    (board.getBoard()[posR + 1][posC + 1].getPiece() == PieceType.MAN ||
                            board.getBoard()[posR + 1][posC + 1].getPiece() == PieceType.KING) &&
                    (board.getBoard()[posR + 2][posC + 2].getPlace() == PlaceType.EMPTY)) {
                return new Position(posR + 2, posC + 2, posR + 1, posC + 1);
            }
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
        return null;
    }

    public static boolean canCapture(ItalianBoard board, int posR, int posC) {
        if (canCaptureUpLeft(board, posR, posC) || canCaptureUpRight(board, posR, posC)) {
            System.out.println("Cattura sopra "+posR+" "+posC);
            System.out.println(board.getBoard()[posR][posC].getPlace());
            System.out.println(board.getBoard()[posR][posC].getPiece());
            return true;
        }
        if(board.getBoard()[posR][posC].getPiece()==PieceType.KING && (canCaptureDownLeft(board, posR, posC) || canCaptureDownRight(board, posR, posC))) {
            System.out.println("Cattura sotto "+posR+" "+posC);
            System.out.println(board.getBoard()[posR][posC].getPlace());
            System.out.println(board.getBoard()[posR][posC].getPiece());
            return true;
        }
        return false;
    }

    private static boolean canCaptureUpLeft(ItalianBoard board, int posR, int posC) {
        try {
            if(((board.getBoard()[posR][posC].getPiece()==PieceType.MAN) && (board.getBoard()[posR - 1][posC - 1].getPiece()==PieceType.MAN))|| (board.getBoard()[posR][posC].getPiece()==PieceType.KING) )
            return ((board.getBoard()[posR - 1][posC - 1].getPlace() != board.getBoard()[posR][posC].getPlace()) &&
                    (board.getBoard()[posR - 1][posC - 1].getPlace() != PlaceType.EMPTY) &&
                    (board.getBoard()[posR - 2][posC - 2].getPlace() == PlaceType.EMPTY));
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
        return false;
    }

    private static boolean canCaptureUpRight(ItalianBoard board, int posR, int posC) {
        try {
            if(((board.getBoard()[posR][posC].getPiece()==PieceType.MAN) && (board.getBoard()[posR - 1][posC + 1].getPiece()==PieceType.MAN))|| (board.getBoard()[posR][posC].getPiece()==PieceType.KING) )
                return ((board.getBoard()[posR - 1][posC + 1].getPlace() != board.getBoard()[posR][posC].getPlace()) &&
                    (board.getBoard()[posR - 1][posC + 1].getPlace() != PlaceType.EMPTY) &&
                    (board.getBoard()[posR - 2][posC + 2].getPlace() == PlaceType.EMPTY));
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
        return false;
    }

    private static boolean canCaptureDownLeft(ItalianBoard board, int posR, int posC) {
        try {
            return ((board.getBoard()[posR + 1][posC - 1].getPlace() != board.getBoard()[posR][posC].getPlace()) &&
                    (board.getBoard()[posR + 1][posC - 1].getPlace() != PlaceType.EMPTY) &&
                    (board.getBoard()[posR + 2][posC - 2].getPlace() == PlaceType.EMPTY));
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
        return false;
    }

    private static boolean canCaptureDownRight(ItalianBoard board, int posR, int posC) {
        try {
            return ((board.getBoard()[posR + 1][posC + 1].getPlace() != board.getBoard()[posR][posC].getPlace()) &&
                    (board.getBoard()[posR + 1][posC + 1].getPlace() != PlaceType.EMPTY) &&
                    (board.getBoard()[posR + 2][posC + 2].getPlace() == PlaceType.EMPTY));
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
        return false;
    }


    public static boolean canMove(ItalianBoard board, int posR, int posC) {
        try {
            return ((board.getBoard()[posR - 1][posC - 1].getPlace() == PlaceType.EMPTY) ||
                    (board.getBoard()[posR - 1][posC + 1].getPlace() == PlaceType.EMPTY) ||
                    (board.getBoard()[posR + 1][posC - 1].getPlace() == PlaceType.EMPTY) ||
                    (board.getBoard()[posR + 1][posC + 1].getPlace() == PlaceType.EMPTY));
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
        return false;
    }


    public static ArrayList<Position> possibleMovesInEmptySpaces(ItalianBoard board, int posR, int posC) {
        ArrayList<Position> possibleMovementList = new ArrayList<>();

        try {
            if (board.getBoard()[posR - 1][posC - 1].getPlace() == PlaceType.EMPTY)//up left
                possibleMovementList.add(new Position(posR - 1, posC - 1));
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }

        try {
            if (board.getBoard()[posR - 1][posC + 1].getPlace() == PlaceType.EMPTY)//up right
                possibleMovementList.add(new Position(posR - 1, posC + 1));
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }

        if (board.getBoard()[posR][posC].getPiece() == PieceType.KING) {
            try {
                if (board.getBoard()[posR + 1][posC - 1].getPlace() == PlaceType.EMPTY)//down left
                    possibleMovementList.add(new Position(posR + 1, posC - 1));
            } catch (ArrayIndexOutOfBoundsException ignored) {
            }

            try {
                if (board.getBoard()[posR + 1][posC + 1].getPlace() == PlaceType.EMPTY)//down right
                    possibleMovementList.add(new Position(posR + 1, posC + 1));
            } catch (ArrayIndexOutOfBoundsException ignored) {
            }
        }
        return possibleMovementList;
    }

}
