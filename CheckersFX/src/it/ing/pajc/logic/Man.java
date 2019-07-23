package it.ing.pajc.logic;

import it.ing.pajc.model.InternationalBoard;
import it.ing.pajc.model.ItalianBoard;

import java.util.ArrayList;

public class Man extends Pieces {
    public Man(PiecesColors player,Position pos){
        super(player,pos);
    }


    /**
     * This method calculates empty spaces close to the selected Man.
     * Managing the outOfBoundException(Not the nullPointerException)
     * @param board The board in question
     * @return Possible moves(only empty spaces)
     */
    public MovementList possibleMoves(ItalianBoard board){

        MovementList possibleMovementList = new MovementList();
        System.out.println();System.out.println();System.out.println();

        int posRow = this.getPosition().getPosR();
        int posColumn = this.getPosition().getPosC();
        if(getPlayer()==PiecesColors.WHITE) {
            try {
                if (board.getBoard()[posRow - 1][posColumn - 1] == null)
                    possibleMovementList.addMovement(new Position( posRow- 1, posColumn- 1));

                if (board.getBoard()[posRow - 1][posColumn + 1] == null)
                    possibleMovementList.addMovement(new Position(posRow - 1,posColumn - 1));
            } catch (ArrayIndexOutOfBoundsException a) { }
        }

        else{
            try {
                if (board.getBoard()[posRow + 1][posColumn + 1] == null)
                    possibleMovementList.addMovement(new Position(posRow + 1,posColumn + 1));

                if (board.getBoard()[posRow + 1][posColumn - 1] == null)
                    possibleMovementList.addMovement(new Position(posRow + 1,posColumn - 1));
            } catch (ArrayIndexOutOfBoundsException a) { }
        }

        return possibleMovementList;
    }

    public void possibleMoves(InternationalBoard board){

    }

    public void possibleCapture(ItalianBoard board){
    }

}


