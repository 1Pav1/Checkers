package it.ing.pajc.model;

import it.ing.pajc.logic.*;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;
import javafx.scene.shape.Rectangle;


public class ItalianBoard implements Board{

    private Pieces[][] board;
    private Rectangle[][] boardFX;

    public ItalianBoard() {
        board = new Pieces[DIMENSION_ITALIAN_BOARD][DIMENSION_ITALIAN_BOARD];

        for (int posR = 0; posR < 3; posR++)
            for (int posC = 0; posC < DIMENSION_ITALIAN_BOARD; posC++)
                if ((posC + posR) % 2 == 0)
                    board[posR][posC] = new Man(PiecesColors.BLACK,new Position(posR,posC));
        for (int posR = 5; posR < DIMENSION_ITALIAN_BOARD; posR++)
            for (int posC = 0; posC < DIMENSION_ITALIAN_BOARD; posC++)
                if ((posC + posR) % 2 == 0)
                    board[posR][posC] = new Man(PiecesColors.WHITE,new Position(posR,posC));

        initializeBoardFX();
    }

    private void initializeBoardFX() {
        boardFX = new Rectangle[DIMENSION_ITALIAN_BOARD][DIMENSION_ITALIAN_BOARD];
        for (int x = 0; x < DIMENSION_ITALIAN_BOARD; x++) {
            for (int y = 0; y < DIMENSION_ITALIAN_BOARD; y++) {
                boardFX[x][y] = new Rectangle();
                boardFX[x][y].setWidth(60);
                boardFX[x][y].setHeight(60);
                boardFX[x][y].setX(60);
                boardFX[x][y].setY(60);
                boardFX[x][y].setStroke(javafx.scene.paint.Color.TRANSPARENT);
                boardFX[x][y].setStrokeType(StrokeType.INSIDE);
                boardFX[x][y].setStrokeWidth(1);
                int finalY = y;
                int finalX = x;
                int finalY1 = y;
                int finalX1 = x;
                boardFX[x][y].setOnMousePressed(event -> {
                    System.out.println("hello");
                    //resetBoardFXColors();
                    //int i = finalX;
                    //int j = finalY;
                    if (board[finalX1][finalY1].getFill() == Color.rgb(0, 255, 0)) {

                    }
                });

            }
        }
        resetBoardFXColors();

    }

    private void resetBoardFXColors(){
        for(int x=0; x < DIMENSION_ITALIAN_BOARD; x++){
            for(int j=0; j < DIMENSION_ITALIAN_BOARD; j++){
                if((x%2==0 && j%2==1) || (x%2==1 && j%2==0)){
                    boardFX[x][j].setFill(Color.rgb(204,183,174));
                }
                else if((x%2==0 && j%2==0) || (x%2==1 && j%2==1)){
                    boardFX[x][j].setFill(Color.rgb(112,102,119));
                }
            }
        }
    }

    public void placeboard(GridPane grid){
        Circle circle;
        for(int i=0;i<DIMENSION_ITALIAN_BOARD;i++){
            for (int j=0;j<DIMENSION_ITALIAN_BOARD;j++){
                grid.add(boardFX[i][j], i,j );
                grid.setHgap(0);
                grid.setVgap(0);
                if(board[j][i]!=null){
                    if(board[j][i].getPlayer()== PiecesColors.WHITE) {
                        circle = board[j][i];
                        circle.setFill(Color.WHITE);
                        circle.setRadius(30);
                        grid.add(circle, i, j);
                    }

                    else {
                        circle = board[j][i];
                        circle.setFill(Color.BLACK);
                        circle.setRadius(30);
                        grid.add(circle, i, j);
                    }
                    int finalJ = j;
                    int finalI = i;
                    circle.setOnMousePressed(event -> {
                        resetBoardFXColors();
                        int x = finalJ;
                        int y = finalI;
                        MovementList list = ((Man)(board[x][y])).possibleMoves(this);
                        for (Position position : list.getPossibleMoves())
                            boardFX[position.getPosC()][position.getPosR()].setFill(Color.rgb(0,255,0));
                    });
                }

            }
        }
    }

    @Override
    public void printBoardConsole(){
        for(int posR=0;posR<DIMENSION_ITALIAN_BOARD;posR++) {
            for (int posC = 0; posC < DIMENSION_ITALIAN_BOARD; posC++) {
                if(board[posR][posC]==null)
                    System.out.print("[ ]");
                else {
                    if (board[posR][posC].getPlayer() == PiecesColors.BLACK)
                        System.out.print("[m]");
                    else if (board[posR][posC].getPlayer() == PiecesColors.WHITE)
                        System.out.print("[M]");

                }
            }
            System.out.println(" ");
        }
    }
    @Override
    public Pieces[][] getBoard() {
        return board;
    }

    @Override
    public void setBoard(Pieces[][] board) {
        this.board = board;
    }

}

