package it.ing.pajc.logic;

public abstract class Pieces {
    private PiecesColors player;

    public Pieces(PiecesColors player){
        this.player=player;
    }

    public PiecesColors getPlayer() {
        return player;
    }

    public void setPlayer(PiecesColors player) {
        this.player = player;
    }

}

