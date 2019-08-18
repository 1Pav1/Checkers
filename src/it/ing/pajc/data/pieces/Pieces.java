package it.ing.pajc.data.pieces;

import it.ing.pajc.data.movements.Position;
import javafx.scene.shape.Circle;

import java.io.Serializable;

/**
 * Pieces abstract class used form all type of pieces, gives informations about position, player and piece's type.
 */
public abstract class Pieces extends Circle implements Serializable {
    private Position position;
    private PiecesColors player;
    private PiecesType type;

    /**
     * Pieces constructor giving position.
     *
     * @param position Piece's position
     */
    public Pieces(Position position) {
        this.position = position;
    }

    /**
     * @return the color of the player
     */
    public PiecesColors getPlayer() {
        return player;
    }

    /**
     * Set a new color for the piece.
     *
     * @param player Set a new color.
     */
    public void setPlayer(PiecesColors player) {
        this.player = player;
    }

    /**
     * @return the position
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Set a new position.
     *
     * @param position New position
     */
    public void setPosition(Position position) {
        this.position = position;
    }

    /**
     * @return the type.
     */
    public PiecesType getType() {
        return type;
    }

    /**
     * Set a new type
     *
     * @param type New type
     */
    public void setType(PiecesType type) {
        this.type = type;
    }
}

