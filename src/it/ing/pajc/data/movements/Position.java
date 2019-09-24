package it.ing.pajc.data.movements;

/**
 * Position class with all getters and setters.
 */
public class Position {
    private int posR;
    private int posC;

    /**
     * Position constructor.
     *
     * @param posR Row position.
     * @param posC Column position.
     */
    public Position(int posR, int posC) {
        this.posR = posR;
        this.posC = posC;
    }

    /**
     * Return the position.
     *
     * @return the position.
     */
    public Position getPosition() {
        return this;
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
}