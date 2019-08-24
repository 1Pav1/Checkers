package it.ing.pajc.engine;

import it.ing.pajc.data.board.ItalianBoard;
import it.ing.pajc.data.movements.*;
import it.ing.pajc.data.pieces.PiecesColors;
import it.ing.pajc.data.pieces.PiecesType;

import java.util.*;

//BOARD: black on the bottom side
public class Engine {

    private final static int INFINITY = Integer.MAX_VALUE;
    private final static int CHECKER = 100; //one checker worth 100
    private final static int POS = 1;  //one position along the -j worth 1
    private final static int KING = 200; //a king's worth
    private final static int EDGE = 10; // effect of king being on the edge
    private final static int RANDOM_WEIGHT = 10; // weight factor

    private static final int[] tableWeight = {
            4, 4, 4, 4,
            4, 3, 3, 3,
            3, 2, 2, 4,
            4, 2, 1, 3,
            3, 1, 2, 4,
            4, 2, 2, 3,
            3, 3, 3, 4,
            4, 4, 4, 4};

    //DONE
    private static int EvalSpettacolo(ItalianBoard board, PiecesColors player) { //BOARD: black on the bottom side and valuation for black
        int score = 0;
        int numberOfPieces = 0;
        boolean kingFound = false;
        boolean manFound = false;
        final int LIMIT_PIECES = 18;
        final int WEIGHT_MAN = 100;
        final int WEIGHT_KING = 200;
        final int WEIGHT_INITIATIVE = 20;
        final int WEIGHT_RANDOM = 10;

        //controllo in che fase di gioco siamo
        for (int posR = 0; posR < 8 && !kingFound; posR++) {
            for (int posC = 0; posC < 8 && !kingFound; posC++) {
                if (board.getBoard()[posR][posC].getPlayer() != PiecesColors.EMPTY)
                    numberOfPieces++;
                if (board.getBoard()[posR][posC].getType() == PiecesType.KING)
                    kingFound = true;
            }
        }
        //INITIAL PHASE
        if (!kingFound || numberOfPieces > LIMIT_PIECES) {

            for (int posR = 0; posR < 8; posR++)
                for (int posC = 0; posC < 8; posC++) {
                    if (board.getBoard()[posR][posC].getPlayer() == PiecesColors.BLACK && board.getBoard()[posR][posC].getType() == PiecesType.MAN) {
                        if ((7 - posR) <= 5) //BLACK side
                            score += WEIGHT_MAN * (8 - posC) * (8 - posC) * (7 - posR) * (7 - posR);
                        else
                            score += WEIGHT_MAN * posC * posC * (7 - posR) * (7 - posR);
                    }
                    if (board.getBoard()[posR][posC].getPlayer() == PiecesColors.WHITE && board.getBoard()[posR][posC].getType() == PiecesType.MAN) {
                        if (posR <= 3) //WHITE side
                            score -= WEIGHT_MAN * (8 - posC) * (8 - posC) * posR * posR;
                        else
                            score -= WEIGHT_MAN * posC * posC * posR * posR;
                    }
                }
        }
        //FINAL PHASE
        else {
            int r = 0, c = 0;
            int numeroCoppiePari = 0;

            for (int posR = 0; posR < 8; posR++)
                for (int posC = 0; posC < 8; posC++) {
                    if (board.getBoard()[posR][posC].getPlayer() != PiecesColors.EMPTY) {
                        if (!manFound) {
                            r = posR;
                            c = posC;
                            manFound = true;
                        } else {
                            manFound = false;
                            if (Math.max(Math.abs(r - posR), Math.abs(c - posC)) % 2 == 0) { //distanza coppia e' pari
                                numeroCoppiePari++;
                            }
                        }
                        if (board.getBoard()[posR][posC].getPlayer() == PiecesColors.BLACK && board.getBoard()[posR][posC].getType() == PiecesType.MAN) {
                            score += WEIGHT_MAN * (7 - posR) * (7 - posR);
                        } else if (board.getBoard()[posR][posC].getPlayer() == PiecesColors.WHITE && board.getBoard()[posR][posC].getType() == PiecesType.MAN) {
                            score -= WEIGHT_MAN * posR * posR;
                        } else if (board.getBoard()[posR][posC].getPlayer() == PiecesColors.BLACK && board.getBoard()[posR][posC].getType() == PiecesType.KING) {
                            score += WEIGHT_KING;
                        } else if (board.getBoard()[posR][posC].getPlayer() == PiecesColors.WHITE && board.getBoard()[posR][posC].getType() == PiecesType.KING) {
                            score -= WEIGHT_KING;
                        }
                    }
                }
            if ((numeroCoppiePari % 2) == 1) //chi muove e' privilegiato
                if (player == PiecesColors.BLACK)  //player DEVE essere WHITE o BLACK
                    score += WEIGHT_INITIATIVE;
                else
                    score -= WEIGHT_INITIATIVE;
        }
        score += (int) (Math.random() * WEIGHT_RANDOM);
        return score;
    }

    //DONE
    private static int valutaPos(int posR, int posC, ItalianBoard board) {
        int value = 0;
        if (board.getBoard()[posR][posC].getType() == PiecesType.MAN) {
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
        } else // dama
            value = 10;

        return value * tableWeight[(posR / 2) * 4 + posC / 2] * posR * posR;
    }

    private static int Evaluation2Compl(ItalianBoard board) {
        int score = 0;

        for (int posR = 0; posR < 8; posR++)
            for (int posC = 0; posC < 8; posC++) {
                if (board.getBoard()[posR][posC].getPlayer() == PiecesColors.WHITE && board.getBoard()[posR][posC].getType() == PiecesType.MAN) {
                    score -= valutaPos(posR, posC, board);
                } else if (board.getBoard()[posR][posC].getPlayer() == PiecesColors.BLACK && board.getBoard()[posR][posC].getType() == PiecesType.MAN) {
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

    private static int Evaluation2(ItalianBoard board) { //Valuto dal punto di vista del NERO
        final int CHECKER = 150;
        final int POS = 1;
        final int KING = 300;
        int score = 0;

        for (int posR = 0; posR < 8; posR++)
            for (int posC = 0; posC < 8; posC++) {
                if (board.getBoard()[posR][posC].getPlayer() == PiecesColors.WHITE && board.getBoard()[posR][posC].getType() == PiecesType.MAN) {
                    score -= CHECKER;
                    score -= POS * posR * posR;
                    score -= supportoWHITE(board, posR, posC);
                } else if (board.getBoard()[posR][posC].getPlayer() == PiecesColors.BLACK && board.getBoard()[posR][posC].getType() == PiecesType.MAN) {
                    score += CHECKER;
                    score += POS * (7 - posR) * (7 - posR);
                    score += supportoBLACK(board, posR, posC);
                } else {
                    double calculationScore = (4 - Math.abs(3.5 - posR)) * (4 - Math.abs(3.5 - posR)) + (4 - Math.abs(3.5 - posC)) * (4 - Math.abs(3.5 - posC));
                    if (board.getBoard()[posR][posC].getPlayer() == PiecesColors.WHITE && board.getBoard()[posR][posC].getType() == PiecesType.KING) {
                        score -= KING;
                        score -= calculationScore;
                    } else if (board.getBoard()[posR][posC].getPlayer() == PiecesColors.BLACK && board.getBoard()[posR][posC].getType() == PiecesType.KING) {
                        score += KING;
                        score += calculationScore;
                    }
                }

            }//end for
        score += (int) (Math.random() * RANDOM_WEIGHT);
        return score;

    }

    private static int supportoBLACK(ItalianBoard board, int posR, int posC) {
        int score = 0;
        int tmp;

        if (!Move.inRange(posR, posC) || ((Move.inRange(posR + 1, posC - 1) && board.getBoard()[posR + 1][posC - 1].getPlayer() != PiecesColors.BLACK && board.getBoard()[posR + 1][posC - 1].getType() != PiecesType.MAN) &&
                (Move.inRange(posR + 1, posC + 1) && board.getBoard()[posR + 1][posC + 1].getPlayer() != PiecesColors.BLACK && board.getBoard()[posR + 1][posC + 1].getType() != PiecesType.MAN)))
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

    private static int supportoWHITE(ItalianBoard board, int posR, int posC) {
        int score = 0;
        int tmp;

        if (!Move.inRange(posR, posC) || ((Move.inRange(posR - 1, posC - 1) && board.getBoard()[posR - 1][posC - 1].getPlayer() != PiecesColors.WHITE && board.getBoard()[posR - 1][posC - 1].getType() != PiecesType.MAN)
                && (Move.inRange(posR - 1, posC + 1) && board.getBoard()[posR - 1][posC + 1].getPlayer() != PiecesColors.WHITE && board.getBoard()[posR - 1][posC + 1].getType() != PiecesType.MAN)))
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


    private static int EvalPrima(ItalianBoard board, PiecesColors player) {
        final int CHECKER = 100;
        final int POS = 1;
        final int KING = 200;
        int score = 0;
        int[][] posKW = new int[12][2];
        int[][] posKB = new int[12][2];
        int nKW = 0, nKB = 0;

        for (int posR = 0; posR < 8; posR++)
            for (int posC = 0; posC < 8; posC++) {
                if (board.getBoard()[posR][posC].getPlayer() == PiecesColors.WHITE && board.getBoard()[posR][posC].getType() == PiecesType.MAN) {
                    score -= CHECKER;
                    score -= POS * posR * posR;
                    score -= supportoWHITE(board, posR, posC);
                } else if (board.getBoard()[posR][posC].getPlayer() == PiecesColors.BLACK && board.getBoard()[posR][posC].getType() == PiecesType.MAN) {
                    score += CHECKER;
                    score += POS * (7 - posR) * (7 - posR);
                    score += supportoBLACK(board, posR, posC);
                } else if (board.getBoard()[posR][posC].getPlayer() == PiecesColors.WHITE && board.getBoard()[posR][posC].getType() == PiecesType.KING) {
                    posKW[nKW][0] = posR;
                    posKW[nKW][1] = posC;
                    nKW++;
                    score -= KING;
                    if (posR == 0 || posR == 7)
                        score += EDGE;
                    if (posC == 0 || posC == 7)
                        score += EDGE;
                } else if (board.getBoard()[posR][posC].getPlayer() == PiecesColors.BLACK && board.getBoard()[posR][posC].getType() == PiecesType.KING) {
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
        score += (int) (Math.random() * RANDOM_WEIGHT);
        return score;

    }

    public static int EvalPrimaOK(ItalianBoard board) {
        final int CHECKER = 100;
        final int POS = 1;
        final int KING = 4000;
        int score = 0;
        for (int posR = 0; posR < 8; posR++)
            for (int posC = 0; posC < 8; posC++) {
                if (board.getBoard()[posR][posC].getPlayer() == PiecesColors.WHITE && board.getBoard()[posR][posC].getType() == PiecesType.MAN) {
                    score -= CHECKER;
                    score -= POS * posR * posR;

                    score -= supportoWHITE(board, posR, posC);
                } else if (board.getBoard()[posR][posC].getPlayer() == PiecesColors.BLACK && board.getBoard()[posR][posC].getType() == PiecesType.MAN) {
                    score += CHECKER;
                    score += POS * (7 - posR) * (7 - posR);
                    score += supportoBLACK(board, posR, posC);
                } else if (board.getBoard()[posR][posC].getPlayer() == PiecesColors.WHITE && board.getBoard()[posR][posC].getType() == PiecesType.KING) {
                    score -= KING;
                    if (posR == 0 || posR == 7)
                        score += EDGE;
                    if (posC == 0 || posC == 7)
                        score += EDGE;
                } else if (board.getBoard()[posR][posC].getPlayer() == PiecesColors.BLACK && board.getBoard()[posR][posC].getType() == PiecesType.KING) {
                    score += KING;
                    if (posR == 0 || posR == 7)
                        score -= EDGE;
                    if (posC == 0 || posC == 7)
                        score -= EDGE;
                }

            }//end for
        score += (int) (Math.random() * RANDOM_WEIGHT);
        return score;

    }


    private static int EvalOriginale(ItalianBoard board) //originale
    {
        int score = 0;

        for (int posR = 0; posR < 8; posR++)
            for (int posC = 0; posC < 8; posC++) {
                if (board.getBoard()[posR][posC].getPlayer() == PiecesColors.WHITE && board.getBoard()[posR][posC].getType() == PiecesType.MAN) {
                    score -= CHECKER;
                    score -= POS * posR * posR;
                } else if (board.getBoard()[posR][posC].getPlayer() == PiecesColors.WHITE && board.getBoard()[posR][posC].getType() == PiecesType.KING) {
                    score -= KING;
                    if (posR == 0 || posR == 7)
                        score += EDGE;
                    if (posC == 0 || posC == 7)
                        score += EDGE;
                } else if (board.getBoard()[posR][posC].getPlayer() == PiecesColors.BLACK && board.getBoard()[posR][posC].getType() == PiecesType.KING) {
                    score += KING;
                    if (posR == 0 || posR == 7)
                        score -= EDGE;
                    if (posC == 0 || posC == 7)
                        score -= EDGE;
                } else if (board.getBoard()[posR][posC].getPlayer() == PiecesColors.BLACK && board.getBoard()[posR][posC].getType() == PiecesType.MAN) {
                    score += CHECKER;
                    score += POS * (7 - posR) * (7 - posR);
                }
            }//end for
        score += (int) (Math.random() * RANDOM_WEIGHT);
        return score;
    }//end Evaluation

    private static int EvalSoloPedine(ItalianBoard board) //originale
    {
        int score = 0;

        for (int posR = 0; posR < 8; posR++)
            for (int posC = 0; posC < 8; posC++) {
                if (board.getBoard()[posR][posC].getPlayer() == PiecesColors.WHITE && board.getBoard()[posR][posC].getType() == PiecesType.MAN) {
                    score -= CHECKER;
                    score -= POS * posR * posR;
                } else if (board.getBoard()[posR][posC].getPlayer() == PiecesColors.WHITE && board.getBoard()[posR][posC].getType() == PiecesType.KING) {
                    score -= KING;
                } else if (board.getBoard()[posR][posC].getPlayer() == PiecesColors.BLACK && board.getBoard()[posR][posC].getType() == PiecesType.KING) {
                    score += KING;
                } else if (board.getBoard()[posR][posC].getPlayer() == PiecesColors.BLACK && board.getBoard()[posR][posC].getType() == PiecesType.MAN) {
                    score += CHECKER;
                    score += POS * (7 - posR) * (7 - posR);
                }
            }//end for
        score += (int) (Math.random() * RANDOM_WEIGHT);
        return score;
    }//end Evaluation

    public static PiecesColors opponent(PiecesColors turn) {
        return turn == PiecesColors.BLACK ? PiecesColors.WHITE : PiecesColors.BLACK;
    }

    public static int MiniMax(ItalianBoard board, int depth, int maxDepth, Vector<int[]> theMove, PiecesColors player, int[] counter, int BFunction, int WFunction) {
        PiecesColors firstTurn = player;
        ItalianBoard newBoard;
        int score;
        ArrayList<GenericTree<Position>> moves;
        int bestMax = -INFINITY;
        int pippo = bestMax;
        int bestMin = INFINITY;

        moves = Move.generateMoves(board, player);
        if (moves.size() > 1)       //non ho una sola mossa obbligata
            for (int ind = 0; ind < moves.size(); ind++) {
                newBoard = copy_board(board);
                Move.eseguiMossa(newBoard, moves.elementAt(ind));
                score = minimize(firstTurn, newBoard, depth + 1, maxDepth, opponent(player), counter, bestMin, bestMax, BFunction, WFunction);
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

    public static boolean quiescent(ItalianBoard board, PiecesColors player, ArrayList<GenericTree<Position>> moves) {
        //Vector<Vector<int[]>> moves=new Vector<Vector<int[]>>();
        moves = Move.generateMoves(board, player);
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

    public static int maximize(PiecesColors firstTurn, ItalianBoard board, int depth, int maxDepth, PiecesColors player, int[] counter, int bestMin, int bestMax, int BFunction, int WFunction) {
        // System.out.println("maximize depth:"+depth);
        ItalianBoard newBoard = new ItalianBoard();
        int score;
        ArrayList<GenericTree<Position>> moves;
        moves = null;

        if (depth >= maxDepth && quiescent(board, player, moves))
            if (firstTurn == PiecesColors.WHITE)
                switch (WFunction) {
                    case 0:
                        return -EvalOriginale(board);
                    case 1:
                        return -EvalPrima(board, firstTurn);
                    case 2:
                        return -Evaluation2(board);
                    case 3:
                        return -EvalSpettacolo(board, player);
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
                        return EvalSpettacolo(board, player);
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
                moves = Move.generateMoves(board, player);
            for (int ind = 0; ind < moves.size(); ind++) {
                newBoard = copy_board(board);
                Move.eseguiMossa(newBoard, moves.elementAt(ind));

                score = Math.max(score, minimize(firstTurn, newBoard, depth + 1, maxDepth, opponent(player), counter, bestMin, bestMax, BFunction, WFunction));
                counter[0]++;
                if (score >= bestMin)
                    return score;
                bestMax = Math.max(bestMax, score);
            }
            return score;
        }
    } //end maximize

    public static int minimize(PiecesColors firstTurn, ItalianBoard board, int depth, int maxDepth, PiecesColors player, int[] counter, int bestMin, int bestMax, int BFunction, int WFunction) {
        //  System.out.println("minimize depth:"+depth);
        ItalianBoard newBoard = new ItalianBoard();
        int score;
        ArrayList<GenericTree<Position>> moves;

        moves = null;

        if (depth >= maxDepth && quiescent(board, player, moves))
            if (firstTurn == PiecesColors.WHITE)
                switch (WFunction) {
                    case 0:
                        return -EvalOriginale(board);
                    case 1:
                        return -EvalPrima(board, firstTurn);
                    case 2:
                        return -Evaluation2(board);
                    case 3:
                        return -EvalSpettacolo(board, player);
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
                        return EvalSpettacolo(board, player);
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
                moves = Move.generateMoves(board, player);
            for (int ind = 0; ind < moves.size(); ind++) {
                newBoard = copy_board(board);
                Move.eseguiMossa(newBoard, moves.elementAt(ind));
                score = Math.min(score, maximize(firstTurn, newBoard, depth + 1, maxDepth, opponent(player), counter, bestMin, bestMax, BFunction, WFunction));
                counter[0]++;
                if (score <= bestMax)
                    return score;
                bestMin = Math.min(bestMin, score);
            }
            return score;
        }
    } //end minimize


    public static ItalianBoard copy_board(ItalianBoard board) {
        ItalianBoard copy = new ItalianBoard();

        for (int posR = 0; posR < 8; posR++)
            for (int posC = 0; posC < 8; posC++)
                copy.getBoard()[posR][posC] = board.getBoard()[posR][posC];
        return copy;
    }//end copy_board

}//end class engine

