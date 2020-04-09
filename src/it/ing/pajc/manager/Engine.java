package it.ing.pajc.manager;

import it.ing.pajc.controller.Controller;
import it.ing.pajc.data.board.ItalianBoard;
import it.ing.pajc.data.movements.*;
import it.ing.pajc.data.pieces.PlaceType;
import it.ing.pajc.data.pieces.PieceType;

import static it.ing.pajc.data.board.Board.DIMENSION_ITALIAN_BOARD;
//TODO PENSACI TU
class Engine {
/*
    
    public static ItalianBoard execute(ItalianBoard board) {
        int point = 13;
        Position initial = null;
        Position end = null;
        for (int i = 0; i < DIMENSION_ITALIAN_BOARD; i++) {
            for (int j = 0; j < DIMENSION_ITALIAN_BOARD; j++) {
                Pieces piece = board.getBoard()[i][j];

                if (board.getBoard()[i][j].getPlayer() == board.getPlayer()) {
                    GenericTree<Position> genericTree;

                    if (piece.getType() == PieceType.MAN)
                        genericTree = ((ItalianMan) (piece)).possibleMoves(board);
                    else//TODO
                        genericTree = ((ItalianKing) (piece)).enginePossibleMoves(board);

                    //System.err.println(genericTree);
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
        if(initial!=null && end !=null) {
            System.out.println("Selected piece " + initial + " with points " + point);
            if (end.getcPosC() == -1 && end.getcPosR() == -1)
                Controller.addToTextArea("Engine Moving to: Position r:" + end.getPosR() + " c:" + end.getPosC() + "\n");
            Move.executeMove(initial, end, board);
        }
        return board;
    }


    public static int evaluateMove(Position init, Position fin, ItalianBoard board) {
        Fen fen = new Fen(board.toString());
        if(board.getPlayer() == PlaceType.BLACK)
            fen.reverseFen();
        ItalianBoard tempBoard = new ItalianBoard(fen, board.getPlayer());
        Move.executeMove(init, fin, tempBoard);
        //tempBoard.printBoardConsole();
        System.out.println();
        return countPiecesOnBoard(tempBoard, tempBoard.getPlayer());
    }

    public static int countPiecesOnBoard(ItalianBoard board, PlaceType piecesColors) {
        int count = 0;
        for (int i = 0; i < DIMENSION_ITALIAN_BOARD; i++) {
            for (int j = 0; j < DIMENSION_ITALIAN_BOARD; j++) {
                if (board.getBoard()[i][j].getPlayer() != piecesColors && board.getBoard()[i][j].getPlayer() != PlaceType.EMPTY)
                    count++;
            }
        }
        return count;
    }
    */

}
