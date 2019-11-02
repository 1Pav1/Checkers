package it.ing.pajc.singleplayer;

import it.ing.pajc.data.board.Fen;
import it.ing.pajc.data.board.ItalianBoard;
import it.ing.pajc.data.pieces.PiecesColors;

public class SinglePlayerManager {
    private PiecesColors player;

    private ItalianBoard italianBoard;

    public PiecesColors getPlayer() {
        return player;
    }

    public ItalianBoard getItalianBoard() {
        return italianBoard;
    }

    public SinglePlayerManager(PiecesColors player) {
        italianBoard = new ItalianBoard(new Fen("memememe/emememem/memememe/eeeeeeee/eeeeeeee/eMeMeMeM/MeMeMeMe/eMeMeMeM"), player);
        this.player = player;
    }

    public void changePlayer() {
        String fenStr = italianBoard.toString();
        System.out.println(fenStr);
        Fen fen = new Fen(fenStr);
        if (player == PiecesColors.WHITE) {
            player = PiecesColors.BLACK;
            italianBoard = new ItalianBoard(fen, player);
        } else {
            player = PiecesColors.WHITE;
            fen.reverseFen();
            italianBoard = new ItalianBoard(fen, player);
        }


    }


}
