package it.ing.pajc.data.board;

import it.ing.pajc.controller.CheckerBoardController;
import it.ing.pajc.data.movements.Position;
import it.ing.pajc.data.pieces.Empty;
import it.ing.pajc.data.pieces.Man;
import it.ing.pajc.data.pieces.Pieces;
import it.ing.pajc.data.pieces.PiecesColors;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;


/**
 * Creates and manage ItalianBoard
 */
public class ItalianBoard implements Board{

    private Pieces[][] piecesBoard;
    
    private StackPane[][] stackPaneBoard;

    private int clickedPieceR;
    private int clickedPieceC;
    private GridPane gridPane;
    /**
     * ItalianBoard constructor.
     */

    public ItalianBoard() {
        piecesBoard = new Pieces[DIMENSION_ITALIAN_BOARD][DIMENSION_ITALIAN_BOARD];

        for (int posR = 0; posR < 3; posR++)
            for (int posC = 0; posC < DIMENSION_ITALIAN_BOARD; posC++)
                if ((posC + posR) % 2 == 0)
                    piecesBoard[posR][posC] = new Man(PiecesColors.BLACK,new Position(posR,posC));
        for (int posR = 5; posR < DIMENSION_ITALIAN_BOARD; posR++)
            for (int posC = 0; posC < DIMENSION_ITALIAN_BOARD; posC++)
                if ((posC + posR) % 2 == 0) {
                    piecesBoard[posR][posC] = new Man(PiecesColors.WHITE,new Position(posR,posC));
                }
        //riempio gli spazi vuoti(null) con l'oggetto empty
        for (int posR = 0; posR < DIMENSION_ITALIAN_BOARD; posR++)
            for (int posC = 0; posC < DIMENSION_ITALIAN_BOARD; posC++)
                if(piecesBoard[posR][posC]==null)
                    piecesBoard[posR][posC]=new Empty(PiecesColors.EMPTY, new Position(posR,posC));

        initializeBoardFX();
    }

    /**
     * Board initializer using JavaFX.
     */
    private void initializeBoardFX() {
        stackPaneBoard = new StackPane[DIMENSION_ITALIAN_BOARD][DIMENSION_ITALIAN_BOARD];
        for (int x = 0; x < DIMENSION_ITALIAN_BOARD; x++) {
            for (int y = 0; y < DIMENSION_ITALIAN_BOARD; y++) {
                stackPaneBoard[x][y] = new StackPane();
                stackPaneBoard[x][y].setPrefWidth(60);
                stackPaneBoard[x][y].setPrefHeight(60);

                int finalY = y;
                int finalX = x;
                int finalY1 = y;
                int finalX1 = x;
                stackPaneBoard[x][y].setOnMousePressed(new EventHandler<MouseEvent>() {
                    public int a;
                    @Override
                    public void handle(MouseEvent event) {
                        if (piecesBoard[finalX1][finalY1].isDisable()) {//Controlla warning perche' usi == la posto di equal per degli oggetti
                            System.out.println("final x:"+finalY1+" final y:"+finalX1);
                            System.out.println("ini x:"+clickedPieceR+" ini y:"+clickedPieceC);
                            move(new Position(clickedPieceR,clickedPieceC),new Position(finalY1,finalX1));
                            printBoardConsole();
                            resetBoardFXColors();
                            placeboard(gridPane,PiecesColors.WHITE);

                        }
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
            for(int y=0; y < DIMENSION_ITALIAN_BOARD; y++){
                stackPaneBoard[x][y].setDisable(true);
                //questo e' sempre vero controlla warning
                if((x%2==0 && y%2==1) || (x%2==1 && y%2==0)){
                    stackPaneBoard[x][y].setId("lightSquare");
                }
                else {
                    stackPaneBoard[x][y].setId("darkSquare");
                }
            }
        }
    }
    //TODO: COMMENTA TU
    public void placeboard(GridPane grid,PiecesColors player){
        gridPane = grid;
        Circle circle;
        grid.getChildren().clear();
        for(int i=0;i<DIMENSION_ITALIAN_BOARD;i++){
            for (int j=0;j<DIMENSION_ITALIAN_BOARD;j++){
                grid.add(stackPaneBoard[i][j], i,j );
                if(piecesBoard[j][i].getPlayer()!=PiecesColors.EMPTY){
                    if(piecesBoard[j][i].getPlayer()== PiecesColors.WHITE) {
                        circle = piecesBoard[j][i];
                        circle.setFill(Color.WHITE);
                        circle.setRadius(29);
                        grid.add(circle, i, j);
                        if(player != PiecesColors.WHITE)
                            circle.setDisable(true);
                    }

                    else {
                        circle = piecesBoard[j][i];
                        circle.setFill(Color.BLACK);
                        circle.setRadius(30);
                        grid.add(circle, i, j);
                        if(player != PiecesColors.BLACK)
                            circle.setDisable(true);
                    }
                    CheckerBoardController.method(i,j,circle);
                    int finalJ = j;
                    int finalI = i;
                    Circle finalCircle = circle;
                    circle.setOnMousePressed(event -> {
                        resetBoardFXColors();
                        /*
                        TranslateTransition transition = new TranslateTransition();
                        transition.setToX(50);
                        transition.setToY(-50);
                        transition.setToZ(100);
                        transition.setNode(finalCircle);
                        transition.play();
                         */
                        clickedPieceR=finalJ;
                        clickedPieceC=finalI;
                        int x = finalJ;
                        int y = finalI;
                        //MovementList list = ((Man) (piecesBoard[x][y])).possibleMoves(ItalianBoard.this);
                        /*for (Position position : list.getPossibleMoves()) {
                            stackPaneBoard[position.getPosC()][position.getPosR()].setDisable(false);
                            stackPaneBoard[position.getPosC()][position.getPosR()].setId("highlighted");

                        }*/
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
        piecesBoard[fin.getPosR()][fin.getPosC()] = new Man(piecesBoard[init.getPosR()][init.getPosC()].getPlayer(),fin);
        System.out.println(fin.getPosR());
        System.out.println(fin.getPosC());
        System.out.println(piecesBoard[fin.getPosR()][fin.getPosC()].getPlayer());
        piecesBoard[init.getPosR()][init.getPosC()].setPlayer(PiecesColors.EMPTY);
    }

    /**
     * Print the board, only console.
     */
    @Override
    public void printBoardConsole(){
        for(int posR=0;posR<DIMENSION_ITALIAN_BOARD;posR++) {
            for (int posC = 0; posC < DIMENSION_ITALIAN_BOARD; posC++) {
                if(piecesBoard[posR][posC].getPlayer()==PiecesColors.EMPTY)
                    System.out.print("[ ]");
                else {
                    if (piecesBoard[posR][posC].getPlayer() == PiecesColors.BLACK)
                        System.out.print("[m]");
                    else if (piecesBoard[posR][posC].getPlayer() == PiecesColors.WHITE)
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
        return piecesBoard;
    }

    /**
     * Set a new board of pieces.
     * @param board Of pieces
     */
    @Override
    public void setBoard(Pieces[][] board) {
        this.piecesBoard = board;
    }

}

