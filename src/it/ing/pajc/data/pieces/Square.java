package it.ing.pajc.data.pieces;


public class Square {
    private PlaceType place;
    private PieceType type;

    public Square(PlaceType place) {
        this.place = place;
        this.type = null;
    }

    public Square(PlaceType place, PieceType type) {
        this(place);
        this.type = type;
    }

    public PieceType getPiece() {
        return type;
    }

    public void setPiece(PieceType type) {
        this.type = type;
    }

    public PlaceType getPlace() {
        return place;
    }

    public void setPlace(PlaceType place) {
        if(place==PlaceType.EMPTY)
            type=null;
        this.place=place;
    }
}
