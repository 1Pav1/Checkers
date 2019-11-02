package it.ing.pajc.singleplayer;

import it.ing.pajc.data.board.Fen;
import it.ing.pajc.data.board.ItalianBoard;
import it.ing.pajc.data.pieces.PiecesColors;
import it.ing.pajc.data.pieces.italian.ItalianMan;

public class SinglePlayerManager {
    private PiecesColors currentPlayer;

    private PiecesColors chosenPlayer;

    private ItalianBoard italianBoard;

    private boolean secondPlayerIsAI;

    public PiecesColors getPlayer() {
        return currentPlayer;
    }

    public ItalianBoard getItalianBoard() {
        return italianBoard;
    }


    public SinglePlayerManager(PiecesColors currentPlayer,PiecesColors chosenPlayer, boolean secondPlayerIsAI) {
        italianBoard = new ItalianBoard(new Fen("memememe/emememem/memememe/eeeeeeee/eeeeeeee/eMeMeMeM/MeMeMeMe/eMeMeMeM"), currentPlayer);
        this.currentPlayer = currentPlayer;
        this.chosenPlayer = chosenPlayer;
        this.secondPlayerIsAI = secondPlayerIsAI;
    }

    public boolean isSecondPlayerIsAI() {
        return secondPlayerIsAI;
    }

    public void setSecondPlayerIsAI(boolean secondPlayerIsAI) {
        this.secondPlayerIsAI = secondPlayerIsAI;
    }

    public void changePlayer() {
        String fenStr = italianBoard.toString();
        System.out.println(fenStr);
        Fen fen = new Fen(fenStr);
        if (!secondPlayerIsAI) {
            if (currentPlayer == PiecesColors.WHITE) {
                currentPlayer = PiecesColors.BLACK;
                italianBoard = new ItalianBoard(fen, currentPlayer);
            } else {
                currentPlayer = PiecesColors.WHITE;
                fen.reverseFen();
                italianBoard = new ItalianBoard(fen, currentPlayer);
            }
        }


        else{
            if(currentPlayer == PiecesColors.ENGINE) {
                currentPlayer = chosenPlayer;
                italianBoard = new ItalianBoard(fen, currentPlayer);
            }
            //If computers turn
            else{
                System.err.println("Computers turn");
                String changedFen;
                if(chosenPlayer == PiecesColors.WHITE)
                    changedFen = Engine.execute(new ItalianBoard(fen, PiecesColors.BLACK)).toString();
                else
                    changedFen = Engine.execute(new ItalianBoard(fen, PiecesColors.WHITE)).toString();
                Fen cf = new Fen(changedFen);
                cf.reverseFen();
                italianBoard = new ItalianBoard(cf,chosenPlayer);

            }
        }


    }


}
