package it.ing.pajc.model;
import it.ing.pajc.logic.*;

public class Board {
    private Pieces [] [] pieces;

    public Board(BoardTypes type){
        if(type==BoardTypes.ITALIAN_CHECKERS) {
            this.pieces = new Pieces[8][8];
            for(int posR=0;posR<3;posR++)
                for(int posC=0;posC<8;posC++)
                    if((posC+posR)%2==0)
                        pieces[posR][posC]=new Man(PiecesColors.BLACK);
            for(int posR=5;posR<8;posR++)
                for(int posC=0;posC<8;posC++)
                    if((posC+posR)%2==0)
                        pieces[posR][posC]=new Man(PiecesColors.WHITE);

        }
        else if(type==BoardTypes.INTERNATIONAL_CHECKERS) {
            this.pieces = new Pieces[10][10];
            for(int posR=0;posR<4;posR++)
                for(int posC=0;posC<10;posC++)
                    if((posC+posR)%2==0)
                        pieces[posR][posC]=new Man(PiecesColors.BLACK);
            for(int posR=6;posR<10;posR++)
                for(int posC=0;posC<8;posC++)
                    if((posC+posR)%2==0)
                        pieces[posR][posC]=new Man(PiecesColors.WHITE);
        }

        printBoardConsole();
    }

    public void printBoardConsole(){
        for(int posR=0;posR<8;posR++) {
            for (int posC = 0; posC < 8; posC++) {
                if(pieces[posR][posC]==null)
                    System.out.print("[ ]");
                else {
                    if (pieces[posR][posC].getPlayer() == PiecesColors.BLACK)
                        System.out.print("[m]");
                    else if (pieces[posR][posC].getPlayer() == PiecesColors.WHITE)
                        System.out.print("[M]");

                }
            }
            System.out.println(" ");
        }
    }

}
