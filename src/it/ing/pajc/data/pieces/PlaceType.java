package it.ing.pajc.data.pieces;

import it.ing.pajc.manager.Player;


/**
 * Pieces colors and empty case.
 */
public enum PlaceType {
    WHITE,
    BLACK,
    EMPTY;

    public static boolean confrontPlayer(PlaceType placeType, Player player){
        Player convertionOfPlayer = null;
        if(placeType==WHITE)
            convertionOfPlayer = Player.WHITE_PLAYER;
        else if(placeType==BLACK)
            convertionOfPlayer = Player.BLACK_PLAYER;

        return convertionOfPlayer == player;
    }
}
