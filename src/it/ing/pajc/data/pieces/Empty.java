package it.ing.pajc.data.pieces;

import it.ing.pajc.data.movements.Position;

public class Empty extends Pieces {
    public Empty( Position pos){
        super(pos);
        setPlayer(PiecesColors.EMPTY);
    }
}

