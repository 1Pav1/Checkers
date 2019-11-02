package it.ing.pajc.singleplayer;

import it.ing.pajc.data.board.ItalianBoard;
import it.ing.pajc.data.movements.*;
import it.ing.pajc.data.pieces.Pieces;
import it.ing.pajc.data.pieces.PiecesType;
import it.ing.pajc.data.pieces.italian.ItalianKing;
import it.ing.pajc.data.pieces.italian.ItalianMan;

import static it.ing.pajc.data.board.Board.DIMENSION_ITALIAN_BOARD;

public class Engine {
    public static ItalianBoard execute(ItalianBoard board){
        for (int i = 0; i < DIMENSION_ITALIAN_BOARD; i++) {
            for (int j = 0; j < DIMENSION_ITALIAN_BOARD; j++) {
                Pieces piece = board.getBoard()[i][j];
                if(board.getBoard()[i][j].getPlayer() == board.getPlayer()) {
                    GenericTree<Position> genericTree;
                    if (piece.getType() == PiecesType.MAN)
                        genericTree = ((ItalianMan) (piece)).possibleMoves(board);
                    else
                        genericTree = ((ItalianKing) (piece)).possibleMoves(board);
                    System.out.println(genericTree);
                    try {
                        GenericTreeNode<Position> parent = genericTree.getRoot();
                        Position fin = (Position) parent.getChildAt(0).getData();
                        Move.executeMove((parent.getData()),(fin),board);
                        return board;
                    }catch (Exception e){};

                }
            }
        }
        return  board;
    }
}
