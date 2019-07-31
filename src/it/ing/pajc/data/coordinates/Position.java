package it.ing.pajc.data.coordinates;

/**
 * Position class with all getters and setters
 */
public class Position {
    private int posR;
    private int posC;

    /**
     * Position constructor.
     * @param posR Row position
     * @param posC Column position
     */
    public Position(int posR, int posC) {
        this.posR = posR;
        this.posC = posC;
    }

    /**
     * Change the position to a final position.
     * @param fPosR Final row position
     * @param fPosC Final Column position
     */
    public void changePosition(int fPosR, int fPosC){
        posR=fPosR;
        posC=fPosC;
    }
    /**
     * @return Row position
     */
    public int getPosR() {
        return posR;
    }

    /**
     * Change Row position
     * @param posR new row position
     */
    public void setPosR(int posR) {
        this.posR = posR;
    }

    /**
     * @return Column position
     */
    public int getPosC() {
        return posC;
    }

    /**
     * Change column position.
     * @param posC new column position
     */
    public void setPosC(int posC) {
        this.posC = posC;
    }
}
