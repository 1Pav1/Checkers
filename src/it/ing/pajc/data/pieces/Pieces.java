package it.ing.pajc.data.pieces;

import it.ing.pajc.data.coordinates.Position;
import javafx.scene.shape.Circle;

public abstract class Pieces extends Circle {
    private Position position;
    private PiecesColors player;

    /**
     * Pieces constructor giving color and position.
     * @param player Player color
     * @param position Piece's position
     */
     public Pieces(PiecesColors player, Position position){
        this.player = player;
        this.position = position;
    }

    /**
     * @return The color of hte player
     */
    public PiecesColors getPlayer() {
        return player;
    }

    /**
     * Set a new color for the piece.
     * @param player Set a new color.
     */
    public void setPlayer(PiecesColors player) {
        this.player = player;
    }

    /**
     * @return The position
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Set a new position.
     * @param position New position
     */
    public void setPosition(Position position) {
        this.position = position;
    }
}

