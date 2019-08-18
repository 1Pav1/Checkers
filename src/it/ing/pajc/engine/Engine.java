package it.ing.pajc.engine;

import it.ing.pajc.data.board.ItalianBoard;
import it.ing.pajc.data.pieces.PiecesColors;
import it.ing.pajc.data.pieces.PiecesType;

import java.util.*;


class Engine {

    final static int INFINITY = Integer.MAX_VALUE;
    final static int CHECKER = 100; //one checker worth 100
    final static int POS = 1;  //one position along the -j worth 1
    final static int KING = 200; //a king's worth
    final static int EDGE = 10; // effect of king being on the edge
    final static int RANDOM_WEIGHT = 10; // weight factor

    private static final int tableWeight[] = {
            4, 4, 4, 4,
            4, 3, 3, 3,
            3, 2, 2, 4,
            4, 2, 1, 3,
            3, 1, 2, 4,
            4, 2, 2, 3,
            3, 3, 3, 4,
            4, 4, 4, 4};


    public static int EvalSpettacolo(ItalianBoard board, PiecesColors player) { //Valuto dal punto di vista del NERO

        int score = 0;
        int numeroPedine = 0;
        boolean damaTrovata = false;
        boolean pedinaTrovata = false;
        final int LIMITE_PEDINE = 18;
        final int PESO_PEDINA = 100;
        final int PESO_DAMA = 200;
        final int PESO_AVERE_MOSSA = 20;
        final int PESO_RANDOM = 10;

        //controllo in che fase di gioco siamo
        for (int posR = 0; posR < 8 && !damaTrovata; posR++)
            for (int posC = 0; posC < 8 && !damaTrovata; posC++) {
                if (board.getBoard()[posR][posC].getPlayer() != PiecesColors.EMPTY)
                    numeroPedine++;
                if ((board.getBoard()[posR][posC].getPlayer() == PiecesColors.BLACK && board.getBoard()[posR][posC].getType() == PiecesType.KING) ||
                        (board.getBoard()[posR][posC].getPlayer() == PiecesColors.WHITE && board.getBoard()[posR][posC].getType() == PiecesType.KING))
                    damaTrovata = true;
            }

        if (!damaTrovata || numeroPedine > LIMITE_PEDINE) { //fase iniziale
            for (int posR = 0; posR < 8; posR++)
                for (int posC = 0; posC < 8; posC++) {
                    if (board.getBoard()[posR][posC].getPlayer() == PiecesColors.BLACK) {
                        if ((7 - posR) <= 5) //parte del NERO
                            score += PESO_PEDINA * (8 - posC) * (8 - posC) * (7 - posR) * (7 - posR);
                        else
                            score += PESO_PEDINA * posC * posC * (7 - posR) * (7 - posR);
                    }
                    if (board.getBoard()[posR][posC].getPlayer() == PiecesColors.WHITE) {
                        if (posR <= 3) //parte del BIANCO
                            score -= PESO_PEDINA * (8 - posC) * (8 - posC) * posR * posR;
                        else
                            score -= PESO_PEDINA * posC * posC * posR * posR;
                    }
                }
        } else {//fase finale
            int r = 0, c = 0;
            int numeroCoppiePari = 0;

            for (int posR = 0; posR < 8; posR++)
                for (int posC = 0; posC < 8; posC++) {
                    if (board.getBoard()[posR][posC].getPlayer() != PiecesColors.EMPTY) {
                        if (!pedinaTrovata) {
                            r = posR;
                            c = posC;
                            pedinaTrovata = true;
                        } else {
                            pedinaTrovata = false;
                            if (Math.max(Math.abs(r - posR), Math.abs(c - posC)) % 2 == 0) { //distanza coppia e' pari
                                numeroCoppiePari++;
                            }
                        }
                        if (board.getBoard()[posR][posC].getPlayer() == PiecesColors.BLACK) {
                            score += PESO_PEDINA * (7 - posR) * (7 - posR);
                        } else if (board.getBoard()[posR][posC].getPlayer() == PiecesColors.WHITE) {
                            score -= PESO_PEDINA * posR * posR;
                        } else if (board.getBoard()[posR][posC].getPlayer() == PiecesColors.BLACK && board.getBoard()[posR][posC].getType() == PiecesType.KING) {
                            score += PESO_DAMA;
                        } else if (board.getBoard()[posR][posC].getPlayer() == PiecesColors.WHITE && board.getBoard()[posR][posC].getType() == PiecesType.KING) {
                            score -= PESO_DAMA;
                        }
                    }
                }
            if ((numeroCoppiePari % 2) == 1) //chi muove e' privilegiato
                if (player == PiecesColors.BLACK)  //player DEVE essere WHITE o BLACK
                    score += PESO_AVERE_MOSSA;
                else
                    score -= PESO_AVERE_MOSSA;

        }
        score += (int) (Math.random() * PESO_RANDOM);
        return score;

    }

    public static int valutaPos(int posR, int posC, ItalianBoard board) {
        int value;

        if (board.getBoard()[posR][posC].getPlayer() == PiecesColors.WHITE)
            if (posR == 1)
                value = 7;
            else
                value = 5;
        else if (board.getBoard()[posR][posC].getPlayer() == PiecesColors.BLACK)
            if (posR == 6)
                value = 7;
            else
                value = 5;
        else // dama
            value = 10;

        return value * tableWeight[(posR / 2) * 4 + posC / 2] * posR * posR;
    }

    public static int Evaluation2Compl(ItalianBoard board) {
        final int CHECKER = 150;
        final int POS = 1;
        final int KING = 300;
        int score = 0;

        for (int posR = 0; posR < 8; posR++)
            for (int posC = 0; posC < 8; posC++) {
                if (board.getBoard()[posR][posC].getPlayer() == PiecesColors.WHITE) {
                    score -= valutaPos(posR, posC, board);
                } else if (board.getBoard()[posR][posC].getPlayer() == PiecesColors.BLACK) {
                    score += valutaPos(posR, posC, board);
                } else if (board.getBoard()[posR][posC].getPlayer() == PiecesColors.WHITE && board.getBoard()[posR][posC].getType() == PiecesType.KING) {
                    score -= valutaPos(posR, posC, board);
                } else if (board.getBoard()[posR][posC].getPlayer() == PiecesColors.BLACK && board.getBoard()[posR][posC].getType() == PiecesType.KING) {
                    score += valutaPos(posR, posC, board);
                }

            }//end for
        score += (int) (Math.random() * RANDOM_WEIGHT);
        return score;


    }

    public static int Evaluation2(ItalianBoard board) { //Valuto dal punto di vista del NERO
        final int CHECKER = 150;
        final int POS = 1;
        final int KING = 300;
        int score = 0;

        for (int posR = 0; posR < 8; posR++)
            for (int posC = 0; posC < 8; posC++) {
                if (board.getBoard()[posR][posC].getPlayer() == PiecesColors.WHITE) {
                    score -= CHECKER;
                    score -= POS * posR * posR;
                    score -= supportoWHITE(board, posR, posC);
                } else if (board.getBoard()[posR][posC].getPlayer() == PiecesColors.BLACK) {
                    score += CHECKER;
                    score += POS * (7 - posR) * (7 - posR);
                    score += supportoBLACK(board, posR, posC);
                } else if (board.getBoard()[posR][posC].getPlayer() == PiecesColors.WHITE && board.getBoard()[posR][posC].getType() == PiecesType.KING) {
                    score -= KING;
                    score -= (4 - Math.abs(3.5 - posR)) * (4 - Math.abs(3.5 - posR)) + (4 - Math.abs(3.5 - posC)) * (4 - Math.abs(3.5 - posC));
                } else if (board.getBoard()[posR][posC].getPlayer() == PiecesColors.BLACK && board.getBoard()[posR][posC].getType() == PiecesType.KING) {
                    score += KING;
                    score += (4 - Math.abs(3.5 - posR)) * (4 - Math.abs(3.5 - posR)) + (4 - Math.abs(3.5 - posC)) * (4 - Math.abs(3.5 - posC));
                }

            }//end for
        score += (int) (Math.random() * RANDOM_WEIGHT);
        return score;

    }

    public static int supportoBLACK(ItalianBoard board, int posR, int posC) {
        int score = 0;
        int tmp = 0;

        if (!Move.inRange(posR, posC) || ((Move.inRange(posR + 1, posC - 1) && position[posR + 1][posC - 1] != Checkers.BLACK) && (Move.inRange(posR + 1, posC + 1) && position[posR + 1][posC + 1] != Checkers.BLACK)))
            if (posR == 8)
                return 0;
            else
                return -1;
        else {
            tmp = supportoBLACK(board, posR + 1, posC - 1);
            if (tmp != -1)
                score += tmp + 2;
            tmp = supportoBLACK(board, posR + 1, posC + 1);
            if (tmp != -1)
                score += tmp + 2;
        }
        return score;
    }

    public static int supportoWHITE(ItalianBoard board, int posR, int posC) {
        int score = 0;
        int tmp = 0;

        if (!Move.inRange(posR, posC) || ((Move.inRange(posR - 1, posC - 1) && board.getBoard()[posR - 1][posC - 1].getPlayer() != PiecesColors.WHITE) && (Move.inRange(posR - 1, posC + 1) && board.getBoard()[posR - 1][posC + 1].getPlayer() != PiecesColors.WHITE)))
            if (posR == -1)
                return 0;
            else
                return -1;
        else {
            tmp = supportoWHITE(board, posR - 1, posC - 1);
            if (tmp != -1)
                score += tmp + 2;
            tmp = supportoWHITE(board, posR - 1, posC + 1);
            if (tmp != -1)
                score += tmp + 2;
        }
        return score;
    }


    public static int EvalPrima(ItalianBoard board, int player) {
        final int CHECKER = 100;
        final int POS = 1;
        final int KING = 200;
        int score = 0;
        int[][] posKW = new int[12][2];
        int[][] posKB = new int[12][2];
        int nKW = 0, nKB = 0;

        for (int posR = 0; posR < 8; posR++)
            for (int posC = 0; posC < 8; posC++) {
                if (board.getBoard()[posR][posC].getPlayer() == PiecesColors.WHITE) {
                    score -= CHECKER;
                    score -= POS * posR * posR;
                    score -= supportoWHITE(board, posR, posC);
                } else if (position[posR][posC] == Checkers.BLACK) {
                    score += CHECKER;
                    score += POS * (7 - posR) * (7 - posR);
                    score += supportoBLACK(board, posR, posC);
                } else if (position[posR][posC] == Checkers.WKING) {
                    posKW[nKW][0] = posR;
                    posKW[nKW][1] = posC;
                    nKW++;
                    score -= KING;
                    if (posR == 0 || posR == 7)
                        score += EDGE;
                    if (posC == 0 || posC == 7)
                        score += EDGE;
                } else if (position[posR][posC] == Checkers.BKING) {
                    posKB[nKB][0] = posR;
                    posKB[nKB][1] = posC;
                    nKB++;
                    score += KING;
                    if (posR == 0 || posR == 7)
                        score -= EDGE;
                    if (posC == 0 || posC == 7)
                        score -= EDGE;
                }

            }//end for
//	for(int i=0;i<nKB;i++)
//		for(int j=0;j<nKW;j++)
//			if(Math.abs(posKB[i][0]-posKW[j][0])!=1 || Math.abs(posKB[i][1]-posKW[j][1])!=1)
//				if(player==Checkers.WHITE)
//					score-=1280-(Math.abs(posKB[i][0]-posKW[j][0])^2+Math.abs(posKB[i][1]-posKW[j][1])^2);
//				else
//					score+=1280-(Math.abs(posKB[i][0]-posKW[j][0])^2+Math.abs(posKB[i][1]-posKW[j][1])^2);
        score += (int) (Math.random() * RANDOM_WEIGHT);
        return score;

    }

    public static int EvalPrimaOK(int[][] position) {
        final int CHECKER = 100;
        final int POS = 1;
        final int KING = 4000;
        final int SPALLE_COPERTE = 100;
        int score = 0;
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++) {
                if (position[i][j] == Checkers.WHITE) {
                    score -= CHECKER;
                    score -= POS * i * i;

                    score -= supportoWHITE(position, i, j);
                } else if (position[i][j] == Checkers.BLACK) {
                    score += CHECKER;
                    score += POS * (7 - i) * (7 - i);
                    score += supportoBLACK(position, i, j);
                } else if (position[i][j] == Checkers.WKING) {
                    score -= KING;
                    if (i == 0 || i == 7)
                        score += EDGE;
                    if (j == 0 || j == 7)
                        score += EDGE;
                } else if (position[i][j] == Checkers.BKING) {
                    score += KING;
                    if (i == 0 || i == 7)
                        score -= EDGE;
                    if (j == 0 || j == 7)
                        score -= EDGE;
                }

            }//end for
        score += (int) (Math.random() * RANDOM_WEIGHT);
        return score;

    }


    public static int EvalOriginale(int[][] position) //originale
    {
        int score = 0;

        for (int posR = 0; posR < 8; posR++)
            for (int posC = 0; posC < 8; posC++) {
                if (position[posR][posC] == Checkers.WHITE) {
                    score -= CHECKER;
                    score -= POS * posR * posR;
                } else if (position[posR][posC] == Checkers.WKING) {
                    score -= KING;
                    if (posR == 0 || posR == 7)
                        score += EDGE;
                    if (posC == 0 || posC == 7)
                        score += EDGE;
                } else if (position[posR][posC] == Checkers.BKING) {
                    score += KING;
                    if (posR == 0 || posR == 7)
                        score -= EDGE;
                    if (posC == 0 || posC == 7)
                        score -= EDGE;
                } else if (position[posR][posC] == Checkers.BLACK) {
                    score += CHECKER;
                    score += POS * (7 - posR) * (7 - posR);
                }
            }//end for
        score += (int) (Math.random() * RANDOM_WEIGHT);
        return score;
    }//end Evaluation

    public static int EvalSoloPedine(int[][] position) //originale
    {
        int score = 0;

        for (int posR = 0; posR < 8; posR++)
            for (int posC = 0; posC < 8; posC++) {
                if (position[posR][posC] == Checkers.WHITE) {
                    score -= CHECKER;
                    score -= POS * posR * posR;
                } else if (position[posR][posC] == Checkers.WKING) {
                    score -= KING;
                } else if (position[posR][posC] == Checkers.BKING) {
                    score += KING;
                } else if (position[posR][posC] == Checkers.BLACK) {
                    score += CHECKER;
                    score += POS * (7 - posR) * (7 - posR);
                }
            }//end for
        score += (int) (Math.random() * RANDOM_WEIGHT);
        return score;
    }//end Evaluation

    static int opponent(int turn) {
        return turn == Checkers.BLACK ? Checkers.WHITE : Checkers.BLACK;
    }

    static int which_turn(int turn) {
        return Move.color(turn) == Checkers.BLACK ? -INFINITY : INFINITY;
    }

    public static int MiniMax(int[][] board, int depth, int maxDepth, Vector<int[]> theMove, int player, int[] counter, int BFunction, int WFunction, Board scacchiera) {
        //  Move.trasponiBoard(board);
        int firstTurn = player;

        //System.out.println("GIOCA PLAYER:"+player);

        int[][] newBoard = new int[8][8];
        int score;
        Vector<Vector<int[]>> moves = new Vector<Vector<int[]>>();
        int bestMax = -INFINITY;
        int pippo = bestMax;
        int bestMin = INFINITY;

        moves = Move.generate_moves(board, player);
        if (moves.size() > 1)       //non ho una sola mossa obbligata
            for (int ind = 0; ind < moves.size(); ind++) {
                newBoard = copy_board(board);
                Move.eseguiMossa(newBoard, moves.elementAt(ind), false, scacchiera);
                score = minimize(firstTurn, newBoard, depth + 1, maxDepth, opponent(player), counter, bestMin, bestMax, BFunction, WFunction, scacchiera);
                counter[0]++;
                if (score >= pippo) {
                    pippo = score;
                    theMove.clear();
                    for (int h = 0; h < moves.elementAt(ind).size(); h++)
                        theMove.add(h, moves.elementAt(ind).get(h));
                }
                if (pippo >= bestMin)
                    return pippo;
                bestMax = Math.max(bestMax, pippo);
            }
        else if (moves.size() == 1) {
            theMove.clear();
            for (int h = 0; h < moves.elementAt(0).size(); h++)
                theMove.add(h, moves.elementAt(0).get(h));
        }

//	  if(moves.size() >= 1)
//		  System.out.println("MOSSA MINIMAX: ("+theMove.elementAt(0)[0]+","+theMove.elementAt(0)[1]+")->("+theMove.elementAt(1)[0]+","+theMove.elementAt(1)[1]+")");
//	  else
//		  System.out.println("MOSSA MINIMAX: NESSUNA MOSSA");	
        return pippo;
    }

    static boolean quiescent(int[][] board, int player, Vector<Vector<int[]>> moves) {
        //Vector<Vector<int[]>> moves=new Vector<Vector<int[]>>();
        moves = Move.generate_moves(board, player);
        if (moves.size() == 0) { //non ci sono mosse possibili
            //System.out.println("QUIESCENTE. PLAYER: "+player);
            return true;
        } else if (Math.abs(moves.elementAt(0).elementAt(0)[0] - moves.elementAt(0).elementAt(1)[0]) == 1) { //non ci sono catture
            //System.out.println("QUIESCENTE. PLAYER: "+player);
            return true;
        }
        //System.out.println("NON QUIESCENTE. PLAYER: "+player);
        return false;
//return true;
    }

    static int maximize(int firstTurn, int[][] board, int depth, int maxDepth, int player, int[] counter, int bestMin, int bestMax, int BFunction, int WFunction, Board scacchiera) {
        // System.out.println("maximize depth:"+depth);
        int[][] newBoard = new int[8][8];
        int score;
        Vector<Vector<int[]>> moves = new Vector<Vector<int[]>>();
        moves = null;

        if (depth >= maxDepth && quiescent(board, player, moves))
            if (firstTurn == Checkers.WHITE)
                switch (WFunction) {
                    case 0:
                        return -EvalOriginale(board);
                    case 1:
                        return -EvalPrima(board, firstTurn);
                    case 2:
                        return -Evaluation2(board);
                    case 3:
                        return -EvalSpettacolo(board, Move.color(player));
                    case 4:
                        return -EvalSoloPedine(board);
                    case 5:
                        return -Evaluation2Compl(board);
                    default:
                        return -EvalOriginale(board);
                }
            else
                switch (BFunction) {
                    case 0:
                        return EvalOriginale(board);
                    case 1:
                        return EvalPrima(board, firstTurn);
                    case 2:
                        return Evaluation2(board);
                    case 3:
                        return EvalSpettacolo(board, Move.color(player));
                    case 4:
                        return EvalSoloPedine(board);
                    case 5:
                        return Evaluation2Compl(board);
                    default:
                        return EvalOriginale(board);
                }
        else { //branch
            score = -INFINITY;
            if (moves == null)
                moves = Move.generate_moves(board, player);
            for (int ind = 0; ind < moves.size(); ind++) {
                newBoard = copy_board(board);
                Move.eseguiMossa(newBoard, moves.elementAt(ind), false, scacchiera);

                score = Math.max(score, minimize(firstTurn, newBoard, depth + 1, maxDepth, opponent(player), counter, bestMin, bestMax, BFunction, WFunction, scacchiera));
                counter[0]++;
                if (score >= bestMin)
                    return score;
                bestMax = Math.max(bestMax, score);
            }
            return score;
        }
    } //end maximize

    static int minimize(int firstTurn, int[][] board, int depth, int maxDepth, int player, int[] counter, int bestMin, int bestMax, int BFunction, int WFunction, Board scacchiera) {
        //  System.out.println("minimize depth:"+depth);
        int[][] newBoard = new int[8][8];
        int score;
        Vector<Vector<int[]>> moves = new Vector<Vector<int[]>>();

        moves = null;

        if (depth >= maxDepth && quiescent(board, player, moves))
            if (firstTurn == Checkers.WHITE)
                switch (WFunction) {
                    case 0:
                        return -EvalOriginale(board);
                    case 1:
                        return -EvalPrima(board, firstTurn);
                    case 2:
                        return -Evaluation2(board);
                    case 3:
                        return -EvalSpettacolo(board, Move.color(player));
                    case 4:
                        return -EvalSoloPedine(board);
                    case 5:
                        return -Evaluation2Compl(board);
                    default:
                        return -EvalOriginale(board);
                }
            else
                switch (BFunction) {
                    case 0:
                        return EvalOriginale(board);
                    case 1:
                        return EvalPrima(board, firstTurn);
                    case 2:
                        return Evaluation2(board);
                    case 3:
                        return EvalSpettacolo(board, Move.color(player));
                    case 4:
                        return EvalSoloPedine(board);
                    case 5:
                        return Evaluation2Compl(board);
                    default:
                        return EvalOriginale(board);
                }
        else { //branch
            score = INFINITY;
            if (moves == null)
                moves = Move.generate_moves(board, player);
            for (int ind = 0; ind < moves.size(); ind++) {
                newBoard = copy_board(board);
                Move.eseguiMossa(newBoard, moves.elementAt(ind), false, scacchiera);
                score = Math.min(score, maximize(firstTurn, newBoard, depth + 1, maxDepth, opponent(player), counter, bestMin, bestMax, BFunction, WFunction, scacchiera));
                counter[0]++;
                if (score <= bestMax)
                    return score;
                bestMin = Math.min(bestMin, score);
            }
            return score;
        }
    } //end minimize


    static int[][] copy_board(int[][] board) {
        int[][] copy = new int[8][8];

        for (int posR = 0; posR < 8; posR++)
            for (int posC = 0; posC < 8; posC++)
                copy[posR][posC] = board[posR][posC];
        return copy;
    }//end copy_board

    static boolean better(int the_score, int best, int turn) {
        if (turn == Checkers.BLACK)
            return the_score > best;
        return the_score < best;
    }//end better
}//end class engine

