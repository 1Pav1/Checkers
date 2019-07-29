package it.ing.pajc.logic;

import java.util.ArrayList;

public class MovementList {
    ArrayList<Position> possibleMoves;

    public MovementList() {
        possibleMoves = new ArrayList<>();
    }

    public ArrayList<Position> getPossibleMoves() {
        return possibleMoves;
    }

    public void setPossibleMovements(ArrayList<Position> possibleMoves) {
        this.possibleMoves = possibleMoves;
    }

    public void addMovement(Position pos){
        possibleMoves.add(pos);
    }

    public int searchMovement(Position pos){
        for(int i=0;i<possibleMoves.size();i++) {
            if(possibleMoves.get(i).getPosR()==pos.getPosR() && possibleMoves.get(i).getPosC()==pos.getPosC())
                return i;
        }
        return -1;
    }

}