package it.ing.pajc.data.board;

import it.ing.pajc.data.pieces.PieceType;
import it.ing.pajc.data.pieces.PlaceType;
import it.ing.pajc.data.pieces.Square;

/**
 * Works with stringBuilder and board representations of a checkers match
 */
class Fen {

    /**
     * Given a fen, creates an Italian board.
     *
     * @return a MultidimensionalArray of position.
     */
    static Square[][] fenToMultidimensionalArray(StringBuilder fenStringBuilder) {
        int i = 0;
        Square[][] squares = new Square[Board.DIMENSION_ITALIAN_BOARD][Board.DIMENSION_ITALIAN_BOARD];
        int boardPosition = 0;
        do {
            if (fenStringBuilder.charAt(i) != '/') {
                switch (fenStringBuilder.charAt(i)) {
                    case 'm': {
                        squares[boardPosition / 8][boardPosition % 8] = new Square(PlaceType.BLACK, PieceType.MAN);
                    }
                    break;
                    case 'k': {
                        squares[boardPosition / 8][boardPosition % 8] = new Square(PlaceType.BLACK, PieceType.KING);
                    }
                    break;
                    //------------------------------------------------------------------
                    case 'M': {
                        squares[boardPosition / 8][boardPosition % 8] = new Square(PlaceType.WHITE, PieceType.MAN);
                    }
                    break;
                    case 'K': {
                        squares[boardPosition / 8][boardPosition % 8] = new Square(PlaceType.WHITE, PieceType.KING);
                    }
                    break;
                    //------------------------------------------------------------------
                    case 'e': {
                        squares[boardPosition / 8][boardPosition % 8] = new Square(PlaceType.EMPTY);
                    }
                }
                boardPosition++;
            }
            i++;
        } while (i < fenStringBuilder.length());
        return squares;
    }

    static StringBuilder multidimensionalArrayToFen(Square[][] squares) {
        StringBuilder fen = new StringBuilder();
        for (int x = 0; x < Board.DIMENSION_ITALIAN_BOARD; x++) {
            for (int y = 0; y < Board.DIMENSION_ITALIAN_BOARD; y++) {
                if (squares[x][y].getPlace() == PlaceType.WHITE)
                    if (squares[x][y].getPiece() == PieceType.MAN)
                        fen.append("M");
                    else
                        fen.append("K");
                else if (squares[x][y].getPlace() == PlaceType.BLACK)
                    if (squares[x][y].getPiece() == PieceType.MAN)
                        fen.append("m");
                    else
                        fen.append("k");
                else if (squares[x][y].getPlace() == PlaceType.EMPTY)
                    fen.append("e");
            }
            if (x != Board.DIMENSION_ITALIAN_BOARD - 1)
                fen.append("/");
        }
        return fen;
    }
}