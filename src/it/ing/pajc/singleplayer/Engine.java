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
        int point = -1;
        Position initial = null;
        Position end = null;
        for (int i = 0; i < DIMENSION_ITALIAN_BOARD; i++) {
            for (int j = 0; j < DIMENSION_ITALIAN_BOARD; j++) {
                Pieces piece = board.getBoard()[i][j];

                if (board.getBoard()[i][j].getPlayer() == board.getPlayer()) {
                    GenericTree<Position> genericTree;


                    if (piece.getType() == PiecesType.MAN)
                        genericTree = ((ItalianMan) (piece)).possibleMoves(board);
                    else
                        genericTree = ((ItalianKing) (piece)).possibleMoves(board);

                    System.out.println(genericTree);
                    try {
                        GenericTreeNode<Position> parent = genericTree.getRoot();
                        for(int x=0;x<=parent.getNumberOfChildren();x++){
                            Position fin = (Position) parent.getChildAt(x).getData();
                            if(evaluateMove((parent.getData()), (fin), board)>point){
                                initial = parent.getData();
                                end = fin;
                            }
                        }

                    }catch (Exception e){}


                }
            }
        }
        Move.executeMove(initial,end,board);
        return board;
    }


    public static int evaluateMove(Position init, Position fin, ItalianBoard board) {
        ItalianBoard tempBoard = new ItalianBoard(new Fen(board.toString()), board.getPlayer());
        Move.executeMove(init, fin, tempBoard);
        return countPiecesOnBoard(board, board.getPlayer());
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
