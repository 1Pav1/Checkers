package it.ing.pajc.data.movements;

import it.ing.pajc.data.board.ItalianBoard;
import it.ing.pajc.data.pieces.PiecesColors;
import it.ing.pajc.data.pieces.PiecesType;
import it.ing.pajc.data.pieces.italian.ItalianKing;
import it.ing.pajc.engine.Engine;

import java.util.*;

public class Move {
    //private class

    private final static int LEGALMOVE = 1;
    private final static int ILLEGALMOVE = 2;
    private final static int INCOMPLETEMOVE = 3;

    /**
     * Gives informations about moves posibilities of a piece.
     *
     * @param board  The using board
     * @param toMove Player's color
     * @return true if a piece can't move and capture, false otherwise
     */
    public static boolean noMovesLeft(ItalianBoard board, PiecesColors toMove) {
        System.out.println("*****");
        for (int posR = 0; posR < 8; posR++)
            for (int posC = 0; posC < 8; posC++)
                if ((float) (posR + posC) / 2 == (float) (posR + posC) / 2) {
                    if (toMove == PiecesColors.WHITE &&
                            board.getBoard()[posR][posC].getPlayer() == PiecesColors.WHITE) {
                        if (canWalk(board, posR, posC)) return false;
                        else if (canCapture(board, posR, posC)) return false;
                    } else if (toMove == PiecesColors.BLACK &&
                            board.getBoard()[posR][posC].getPlayer() == PiecesColors.BLACK) {
                        if (canWalk(board, posR, posC)) return false;
                        else if (canCapture(board, posR, posC)) return false;
                    }
                } // if and for
        return true;
    }

    //TODO
    static void printMoves(Vector<Vector<int[]>> moves) {
        System.out.println("MOSSE:");
        for (int posR = 0; posR < moves.size(); posR++) {
            System.out.println("MOSSA " + posR);
            for (int posC = 0; posC < moves.elementAt(posR).size(); posC++)
                System.out.print(moves.elementAt(posR).elementAt(posC)[0] + "," + moves.elementAt(posR).elementAt(posC)[1] + "  ");
            System.out.println();
        }
    }

    // ApplyMove checks if the move entered is legal, illegal,
    // or incomplete.

    // If IsMoveLegal returns INCOMPLETEMOVE, this means a capture has just been made.
    // Call canCapture() to see     if a further capture is possible.
    //TODO
    public static int ApplyMove(ItalianBoard board, int startR, int startC, int endR, int endC, int incInd, Vector<Vector<int[]>> mosseObbligate) {
        System.out.println("ESEGUO MOSSA: (" + startR + "," + startC + ")->(" + endR + "," + endC + ")");
        System.out.println("INC IND:" + incInd);
        int i = 0;
        //for(int i=0; i<mosseObbligate.size(); i++)
        while (i < mosseObbligate.size() && (mosseObbligate.elementAt(i).elementAt(incInd + 1)[0] != endR || mosseObbligate.elementAt(i).elementAt(incInd + 1)[1] != endC))
            i++;
        if (i == mosseObbligate.size())
            return Move.ILLEGALMOVE;

        int r = 0;
        incInd++;
        while (r < mosseObbligate.size()) {
            if (!(mosseObbligate.elementAt(r).elementAt(incInd)[0] == endR && mosseObbligate.elementAt(r).elementAt(incInd)[1] == endC))
                mosseObbligate.remove(r);
            else r++;
        }

        Move.eseguiMossa(board, startR, startC, endR, endC);
        if (incInd != mosseObbligate.get(0).size() - 1)
            return Move.INCOMPLETEMOVE;
        return Move.LEGALMOVE;
    }

    // examines a board position to see if the piece indicated at (x,y)
    // can make a(nother) capture
    static boolean canCapture(ItalianBoard board, int posR, int posC) {
        boolean result = false;
        if (board.getBoard()[posR][posC].getType() == PiecesType.MAN)
            result = board.getMan(posR, posC).canCapture(board, new Position(posR, posC));
        else if (board.getBoard()[posR][posC].getType() == PiecesType.KING)
            result = board.getKing(posR, posC).canCapture(board, new Position(posR, posC));
        return result;
    } // canCapture()

    // canWalk() returns true if the piece on (posR,posC) can make a
    // legal non-capturing move
    static boolean canWalk(ItalianBoard board, int posR, int posC) {
        boolean result = false;
        if (board.getBoard()[posR][posC].getType() == PiecesType.MAN)
            result = board.getMan(posR, posC).canMove(board, new Position(posR, posC));
        else if (board.getBoard()[posR][posC].getType() == PiecesType.KING)
            result = board.getKing(posR, posC).canMove(board, new Position(posR, posC));
        return result;
    } // canWalk()

    private static boolean isEmpty(ItalianBoard board, int posR, int posC) {
        if (posR > -1 && posR < 8 && posC > -1 && posC < 8)
            if (board.getBoard()[posR][posC].getPlayer() == PiecesColors.EMPTY)
                return true;
        return false;
    } // isEmpty()

    // checkers that i and j are between 0 and 7 inclusive
    public static boolean inRange(int posR, int posC) {
        return (posR > -1 && posR < 8 && posC > -1 && posC < 8);
    }


    /**
     * Esegue la mossa in input ed eventualmente "mangia"
     *
     * @param board
     * @param startR
     * @param startC
     * @param endR
     * @param endC
     */
    public static void eseguiMossa(ItalianBoard board, int startR, int startC, int endR, int endC) {
        if (Math.abs(startR - endR) == 2) { //sto mangiando?
            board.getBoard()[startR + (endR - startR) / 2][startC + (endC - startC) / 2].setPlayer(PiecesColors.EMPTY); //mangio
        }
        board.getBoard()[endR][endC] = board.getBoard()[startR][startC]; //sposto la pedina nella nuova posizione
        board.getBoard()[startR][startC].setPlayer(PiecesColors.EMPTY);  //libero la posizione di partenza
        // check for new king
        if (board.getBoard()[endR][endC].getPlayer() == PiecesColors.WHITE && endR == 7)
            board.getBoard()[endR][endC] = new ItalianKing(new Position(endR, endC), PiecesColors.WHITE);
        else if (board.getBoard()[endR][endC].getPlayer() == PiecesColors.BLACK && endR == 0)
            board.getBoard()[endR][endC] = new ItalianKing(new Position(endR, endC), PiecesColors.BLACK);
    }

    public static void eseguiMossa(ItalianBoard board, Vector<int[]> m) {

        for (int i = 1; i < m.size(); i++) {
            if (Math.abs(m.get(i - 1)[1] - m.get(i)[1]) == 2) { //sto mangiando?
                board.getBoard()[m.get(i - 1)[0] + (m.get(i)[0] - m.get(i - 1)[0]) / 2][m.get(i - 1)[1] + (m.get(i)[1] - m.get(i - 1)[1]) / 2].setPlayer(PiecesColors.EMPTY); //mangio
            }
            board.getBoard()[m.get(i)[0]][m.get(i)[1]] = board.getBoard()[m.get(i - 1)[0]][m.get(i - 1)[1]];
            board.getBoard()[m.get(i - 1)[0]][m.get(i - 1)[1]].setPlayer(PiecesColors.EMPTY);
            //controllo se diventa dama
            if (board.getBoard()[m.get(i)[0]][m.get(i)[1]].getPlayer() == PiecesColors.WHITE && m.get(i)[0] == 7)
                board.getBoard()[m.get(i)[0]][m.get(i)[1]] = new ItalianKing(new Position(m.get(i)[0], m.get(i)[1]), PiecesColors.WHITE);
            else if (board.getBoard()[m.get(i)[0]][m.get(i)[1]].getPlayer() == PiecesColors.BLACK && m.get(i)[0] == 0)
                board.getBoard()[m.get(i)[0]][m.get(i)[1]] = new ItalianKing(new Position(m.get(i)[0], m.get(i)[1]), PiecesColors.BLACK);
        }

    }

    /**
     * Gestisce le catture multiple.
     *
     * @param board
     * @param posR
     * @param posC
     * @param player
     */
    static void cattureMultiple(ItalianBoard board, int posR, int posC, PiecesColors player, String sequenzaCatture, int nDamePrese, char pezzo, MoveScheduler ms) {

        ms.add(infoMossa);                // inserisco la mossa base nella struttura

        // determino il verso del gioco in base a chi gioca: bianco o nero,
        int verso = 0;
        if (player == PiecesColors.WHITE)    // bianco incrementa le coordinate delle ascisse
            verso = 1;
        else
            verso = -1;        // nero decrementa le coordinate delle ascisse

        // memorizzo la sequenza prima del branching
        String sequenzaTmp = sequenzaCatture;

        // determino quale sono le mosse legali del giocatore a partire dalla posizione posR, posC
        for (int k = -2; k <= 2; k += 4) { //provo le 2 mosse legali per la pedina (e la dama)
            sequenzaCatture = sequenzaTmp;
            if (Move.inRange(posR + 2 * verso, posC + k) && board.getBoard()[posR + 2 * verso][posC + k].getPlayer() == PiecesColors.EMPTY) //sono nella scacchiera
                if (board.getBoard()[posR + verso][posC + k / 2].getPlayer() == Engine.opponent(player) &&
                        (isKing(board.getBoard()[posR][posC].getType()) || (!isKing(board.getBoard()[posR][posC].getType()) && !isKing(board.getBoard()[posR + verso][posC + k / 2].getType())))) { //posso mangiare?
                    // costruisco la sequenza del tipo di pedine mangiate
                    if (board.getBoard()[posR + verso][posC + k / 2].getType() == PiecesType.MAN) {
                        sequenzaCatture += "p";
                    } else {
                        sequenzaCatture += "d";
                        nDamePrese--;
                    }

                    ItalianBoard tmpBoard = Engine.copy_board(board);        // eseguo una copia della board
                    eseguiMossa(tmpBoard, posR, posC, posR + 2 * verso, posC + k);    // eseguo la mossa sulla copia

                    cattureMultiple(tmpBoard, posR + 2 * verso, posC + k, player, new String(sequenzaCatture), nDamePrese, pezzo, ms);    // richiamo la funzione ricorsiva di cattura multiple
                }
        }

        // nel caso in cui si stia analizzando una dama, devo considerare anche le possibili mosse che puo' eseguire tornando indietro
        if (board.getBoard()[posR][posC].getType() == PiecesType.KING) { //se muove una dama
            for (int k = -2; k <= 2; k += 4) { //provo le 2 mosse legali per la pedina (e la dama)
                sequenzaCatture = sequenzaTmp;
                if (Move.inRange(posR - 2 * verso, posC + k) && board.getBoard()[posR - 2 * verso][posC + k].getPlayer() == PiecesColors.EMPTY) //sono nella scacchiera
                    if (board.getBoard()[posR - verso][posC + k / 2].getPlayer() == Engine.opponent(player)) { //posso mangiare?


                        if (board.getBoard()[posR - verso][posC + k / 2].getType() == PiecesType.MAN) {
                            sequenzaCatture += "p";
                        } else {
                            sequenzaCatture += "d";
                            nDamePrese--;
                        }

                        ItalianBoard tmpBoard = Engine.copy_board(board);
                        eseguiMossa(tmpBoard, posR, posC, posR - 2 * verso, posC + k);


                        cattureMultiple(tmpBoard, posR - 2 * verso, posC + k, player, new String(sequenzaCatture), nDamePrese, pezzo, ms);
                    }

            }//fine controllo altre 2 mosse legali dama
        }
    }// finito cattureMultiple()


    public static boolean isKing(PiecesType pezzo) {
        return (pezzo == PiecesType.KING);
    }

    //	given a board, generates all the possible moves depending on whose turn
    public static Vector<Vector<int[]>> generate_moves(ItalianBoard board, PiecesColors player) {
        MoveScheduler ms = new MoveScheduler(); //serve per diversificare le liste di mosse possibili in funzione del numero
        //e tipo di catture (vedi regole)
        char pezzo;
        int nDamePrese = 99;
        int verso = 0;
        String sequenzaCatture;

        if (player == PiecesColors.WHITE)
            verso = 1;
        else
            verso = -1;
        for (int posR = 0; posR < 8; posR++)
            for (int posC = 0; posC < 8; posC++) {
                if (board.getBoard()[posR][posC].getPlayer() == player) {
                    if (board.getBoard()[posR][posC].getType() == PiecesType.MAN)
                        pezzo = 'p';
                    else
                        pezzo = 'd';
                    for (int k = -2; k <= 2; k += 4) { //provo le 2 direzioni legali per la pedina (e la dama)
                        sequenzaCatture = new String();
                        nDamePrese = 99;
                        if (Move.inRange(posR + 2 * verso, posC + k) && board.getBoard()[posR + 2 * verso][posC + k].getPlayer() == PiecesColors.EMPTY) { //nel tentativo di mangiare, sono nella scacchiera
                            if (board.getBoard()[posR + verso][posC + k / 2].getPlayer() == Engine.opponent(player) &&
                                    (isKing(board.getBoard()[posR][posC].getType()) || (!isKing(board.getBoard()[posR][posC].getType()) && !isKing(board.getBoard()[posR + verso][posC + k / 2].getType())))) { //posso mangiare?
                                if (board.getBoard()[posR + verso][posC + k / 2].getType() == PiecesType.MAN)
                                    sequenzaCatture += "p";
                                else {
                                    sequenzaCatture += "d";
                                    nDamePrese--;
                                }
                                ItalianBoard tmpBoard = Engine.copy_board(board);
                                eseguiMossa(tmpBoard, posR, posC, posR + 2 * verso, posC + k);
//								Vector<int[]> vettoreMosse = new Vector<int[]>();
//								int[] inizioMossa={i,j};
//								vettoreMosse.add(inizioMossa);
                                cattureMultiple(tmpBoard, posR + 2 * verso, posC + k, player, sequenzaCatture, nDamePrese, pezzo, ms);
                            } else if (board.getBoard()[posR + verso][posC + k / 2].getPlayer() == PiecesColors.EMPTY) { //mossa semplice
                                ms.add(); //inserisco "p99" perchè il passo è singolo e non devo fare catture cosi' la dama non ha priorita'
                            }
                        } else if (Move.inRange(posR + verso, posC + k / 2) && board.getBoard()[posR + verso][posC + k / 2].getPlayer() == PiecesColors.EMPTY) { //posso fare mossa semplice?
                            ms.add();//inserisco "p99" perchè il passo è singolo e non devo fare catture cosi' la dama non ha priorita'
                        }
                    } //fine controllo mosse possibili per pedina e dama nel verso "verso l'avversario"
                    if (board.getBoard()[posR][posC].getType() == PiecesType.KING) {
                        for (int k = -2; k <= 2; k += 4) { //provo le altre 2 direzioni legali per la la dama
                            sequenzaCatture = new String();
                            nDamePrese = 99;
                            if (Move.inRange(posR - 2 * verso, posC + k) && board.getBoard()[posR - 2 * verso][posC + k].getPlayer() == PiecesColors.EMPTY) { //nel tentativo di mangiare, sono nella scacchiera
                                if (board.getBoard()[posR - verso][posC + k / 2].getPlayer() == Engine.opponent(player)) { //posso mangiare?
                                    if (board.getBoard()[posR - verso][posC + k / 2].getType() == PiecesType.MAN)
                                        sequenzaCatture += "p";
                                    else {
                                        sequenzaCatture += "d";
                                        nDamePrese--;
                                    }
                                    ItalianBoard tmpBoard = Engine.copy_board(board);
                                    eseguiMossa(tmpBoard, posR, posC, posR - 2 * verso, posC + k);
//									Vector<int[]> vettoreMosse = new Vector<int[]>();
//									int[] inizioMossa={i,j};
//									vettoreMosse.add(inizioMossa);
                                    cattureMultiple(tmpBoard, posR - 2 * verso, posC + k, player, sequenzaCatture, nDamePrese, pezzo, ms);
                                } else if (board.getBoard()[posR - verso][posC + k / 2].getPlayer() == PiecesColors.EMPTY) { //mossa semplice
                                    ms.add();//inserisco "p99" perchè il passo è singolo e non devo fare catture cosi' la dama non ha priorita'
                                }
                            } else if (Move.inRange(posR - verso, posC + k / 2) && board.getBoard()[posR - verso][posC + k / 2].getPlayer() == PiecesColors.EMPTY) { //posso fare mossa semplice?
                                ms.add();//inserisco "p99" perchè il passo è singolo e non devo fare catture cosi' la dama non ha priorita'
                            }
                        }
                    }//fine controllo altre 2 possibili mosse se muove una dama
                }
            }

        return ms.getMossePossibili();
    }//generate_moves

} // class Move
