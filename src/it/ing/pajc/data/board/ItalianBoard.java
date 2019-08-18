package it.ing.pajc.data.board;

import it.ing.pajc.data.movements.GenericTree;
import it.ing.pajc.data.movements.GenericTreeNode;
import it.ing.pajc.data.movements.GenericTreeTraversalOrderEnum;
import it.ing.pajc.data.movements.Position;
import it.ing.pajc.data.pieces.*;
import it.ing.pajc.data.pieces.black.BlackKing;
import it.ing.pajc.data.pieces.black.BlackMan;
import it.ing.pajc.data.pieces.white.WhiteKing;
import it.ing.pajc.data.pieces.white.WhiteMan;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.List;


/**
 * Creates and manage ItalianBoard
 */
public class ItalianBoard implements Board{
    private PiecesColors player;
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
                    piecesBoard[posR][posC] = new BlackMan(new Position(posR,posC));


        for (int posR = 5; posR < DIMENSION_ITALIAN_BOARD; posR++)
            for (int posC = 0; posC < DIMENSION_ITALIAN_BOARD; posC++)
                if ((posC + posR) % 2 == 0) {
                    piecesBoard[posR][posC] = new WhiteMan(new Position(posR,posC));
                }
        //putting object of type Empty in the empty spaces
        for (int posR = 0; posR < DIMENSION_ITALIAN_BOARD; posR++)
            for (int posC = 0; posC < DIMENSION_ITALIAN_BOARD; posC++)
                if(piecesBoard[posR][posC]==null)
                    piecesBoard[posR][posC]=new Empty(new Position(posR,posC));
        initializeBoardFX();
    }

    public ItalianBoard(String fen,PiecesColors color) {
        player = color;
        if(player==PiecesColors.WHITE)
            piecesBoard = fenToMultidimensionalArray(fen);
        else{
            StringBuilder reverseFen = new StringBuilder();

            for(int i = fen.length() - 1; i >= 0; i--)
            {
                reverseFen.append(fen.charAt(i));
            }
            piecesBoard = fenToMultidimensionalArray(reverseFen.toString());
        }


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
                int finalY1 = y;
                int finalX1 = x;
                stackPaneBoard[x][y].setOnMousePressed(new EventHandler<MouseEvent>() {
                    private int a;
                    @Override
                    public void handle(MouseEvent event) {
                        System.out.println(piecesBoard[finalY1][finalX1].isDisable());
                        if (piecesBoard[finalY1][finalX1].isDisable()==false) {
                            System.out.println("final x:"+finalY1+" final y:"+finalX1);
                            System.out.println("ini x:"+clickedPieceR+" ini y:"+clickedPieceC);
                            move(new Position(clickedPieceR,clickedPieceC),new Position(finalY1,finalX1));
                            printBoardConsole();
                            resetBoardFXColors();
                            placeboard(gridPane,player);

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

    /**
     * Creation of pieces on the board graphically
     * @param grid Defines the positions on whom the pieces can move
     * @param player Defines the color of the pieces of a specific player
     */
    public void placeboard(GridPane grid,PiecesColors player){
        gridPane = grid;
        Circle circle;
        grid.getChildren().clear();
        for(int i=0;i<DIMENSION_ITALIAN_BOARD;i++){
            for (int j=0;j<DIMENSION_ITALIAN_BOARD;j++){
                grid.add(stackPaneBoard[i][j], i,j);

                if(piecesBoard[j][i].getPlayer()!=PiecesColors.EMPTY){
                    if(piecesBoard[j][i].getPlayer()== PiecesColors.WHITE) {
                        circle = piecesBoard[j][i];
                        circle.setFill(Color.WHITE);
                        circle.setRadius(29);
                        grid.add(circle, i, j);
                        if(this.player != piecesBoard[j][i].getPlayer())
                            circle.setDisable(true);
                    }

                    else {
                        circle = piecesBoard[j][i];
                        circle.setFill(Color.BLACK);
                        circle.setRadius(30);
                        grid.add(circle, i, j);
                        if(this.player != piecesBoard[j][i].getPlayer())
                            circle.setDisable(true);
                    }
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
                        GenericTree genericTree = ((Man) (piecesBoard[x][y])).bestCaptures(ItalianBoard.this);
                        System.out.println(genericTree.getNumberOfNodes());
                        List<GenericTreeNode> list = genericTree.build(GenericTreeTraversalOrderEnum.PRE_ORDER);
                        for(int p=1;p<list.size();p++) {
                            Position position = (Position) list.get(p).getData();
                            System.out.println(position.getPosR()+" "+position.getPosC());
                            stackPaneBoard[position.getPosC()][position.getPosR()].setDisable(false);
                            System.out.println(stackPaneBoard[position.getPosC()][position.getPosR()].isDisable());
                            stackPaneBoard[position.getPosC()][position.getPosR()].setId("captureHighlight");
                        }

                        genericTree = ((Man) (piecesBoard[x][y])).possibleMoves(ItalianBoard.this);
                        list = genericTree.build(GenericTreeTraversalOrderEnum.PRE_ORDER);
                        for(int p=1;p<list.size();p++) {
                            Position position = (Position) list.get(p).getData();
                            System.out.println(position.getPosR()+" "+position.getPosC());
                            stackPaneBoard[position.getPosC()][position.getPosR()].setDisable(false);
                            stackPaneBoard[position.getPosC()][position.getPosR()].setId("movementHighlight");
                        }
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

        if(piecesBoard[init.getPosR()][init.getPosC()].getPlayer()==PiecesColors.WHITE)
        piecesBoard[fin.getPosR()][fin.getPosC()] = new WhiteMan(fin);

        else if(piecesBoard[init.getPosR()][init.getPosC()].getPlayer()==PiecesColors.BLACK)
            piecesBoard[fin.getPosR()][fin.getPosC()] = new BlackMan(fin);

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

    /**
     * Creates a string where the whole board and the pieces postions are saved
     * @return The string generated or AKA FEN notion
     */
    @Override
    public String toString() {
        String fen="";
        int i = 0;
        for(int x=0; x < DIMENSION_ITALIAN_BOARD; x++){
            for(int y=0; y < DIMENSION_ITALIAN_BOARD; y++){
                    //fen+=piecesBoard[x][y].getClass().getName();
                    if (piecesBoard[x][y].getClass().getName() == "it.ing.pajc.data.pieces.black.BlackMan" || piecesBoard[x][y].getClass().getName() == "it.ing.pajc.data.pieces.white.WhiteMan")
                        if (piecesBoard[x][y].getPlayer() == PiecesColors.BLACK)
                            fen += "m";
                        else
                            fen += "M";
                    else if (piecesBoard[x][y].getClass().getName() == "it.ing.pajc.data.pieces.black.BlackKing" || piecesBoard[x][y].getClass().getName() == "it.ing.pajc.data.pieces.white.WhiteKing")
                        if (piecesBoard[x][y].getPlayer() == PiecesColors.BLACK)
                            fen += "k";
                        else
                            fen += "K";

                    else if (piecesBoard[x][y].getClass().getName() == "it.ing.pajc.data.pieces.Empty")
                            fen += "e";
            }
            fen+="/";
        }
        return fen;
    }

    /**
     * Creation of board from specific positions of each piece
     * @param fen Defines the postions of the pieces
     * @return Generated board
     */
    public static Pieces [] [] fenToMultidimensionalArray(String fen){
        int i = 0;
        Pieces pieces [][] = new Pieces [DIMENSION_ITALIAN_BOARD] [DIMENSION_ITALIAN_BOARD];
        int boardPosition = 0;
        do {

            if(fen.charAt(i) !='/') {
                switch (fen.charAt(i)) {

                    case 'm': {
                        pieces[boardPosition / 8][boardPosition % 8]  = new BlackMan(new Position(boardPosition / 8,boardPosition % 8));
                    }
                    break;
                    case 'k': {
                        pieces[boardPosition / 8][boardPosition % 8] = new BlackKing(new Position(boardPosition / 8,boardPosition % 8));
                    }
                    break;
                    //------------------------------------------------------------------
                    case 'M': {
                        pieces[boardPosition / 8][boardPosition % 8]  = new WhiteMan(new Position(boardPosition / 8,boardPosition % 8));
                    }
                    break;
                    case 'K': {
                        pieces[boardPosition / 8][boardPosition % 8]  = new WhiteKing(new Position(boardPosition / 8,boardPosition % 8));
                    }
                    break;
                    //------------------------------------------------------------------
                    case 'e':{
                        pieces[boardPosition / 8][boardPosition % 8]  = new Empty(new Position(boardPosition / 8,boardPosition % 8));
                    }
                }

                boardPosition++;
            }

            i++;
        }while (i< fen.length());

        return pieces;
    }
}

