package it.ing.pajc.singleplayer;

import it.ing.pajc.data.board.Fen;
import it.ing.pajc.data.board.ItalianBoard;
import it.ing.pajc.data.movements.*;
import it.ing.pajc.data.pieces.Pieces;
import it.ing.pajc.data.pieces.PiecesColors;
import it.ing.pajc.data.pieces.PiecesType;
import it.ing.pajc.data.pieces.italian.ItalianKing;
import it.ing.pajc.data.pieces.italian.ItalianMan;

import static it.ing.pajc.data.board.Board.DIMENSION_ITALIAN_BOARD;

public class Engine {
    public static ItalianBoard execute(ItalianBoard board) {
        int point = 13;
        Position initial = null;
        Position end = null;
        for (int i = 0; i < DIMENSION_ITALIAN_BOARD; i++) {
            for (int j = 0; j < DIMENSION_ITALIAN_BOARD; j++) {
                Pieces piece = board.getBoard()[i][j];

                if (board.getBoard()[i][j].getPlayer() == board.getPlayer()) {
                    GenericTree<Position> genericTree;

                    if (piece.getType() == PiecesType.MAN)
                        genericTree = ((ItalianMan) (piece)).possibleMoves(board);
                    else//TODO
                        genericTree = ((ItalianKing) (piece)).enginePossibleMoves(board);

                    System.err.println(genericTree);
                    try {
                        GenericTreeNode<Position> parent = genericTree.getRoot();

                        for(int x=0;x<=parent.getNumberOfChildren();x++){
                            Position fin = parent.getChildAt(x).getData();
                            int eval = evaluateMove((parent.getData()), (fin), board);
                            if(eval<=point){
                                point = eval;
                                initial = parent.getData();
                                end = fin;
                            }
                        }

                    }catch (Exception e){}
                }
            }
        }
        System.out.println("Selected piece "+ initial +" with points "+point);
        Move.executeMove(initial,end,board);
        return board;
    }


    public static int evaluateMove(Position init, Position fin, ItalianBoard board) {
        System.out.println(board.getPlayer());
        ItalianBoard tempBoard = new ItalianBoard(new Fen(board.toString()), board.getPlayer());
        Move.executeMove(init, fin, tempBoard);
        tempBoard.printBoardConsole();
        System.out.println();
        return countPiecesOnBoard(tempBoard, tempBoard.getPlayer());
    }

    public static int countPiecesOnBoard(ItalianBoard board, PiecesColors piecesColors) {
        int count = 0;
        for (int i = 0; i < DIMENSION_ITALIAN_BOARD; i++) {
            for (int j = 0; j < DIMENSION_ITALIAN_BOARD; j++) {
                if (board.getBoard()[i][j].getPlayer() != piecesColors && board.getBoard()[i][j].getPlayer() != PiecesColors.EMPTY)
                    count++;
            }
        }
        return count;
    }
}
