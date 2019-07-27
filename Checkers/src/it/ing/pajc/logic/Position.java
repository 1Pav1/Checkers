package it.ing.pajc.logic;

public class Position {
    private int posR;
    private int posC;

    public Position(int posR, int posC) {
        this.posR = posR;
        this.posC = posC;
    }

    public int getPosR() {
        return posR;
    }

    public void setPosR(int posR) {
        this.posR = posR;
    }

    public int getPosC() {
        return posC;
    }

    public void setPosC(int posC) {
        this.posC = posC;
    }
}
