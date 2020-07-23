package it.ing.pajc.data.pieces;


public class Square {
    private PlaceType place;
    private PieceType type;

    /**
     * Constructor Square
     *
     * @param place of the piece
     */
    public Square(PlaceType place) {
        this.place = place;
        this.type = null;
    }

    /**
     * Constructor Square
     *
     * @param place of the piece
     * @param type  of the piece
     */
    public Square(PlaceType place, PieceType type) {
        this(place);
        this.type = type;
    }

    /**
     * Getter
     *
     * @return the piece
     */
    public PieceType getPiece() {
        return type;
    }

    /**
     * Setter
     *
     * @param type of the piece
     */
    public void setPiece(PieceType type) {
        this.type = type;
    }

    /**
     * Getter
     *
     * @return the place
     */
    public PlaceType getPlace() {
        return place;
    }

    /**
     * Setter
     *
     * @param place of the place
     */
    public void setPlace(PlaceType place) {
        if (place == PlaceType.EMPTY)
            type = null;
        this.place = place;
    }
}
