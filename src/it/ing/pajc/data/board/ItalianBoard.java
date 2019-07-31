package it.ing.pajc.data.board;

import it.ing.pajc.data.movements.MovementList;
import it.ing.pajc.data.movements.Position;
import it.ing.pajc.data.pieces.Empty;
import it.ing.pajc.data.pieces.Man;
import it.ing.pajc.data.pieces.Pieces;
import it.ing.pajc.data.pieces.PiecesColors;
import javafx.animation.TranslateTransition;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;
import javafx.scene.shape.Rectangle;

/**
 * Creates and manage ItalianBoard
 */
public class ItalianBoard implements Board{

    private Pieces[][] board;
    private Rectangle[][] boardFX;


    /**
     * ItalianBoard constructor.
     */
    public ItalianBoard() {
        board = new Pieces[DIMENSION_ITALIAN_BOARD][DIMENSION_ITALIAN_BOARD];

        for (int posR = 0; posR < 3; posR++)
            for (int posC = 0; posC < DIMENSION_ITALIAN_BOARD; posC++)
                if ((posC + posR) % 2 == 0)
                    board[posR][posC] = new Man(PiecesColors.BLACK,new Position(posR,posC));
        for (int posR = 5; posR < DIMENSION_ITALIAN_BOARD; posR++)
            for (int posC = 0; posC < DIMENSION_ITALIAN_BOARD; posC++)
                if ((posC + posR) % 2 == 0) {
                    board[posR][posC] = new Man(PiecesColors.WHITE,new Position(posR,posC));
                }
        //riempio gli spazi vuoti(null) con l'oggetto empty
        for (int posR = 0; posR < DIMENSION_ITALIAN_BOARD; posR++)
            for (int posC = 0; posC < DIMENSION_ITALIAN_BOARD; posC++)
                if(board[posR][posC]==null)
                    board[posR][posC]=new Empty(PiecesColors.EMPTY, new Position(posR,posC));

        initializeBoardFX();
    }

    /**
     * Board initializer using JavaFX.
     */
    private void initializeBoardFX() {
        boardFX = new Rectangle[DIMENSION_ITALIAN_BOARD][DIMENSION_ITALIAN_BOARD];
        for (int x = 0; x < DIMENSION_ITALIAN_BOARD; x++) {
            for (int y = 0; y < DIMENSION_ITALIAN_BOARD; y++) {

                boardFX[x][y] = new Rectangle();
                boardFX[x][y].setId("rectangle");
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
                    //resetBoardFXColors();
                    //int i = finalX;
                    //int j = finalY;
                    if (board[finalX1][finalY1].getFill().equals(Color.rgb(255, 255, 0))) {//Controlla warning perche' usi == la posto di equal per degli oggetti
                        System.out.println("ciao");
                    }
                });
            }
        }
        resetBoardFXColors();
    }

    /**
     * Reset colors of the pieces on the board
     */
    private void resetBoardFXColors(){
        for(int x=0; x < DIMENSION_ITALIAN_BOARD; x++){
            for(int j=0; j < DIMENSION_ITALIAN_BOARD; j++){
                //questo e' sempre vero controlla warning
                if((x%2==0 && j%2==1) || (x%2==1 && j%2==0)){
                    boardFX[x][j].setFill(Color.rgb(204,183,174));
                }
                else {
                    boardFX[x][j].setFill(Color.rgb(112, 102, 119));
                }
            }
        }
    }
    //TODO: COMMENTA TU
    public void placeboard(GridPane grid){
        Circle circle;
        for(int i=0;i<DIMENSION_ITALIAN_BOARD;i++){
            for (int j=0;j<DIMENSION_ITALIAN_BOARD;j++){
                grid.add(boardFX[i][j], i,j );
                grid.setPrefWidth(479);
                grid.setPrefHeight(479);
                grid.setHgap(0);
                grid.setVgap(0);
                if(board[j][i].getPlayer()!=PiecesColors.EMPTY){
                    if(board[j][i].getPlayer()== PiecesColors.WHITE) {
                        circle = board[j][i];
                        circle.setFill(Color.WHITE);
                        circle.setRadius(29);
                        grid.add(circle, i, j);
                    }

                    else {
                        circle = board[j][i];
                        circle.setFill(Color.BLACK);
                        circle.setStyle("-fx-background-image:  url(../../graphics/giphy.gif)");
                        circle.setRadius(30);
                        grid.add(circle, i, j);
                    }
                    int finalJ = j;
                    int finalI = i;
                    Circle finalCircle = circle;

                    circle.setOnMousePressed(event -> {
                        resetBoardFXColors();
                        TranslateTransition transition = new TranslateTransition();
                        transition.setToX(50);
                        transition.setToY(-50);
                        transition.setToZ(100);
                        transition.setNode(finalCircle);
                        transition.play();

                        int x = finalJ;
                        int y = finalI;
                        MovementList list = ((Man) (board[x][y])).possibleMoves(ItalianBoard.this);
                        for (Position position : list.getPossibleMoves())
                            boardFX[position.getPosC()][position.getPosR()].setFill(Color.rgb(255, 255, 0));
                    });
                }

            }
        }
    }


    /**
     * Change the position of a piece on the board.
     * @param init initial position
     * @param fin  final position
     */
    @Override
    public void move(Position init, Position fin) {
        board[init.getPosR()][init.getPosC()].setPosition(fin);
        board[init.getPosR()][init.getPosC()].setPlayer(PiecesColors.EMPTY);
    }

    /**
     * Print the board, only console.
     */
    @Override
    public void printBoardConsole(){
        for(int posR=0;posR<DIMENSION_ITALIAN_BOARD;posR++) {
            for (int posC = 0; posC < DIMENSION_ITALIAN_BOARD; posC++) {
                if(board[posR][posC].getPlayer()==PiecesColors.EMPTY)
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

    /**
     * @return The board
     */
    @Override
    public Pieces[][] getBoard() {
        return board;
    }

    /**
     * Set a new board of pieces.
     * @param board Of pieces
     */
    @Override
    public void setBoard(Pieces[][] board) {
        this.board = board;
    }

}

