package it.ing.pajc.data.movements;

import java.util.ArrayList;

/**
 * This class takes a movement list that a piece can make
 */
public class MovementList{
    TreeNode<Position> possibleMoves;

    public MovementList(Position startPos) {
        possibleMoves = new TreeNode<>(startPos);
    }
    public void addNewPossibleMove(Position pos){
        possibleMoves.addNewChild(pos);
    }

    public TreeNode<Position> getPossibleMoves() {
        return possibleMoves;
    }

    public void setPossibleMovements(TreeNode<Position> possibleMoves) {
        this.possibleMoves = possibleMoves;
    }
    //Manca metodo che ritorna l'arraylist delle uniche mosse possibili



}
