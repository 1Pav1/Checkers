package it.ing.pajc.data.board;

import it.ing.pajc.data.movements.*;
import it.ing.pajc.data.pieces.*;
import it.ing.pajc.data.pieces.italian.*;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.util.List;


/**
 * Creates and manage ItalianBoard
 */
public class ItalianBoard implements Board {
    private PiecesColors player;

    private Pieces[][] piecesBoard;

    private StackPane[][] stackPaneBoard;

    private GridPane gridPane;


    public PiecesColors getPlayer() {
        return player;
    }

    public Pieces[][] getPiecesBoard() {
        return piecesBoard;
    }

    public StackPane[][] getStackPaneBoard() {
        return stackPaneBoard;
    }

    public GridPane getGridPane() {
        return gridPane;
    }
    /**
     * ItalianBoard constructor.
     */
    public ItalianBoard() {
        piecesBoard = new Pieces[DIMENSION_ITALIAN_BOARD][DIMENSION_ITALIAN_BOARD];
        for (int posR = 0; posR < 3; posR++)
            for (int posC = 0; posC < DIMENSION_ITALIAN_BOARD; posC++)
                if ((posC + posR) % 2 == 0)
                    piecesBoard[posR][posC] = new ItalianMan(new Position(posR, posC), PiecesColors.BLACK);


        for (int posR = 5; posR < DIMENSION_ITALIAN_BOARD; posR++)
            for (int posC = 0; posC < DIMENSION_ITALIAN_BOARD; posC++)
                if ((posC + posR) % 2 == 0) {
                    piecesBoard[posR][posC] = new ItalianMan(new Position(posR, posC), PiecesColors.WHITE);
                }
        //putting object of type Empty in the empty spaces
        for (int posR = 0; posR < DIMENSION_ITALIAN_BOARD; posR++)
            for (int posC = 0; posC < DIMENSION_ITALIAN_BOARD; posC++)
                if (piecesBoard[posR][posC] == null)
                    piecesBoard[posR][posC] = new Empty(new Position(posR, posC));
        initializeBoardFX();
    }

    public ItalianBoard(String fen, PiecesColors color) {
        player = color;
        if (player == PiecesColors.WHITE)
            piecesBoard = fenToMultidimensionalArray(fen);
        else {
            StringBuilder reverseFen = new StringBuilder();

            for (int i = fen.length() - 1; i >= 0; i--) {
                reverseFen.append(fen.charAt(i));
            }
            piecesBoard = fenToMultidimensionalArray(reverseFen.toString());
        }


        initializeBoardFX();
    }

    /**
     * Board initializer using JavaFX.
     */
    public void initializeBoardFX() {
        stackPaneBoard = new StackPane[DIMENSION_ITALIAN_BOARD][DIMENSION_ITALIAN_BOARD];
        for (int x = 0; x < DIMENSION_ITALIAN_BOARD; x++) {
            for (int y = 0; y < DIMENSION_ITALIAN_BOARD; y++) {
                stackPaneBoard[x][y] = new StackPane();
                stackPaneBoard[x][y].setPrefWidth(60);
                stackPaneBoard[x][y].setPrefHeight(60);

            }
        }
        resetBoardFXColors();
    }

    /**
     * Reset colors of the pieces on the board
     */
    public void resetBoardFXColors() {
        for (int x = 0; x < DIMENSION_ITALIAN_BOARD; x++) {
            for (int y = 0; y < DIMENSION_ITALIAN_BOARD; y++) {
                stackPaneBoard[x][y].setDisable(true);
                //questo e' sempre vero controlla warning
                if ((x % 2 == 0 && y % 2 == 1) || (x % 2 == 1 && y % 2 == 0)) {
                    stackPaneBoard[x][y].setId("lightSquare");
                } else {
                    stackPaneBoard[x][y].setId("darkSquare");
                }
            }
        }
    }

    /**
     * Creation of pieces on the board graphically
     *
     * @param grid   Defines the positions on whom the pieces can move
     * @param player Defines the color of the pieces of a specific player
     */
    public void placeboard(GridPane grid, PiecesColors player) {
        gridPane = grid;
        Circle circle;
        grid.getChildren().clear();
        for (int i = 0; i < DIMENSION_ITALIAN_BOARD; i++) {
            for (int j = 0; j < DIMENSION_ITALIAN_BOARD; j++) {
                grid.add(stackPaneBoard[i][j], i, j);

                if (piecesBoard[j][i].getPlayer() != PiecesColors.EMPTY) {
                    if (piecesBoard[j][i].getPlayer() == PiecesColors.WHITE) {
                        circle = piecesBoard[j][i];
                        if(piecesBoard[j][i].getType() == PiecesType.MAN)
                            circle.setFill(Color.WHITE);
                        else {
                            Image image = new Image("/it/ing/pajc/graphics/Images/WoodenStyle/Kings/whiteKing.JPG",false);
                            circle.setFill(new ImagePattern(image));
                        }
                        circle.setRadius(30);
                        circle.setStyle("");
                        grid.add(circle, i, j);
                        if (this.player != piecesBoard[j][i].getPlayer())
                            circle.setDisable(true);
                    } else {
                        circle = piecesBoard[j][i];
                        if(piecesBoard[j][i].getType() == PiecesType.MAN)
                            circle.setFill(Color.BLACK);
                        else {
                            Image image = new Image("/it/ing/pajc/graphics/Images/WoodenStyle/Kings/blackKing.JPG",false);
                            circle.setFill(new ImagePattern(image));
                        }
                        circle.setRadius(30);
                        grid.add(circle, i, j);
                        if (this.player != piecesBoard[j][i].getPlayer())
                            circle.setDisable(true);
                    }
                    int finalJ = j;
                    int finalI = i;

                    //Piece click handling event
                    Circle finalCircle = circle;
                    circle.setOnMousePressed(event -> {
                        resetBoardFXColors();
                        /* animations
                        TranslateTransition transition = new TranslateTransition();
                        transition.setToX(50);
                        transition.setToY(-50);
                        transition.setToZ(100);
                        transition.setNode(finalCircle);
                        transition.play();
                         */
                        int x = finalJ;
                        int y = finalI;
                        //Getting the possible moves of specific man or king
                        GenericTree genericTree;
                        if(piecesBoard[x][y].getType()==PiecesType.MAN)
                            genericTree = ((Man) (piecesBoard[x][y])).possibleMoves(ItalianBoard.this);
                        else
                            genericTree = ((King) (piecesBoard[x][y])).possibleMoves(ItalianBoard.this);

                        List<GenericTreeNode> list = genericTree.build(GenericTreeTraversalOrderEnum.PRE_ORDER);
                        //Printing all the possible moves
                        for (int p = 1; p < list.size(); p++) {
                            Position position = (Position) list.get(p).getData();
                            //System.out.println(position.getPosR() + " " + position.getPosC());
                            stackPaneBoard[position.getPosC()][position.getPosR()].setDisable(false);
                            //System.out.println(stackPaneBoard[position.getPosC()][position.getPosR()].isDisable());
                            stackPaneBoard[position.getPosC()][position.getPosR()].setId("movementHighlight");


                            //Generating event for the movement positions with deletion of the eaten pieces
                            stackPaneBoard[position.getPosC()][position.getPosR()].setOnMousePressed(new EventHandler<MouseEvent>() {
                                @Override
                                public void handle(MouseEvent event) {
                                    GenericTree genericTreePossibleCaptures;
                                    if(piecesBoard[x][y].getType()==PiecesType.MAN)
                                        genericTreePossibleCaptures = ((Man) (piecesBoard[x][y])).possibleCaptures(ItalianBoard.this);
                                    else
                                        genericTreePossibleCaptures = ((King) (piecesBoard[x][y])).possibleCaptures(ItalianBoard.this);

                                    //GenericTree genericTreePossibleCaptures = ((Man) (piecesBoard[x][y])).possibleCaptures(ItalianBoard.this);
                                    List<GenericTreeNode> listPossibleCaptures = genericTreePossibleCaptures.build(GenericTreeTraversalOrderEnum.PRE_ORDER);
                                    Move.executeMove(piecesBoard,new Position(x, y),position,listPossibleCaptures);
                                    resetBoardFXColors();
                                    placeboard(gridPane, player);
                                }
                            });
                        }

                        /*
                        genericTree = ((Man) (piecesBoard[x][y])).possibleMoves(ItalianBoard.this);
                        list = genericTree.build(GenericTreeTraversalOrderEnum.PRE_ORDER);
                        for (int p = 1; p < list.size(); p++) {
                            Position position = (Position) list.get(p).getData();
                            //System.out.println(position.getPosR() + " " + position.getPosC());
                            stackPaneBoard[position.getPosC()][position.getPosR()].setDisable(false);
                            stackPaneBoard[position.getPosC()][position.getPosR()].setId("movementHighlight");
                        }
                        //MovementList list = ((Man) (piecesBoard[x][y])).possibleMoves(ItalianBoard.this);
                        for (Position position : list.getPossibleMoves()) {
                            stackPaneBoard[position.getPosC()][position.getPosR()].setDisable(false);
                            stackPaneBoard[position.getPosC()][position.getPosR()].setId("highlighted");

                        }*/
                    });
                }

            }
        }
    }

    /**
     * Deletes the piece in the given position and replaces it with an empty object.
     *
     * @param position of the piece wanted to be deleted
     */
    public void delete(Position position){
        piecesBoard[position.getPosR()][position.getPosC()] = new Empty(position);
    }


    /**
     * Change the position of a piece on the board.
     *
     * @param init initial position
     * @param fin  final position
     */
    @Override
    public void move(Position init, Position fin) {

        if (piecesBoard[init.getPosR()][init.getPosC()].getPlayer() == PiecesColors.WHITE)
            piecesBoard[fin.getPosR()][fin.getPosC()] = new ItalianMan(fin, PiecesColors.WHITE);

        else if (piecesBoard[init.getPosR()][init.getPosC()].getPlayer() == PiecesColors.BLACK)
            piecesBoard[fin.getPosR()][fin.getPosC()] = new ItalianMan(fin, PiecesColors.BLACK);

        piecesBoard[init.getPosR()][init.getPosC()] = new Empty(init);
    }

    /**
     * Print the board, only console.
     */
    @Override
    public void printBoardConsole() {
        for (int posR = 0; posR < DIMENSION_ITALIAN_BOARD; posR++) {
            for (int posC = 0; posC < DIMENSION_ITALIAN_BOARD; posC++) {
                if (piecesBoard[posR][posC].getPlayer() == PiecesColors.EMPTY)
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
     *
     * @param board Of pieces
     */
    @Override
    public void setBoard(Pieces[][] board) {
        this.piecesBoard = board;
    }

    /**
     * Creates a string where the whole board and the pieces postions are saved
     *
     * @return The string generated or AKA FEN notion
     */
    @Override
    public String toString() {
        String fen = "";
        int i = 0;
        for (int x = 0; x < DIMENSION_ITALIAN_BOARD; x++) {
            for (int y = 0; y < DIMENSION_ITALIAN_BOARD; y++) {
                //fen+=piecesBoard[x][y].getClass().getName();
                if (piecesBoard[x][y].getClass().getName() == "it.ing.pajc.data.pieces.italian.ItalianMan" )
                    if (piecesBoard[x][y].getPlayer() == PiecesColors.BLACK)
                        fen += "m";
                    else
                        fen += "M";
                else if (piecesBoard[x][y].getClass().getName() == "it.ing.pajc.data.pieces.italian.ItalianKing" )
                    if (piecesBoard[x][y].getPlayer() == PiecesColors.BLACK)
                        fen += "k";
                    else
                        fen += "K";

                else if (piecesBoard[x][y].getClass().getName() == "it.ing.pajc.data.pieces.Empty")
                    fen += "e";
            }
            fen += "/";
        }


        if (player == PiecesColors.BLACK){
            StringBuilder reverseFen = new StringBuilder();

            for (int j = fen.length() - 1; j >= 0; j--) {
                reverseFen.append(fen.charAt(j));
            }
            return reverseFen.toString();
        }


        return fen;
    }

    /**
     * Creation of board from specific positions of each piece
     *
     * @param fen Defines the postions of the pieces
     * @return Generated board
     */
    public static Pieces[][] fenToMultidimensionalArray(String fen) {
        int i = 0;
        Pieces pieces[][] = new Pieces[DIMENSION_ITALIAN_BOARD][DIMENSION_ITALIAN_BOARD];
        int boardPosition = 0;
        do {

            if (fen.charAt(i) != '/') {
                switch (fen.charAt(i)) {

                    case 'm': {
                        pieces[boardPosition / 8][boardPosition % 8] = new ItalianMan(new Position(boardPosition / 8, boardPosition % 8), PiecesColors.BLACK);
                    }
                    break;
                    case 'k': {
                        pieces[boardPosition / 8][boardPosition % 8] = new ItalianKing(new Position(boardPosition / 8, boardPosition % 8), PiecesColors.BLACK);
                    }
                    break;
                    //------------------------------------------------------------------
                    case 'M': {
                        pieces[boardPosition / 8][boardPosition % 8] = new ItalianMan(new Position(boardPosition / 8, boardPosition % 8), PiecesColors.WHITE);
                    }
                    break;
                    case 'K': {
                        pieces[boardPosition / 8][boardPosition % 8] = new ItalianKing(new Position(boardPosition / 8, boardPosition % 8), PiecesColors.WHITE);
                    }
                    break;
                    //------------------------------------------------------------------
                    case 'e': {
                        pieces[boardPosition / 8][boardPosition % 8] = new Empty(new Position(boardPosition / 8, boardPosition % 8));
                    }
                }

                boardPosition++;
            }

            i++;
        } while (i < fen.length());

        return pieces;
    }

    public ItalianMan getMan(int posR, int posC) {
        return (ItalianMan) piecesBoard[posR][posC];
    }

    public ItalianKing getKing(int posR, int posC) {
        return (ItalianKing) piecesBoard[posR][posC];
    }
}

