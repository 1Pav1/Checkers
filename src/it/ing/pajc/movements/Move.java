package it.ing.pajc.movements;

import it.ing.pajc.data.board.Board;
import it.ing.pajc.data.board.ItalianBoard;
import it.ing.pajc.data.pieces.PieceType;
import it.ing.pajc.data.pieces.PlaceType;
import it.ing.pajc.manager.Player;

public class Move {

    public static void executeMove(Position init, Position fin, ItalianBoard board) {
        PlaceType place = board.getBoard()[init.getPosR()][init.getPosC()].getPlace();
        if (CheckPossibleMovements.canCapture(board, init.getPosR(),init.getPosC()))
            deleteCaptured(fin, board);
        if (checkKingTransformation(fin) || (board.getBoard()[init.getPosR()][init.getPosC()]).getPiece() == PieceType.KING)
            board.getBoard()[fin.getPosR()][fin.getPosC()].setPiece(PieceType.KING);
        else
            board.getBoard()[fin.getPosR()][fin.getPosC()].setPiece(PieceType.MAN);

        board.getBoard()[fin.getPosR()][fin.getPosC()].setPlace(place);
        delete(init, board);
    }


    public static boolean canSomebodyDoSomething(ItalianBoard board, Player player){
        for(int i=0;i< Board.DIMENSION_ITALIAN_BOARD;i++)
            for(int j=0;j< Board.DIMENSION_ITALIAN_BOARD;j++)
                if(PlaceType.confrontPlayer(board.getBoard()[i][j].getPlace(), player) && (!CheckPossibleMovements.allPossibleMoves(board,i,j).isEmpty()))
                    return true;
        return  false;
    }



    private static boolean checkKingTransformation(Position pos) {
        return pos.getPosR() == 0;
    }



    private static void delete(Position position, ItalianBoard board) {
        board.getBoard()[position.getPosR()][position.getPosC()].setPlace(PlaceType.EMPTY);
        board.getBoard()[position.getPosR()][position.getPosC()].setPiece(null);
    }

    private static void deleteCaptured(Position position, ItalianBoard board) {
        board.getBoard()[position.getcPosR()][position.getcPosC()].setPlace(PlaceType.EMPTY);
        board.getBoard()[position.getcPosR()][position.getcPosC()].setPiece(null);
    }
}
