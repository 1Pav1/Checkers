package it.ing.pajc.data.board;

import it.ing.pajc.data.movements.Position;
import it.ing.pajc.data.pieces.Empty;
import it.ing.pajc.data.pieces.Pieces;
import it.ing.pajc.data.pieces.PiecesColors;
import it.ing.pajc.data.pieces.PiecesType;
import it.ing.pajc.data.pieces.italian.ItalianKing;
import it.ing.pajc.data.pieces.italian.ItalianMan;

public class Fen {
    private StringBuilder fen = new StringBuilder();
    private ItalyBoard board;

    /**
     * Assigns a given fen to a class property.
     *
     * @param fen Notation that describes a given set of positions.
     */
    public Fen(String fen) {
        this.fen.append(fen);
    }

    /**
     * Given an Italianboard, creates the specific fen.
     *
     * @param board The Italianboard.
     */
    public Fen(ItalyBoard board) {
        this.board = board;
        fen = new StringBuilder();
        for (int x = 0; x < board.DIMENSION_ITALIAN_BOARD; x++) {
            for (int y = 0; y < board.DIMENSION_ITALIAN_BOARD; y++) {
                if (board.getBoard()[x][y].getPlayer() == PiecesColors.WHITE)
                    if (board.getBoard()[x][y].getType() == PiecesType.MAN)
                        fen.append("M");
                    else
                        fen.append("K");
                else if (board.getBoard()[x][y].getPlayer() == PiecesColors.BLACK)
                    if (board.getBoard()[x][y].getType() == PiecesType.MAN)
                        fen.append("m");
                    else
                        fen.append("k");
                else if (board.getBoard()[x][y].getPlayer() == PiecesColors.EMPTY)
                    fen.append("e");
            }
            if (x != board.DIMENSION_ITALIAN_BOARD - 1)
                fen.append("/");
        }
    }

    /**
     * Given a fen, creates an Italian board.
     *
     * @return a MultidimensionalArray of position.
     */
    Pieces[][] fenToMultidimensionalArray() {
        int i = 0;
        Pieces[][] pieces = new Pieces[board.DIMENSION_ITALIAN_BOARD][board.DIMENSION_ITALIAN_BOARD];
        int boardPosition = 0;
        do {
            if (fen.charAt(i) != '/') {
                switch (fen.charAt(i)) {
                    case 'm': {
                        pieces[boardPosition / 8][boardPosition % 8] = new ItalianMan(new Position(boardPosition / 8, boardPosition % 8), PiecesColors.BLACK);
                    }
                    break;
                    case 'k': {
                        pieces[boardPosition / 8][boardPosition % 8] = new ItalianKing(new Position(boardPosition / 8, boardPosition % 8), PiecesColors.BLACK);
                    }
                    break;
                    //------------------------------------------------------------------
                    case 'M': {
                        pieces[boardPosition / 8][boardPosition % 8] = new ItalianMan(new Position(boardPosition / 8, boardPosition % 8), PiecesColors.WHITE);
                    }
                    break;
                    case 'K': {
                        pieces[boardPosition / 8][boardPosition % 8] = new ItalianKing(new Position(boardPosition / 8, boardPosition % 8), PiecesColors.WHITE);
                    }
                    break;
                    //------------------------------------------------------------------
                    case 'e': {
                        pieces[boardPosition / 8][boardPosition % 8] = new Empty(new Position(boardPosition / 8, boardPosition % 8));
                    }
                }
                boardPosition++;
            }
            i++;
        } while (i < fen.length());
        return pieces;
    }

    /**
     * Rotates the board by reversing the fen.
     */
    void reverseFen() {
        fen.reverse();
    }

    /**
     * Getter of Fen.
     *
     * @return the fen.
     */
    public StringBuilder getFen() {
        return fen;
    }
}
