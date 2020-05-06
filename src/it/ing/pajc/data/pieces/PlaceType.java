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
            convertionOfPlayer = Player.FIRST;
        else if(placeType==BLACK)
            convertionOfPlayer = Player.SECOND;

        return convertionOfPlayer == player;
    }
}
