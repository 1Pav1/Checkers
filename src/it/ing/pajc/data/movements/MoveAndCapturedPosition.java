package it.ing.pajc.data.movements;

public class MoveAndCapturedPosition extends Position {
    private int cPosR;
    private int cPosC;

    public MoveAndCapturedPosition(int mPosR, int mPosC, int cPosR, int cPosC) {
        super(mPosR, mPosC);
        this.cPosR = cPosR;
        this.cPosC = cPosC;
    }
 

    public int getcPosR() {
        return cPosR;
    }

    public int getcPosC() {
        return cPosC;
    }

}
