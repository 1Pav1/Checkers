package it.ing.pajc.data.movements;

/**
 * Position class with all getters and setters.
 */
public class Position {
    private final int posR;
    private final int posC;
    private final int cPosR;
    private final int cPosC;

    /**
     * Position constructor.
     *
     * @param posR Row position.
     * @param posC Column position.
     */
    public Position(int posR, int posC) {
        this.posR = posR;
        this.posC = posC;
        this.cPosR = -1;
        this.cPosC = -1;
    }

    /**
     * Position constructor.
     *
     * @param posR  Row position.
     * @param posC  Column position.
     * @param cPosR Row position.
     * @param cPosC Column position.
     */
    public Position(int posR, int posC, int cPosR, int cPosC) {
        this.posR = posR;
        this.posC = posC;
        this.cPosR = cPosR;
        this.cPosC = cPosC;
    }


    /**
     * Returns the capture position
     * @return if both negative there is not a right capturing position
     */
    public int getcPosR() {
        return cPosR;
    }

    public int getcPosC() {
        return cPosC;
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

    public String toString() {
        String stringa="Moving to:" + " Position r:" + posR + " c:" + posC;
        if(cPosC!=-1 && cPosR!=-1)
            stringa+= " Capturing :" + " r:" + cPosR + " c:" + cPosC;
        return  stringa;
    }
}