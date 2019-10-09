package it.ing.pajc.data.movements;

/**
 * Position class with all getters and setters.
 */
public class Position {
    private int posR;
    private int posC;
    private int cPosR;
    private int cPosC;

    /**
     * Position constructor.
     *
     * @param posR Row position.
     * @param posC Column position.
     */
    public Position(int posR, int posC) {
        this.posR = posR;
        this.posC = posC;
        this.cPosR=-1;
        this.cPosC=-1;
    }

    public Position(int posR, int posC, int cPosR, int cPosC) {
        this.posR = posR;
        this.posC = posC;
        this.cPosR = cPosR;
        this.cPosC = cPosC;

    }

    /**
     * Return the position.
     *
     * @return the position.
     */
    public Position getPosition() {
        return this;
    }

    public Position getCapturedPosition() {
        return new Position(cPosR, cPosC);
    }

    /**
     * Getter of row.
     *
     * @return Row position.
     */
    public int getPosR() {
        return posR;
    }

    /**
     * Getter of column.
     *
     * @return Column position.
     */
    public int getPosC() {
        return posC;
    }

    public int getcPosR() {
        return cPosR;
    }

    public int getcPosC() {
        return cPosC;
    }
}