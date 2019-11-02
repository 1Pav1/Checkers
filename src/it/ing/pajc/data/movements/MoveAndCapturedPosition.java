package it.ing.pajc.data.movements;

public class MoveAndCapturedPosition extends Position{
    private Position toCapture;

    public MoveAndCapturedPosition(int mPosR, int mPosC, int cPosR, int cPosC) {
        super(mPosR, mPosC);
        this.toCapture = new Position(cPosR, cPosC);
    }

    public Position getToCapture() {
        return toCapture;
    }

    public void setToCapture(Position toCapture) {
        this.toCapture = toCapture;
    }

    @Override
    public String toString() {
        return "Moving to:"+super.toString()+" Capturing :"+toCapture;
    }
}
