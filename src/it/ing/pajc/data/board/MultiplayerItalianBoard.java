package it.ing.pajc.data.board;

import it.ing.pajc.data.movements.*;
import it.ing.pajc.data.pieces.*;
import it.ing.pajc.multiplayer.MultiplayerManager;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.util.List;

public class MultiplayerItalianBoard extends ItalianBoard{
    private PiecesColors player;

    private Pieces[][] piecesBoard;

    private StackPane[][] stackPaneBoard;

    private GridPane gridPane;
    private PiecesColors color;
    private MultiplayerManager multiplayerManager;

    public MultiplayerItalianBoard(String fen, PiecesColors color,MultiplayerManager multiplayerManager) {
        super(fen, color);
        this.multiplayerManager = multiplayerManager;
        player = super.getPlayer();
        piecesBoard = super.getPiecesBoard();
        stackPaneBoard = super.getStackPaneBoard();
        gridPane = super.getGridPane();
        this.color = color;
    }

    public void changeFen(String fen){
        if (player == PiecesColors.WHITE)
            piecesBoard = fenToMultidimensionalArray(fen);
        else {
            StringBuilder reverseFen = new StringBuilder();

            for (int i = fen.length() - 1; i >= 0; i--) {
                reverseFen.append(fen.charAt(i));
            }
            piecesBoard = fenToMultidimensionalArray(reverseFen.toString());
        }


        super.initializeBoardFX();
    }
    @Override
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
                        super.resetBoardFXColors();
                        int x = finalJ;
                        int y = finalI;
                        //Getting the possible moves of specific man or king
                        GenericTree genericTree;
                        if(piecesBoard[x][y].getType()==PiecesType.MAN)
                            genericTree = ((Man) (piecesBoard[x][y])).possibleMoves(MultiplayerItalianBoard.this);
                        else
                            genericTree = ((King) (piecesBoard[x][y])).possibleMoves(MultiplayerItalianBoard.this);

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
                                        genericTreePossibleCaptures = ((Man) (piecesBoard[x][y])).possibleCaptures(MultiplayerItalianBoard.this);
                                    else
                                        genericTreePossibleCaptures = ((King) (piecesBoard[x][y])).possibleCaptures(MultiplayerItalianBoard.this);

                                    //GenericTree genericTreePossibleCaptures = ((Man) (piecesBoard[x][y])).possibleCaptures(ItalianBoard.this);
                                    List<GenericTreeNode> listPossibleCaptures = genericTreePossibleCaptures.build(GenericTreeTraversalOrderEnum.PRE_ORDER);
                                    Move.executeMove(piecesBoard,new Position(x, y),position,listPossibleCaptures);
                                    resetBoardFXColors();
                                    placeboard(gridPane, player);
                                    try {
                                        sendAndWait();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
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

    private void sendAndWait() throws IOException {
        multiplayerManager.sendFen();
        for (int x = 0; x < DIMENSION_ITALIAN_BOARD; x++) {
            for (int y = 0; y < DIMENSION_ITALIAN_BOARD; y++) {
                piecesBoard[x][y].setDisable(true);
            }
        }
    }

}
