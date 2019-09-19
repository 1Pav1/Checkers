package it.ing.pajc.data.pieces;

import it.ing.pajc.data.movements.Position;

/**
 * Empty class used to define null positions.
 */
public class Empty extends Pieces {
    /**
     * Empty's constructor giving position.
     * @param pos Empty position.
     */
    public Empty(Position pos) {
        super(pos);
        setPlayer(PiecesColors.EMPTY);
    }
}

