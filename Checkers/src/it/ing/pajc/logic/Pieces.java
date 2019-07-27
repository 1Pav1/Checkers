package it.ing.pajc.logic;

import javafx.scene.shape.Circle;

public abstract class Pieces extends Circle {
    private Position position;

    private PiecesColors player;

    public Pieces(PiecesColors player,Position position){
        this.player = player;
        this.position = position;
    }

    public PiecesColors getPlayer() {
        return player;
    }

    public void setPlayer(PiecesColors player) {
        this.player = player;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
}

