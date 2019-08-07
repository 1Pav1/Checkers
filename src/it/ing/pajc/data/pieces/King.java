package it.ing.pajc.data.pieces;

import it.ing.pajc.data.movements.Position;

public abstract class King extends Pieces {
    public King( Position pos){
        super(pos);
        setType(PiecesType.KING);
    }
}
