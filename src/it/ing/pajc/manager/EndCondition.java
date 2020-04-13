package it.ing.pajc.manager;

import it.ing.pajc.data.board.Board;
import it.ing.pajc.data.board.ItalianBoard;
import it.ing.pajc.data.pieces.PlaceType;

//TODO FINISCI I METODI CHE VEDI SONO OVVI E POI I BUG SEMBRANO SPARITI L'ULTIMO ERA IN CHECKPOSSIBLECAPTURES IN MOVIMENTI SU E GIù OVVERO CREAZIONE DEGLI ARRAY DEI MOVIMENTI
public class EndCondition {

    /*public static boolean checkEnd(ItalianBoard board){

    }*/

    public static boolean checkWin(ItalianBoard board, Player player) {
        for(int i = Board.DIMENSION_ITALIAN_BOARD - 1; i >= 0; i--){
            for(int j = Board.DIMENSION_ITALIAN_BOARD - 1; j >= 0; j--){
                if(!PlaceType.confrontPlayer(board.getBoard()[i][j].getPlace(), player) && board.getBoard()[i][j].getPlace()!=PlaceType.EMPTY)
                    return false;
            }
        }
        return  true;
    }

    /*private  static boolean checkDraw(ItalianBoard board) {

    }*/
}
