package it.ing.pajc.controller;

import it.ing.pajc.Main;
import it.ing.pajc.data.board.ItalianBoard;
import it.ing.pajc.data.movements.*;
import it.ing.pajc.data.pieces.*;
import it.ing.pajc.data.pieces.italian.ItalianKing;
import it.ing.pajc.data.pieces.italian.ItalianMan;
import it.ing.pajc.singleplayer.SinglePlayerManager;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.util.Set;

import static it.ing.pajc.data.board.Board.DIMENSION_ITALIAN_BOARD;

/**
 * Controller of the board displaying page.
 */
public class CheckerBoardController {
    private double x, y;
    private static GridPane gridPane = null;
    private static StackPane[][] stackPaneBoard;
    private static TextArea textArea;

    public static void start() {
        placeBoard();
    }

    public static void setTextArea(TextArea textArea) {
        CheckerBoardController.textArea = textArea;
    }

    public static void setSinglePlayerManager(SinglePlayerManager singlePlayerManager) {
        CheckerBoardController.singlePlayerManager = singlePlayerManager;
    }


    private static SinglePlayerManager singlePlayerManager;

    /**
     * Closes the game.
     */
    public void close() {
        Platform.exit();
    }

    /**
     * Goes back to home page.
     */
    public void back() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("../graphics/Home.fxml"));
            Scene scene = new Scene(root);
            changeScene(root, scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Changes the current scene.
     *
     * @param root  Graphics file.
     * @param scene Graphics file scene.
     */
    private void changeScene(Parent root, Scene scene) {
        Main.getPrimaryStage().setTitle("Home");
        Main.getPrimaryStage().setScene(scene);
        root.setOnMousePressed(event -> {
            x = event.getSceneX();
            y = event.getSceneY();
        });
        root.setOnMouseDragged(event -> {
            Main.getPrimaryStage().setX(event.getScreenX() - x);
            Main.getPrimaryStage().setY(event.getScreenY() - y);
        });
    }

    public static void setGridPane(GridPane gridPane) {
        CheckerBoardController.gridPane = gridPane;
    }


    public static void placeBoard() {
        ItalianBoard board = singlePlayerManager.getItalianBoard();
        gridPane.getChildren().clear();
        for (int i = 0; i < DIMENSION_ITALIAN_BOARD; i++) {
            for (int j = 0; j < DIMENSION_ITALIAN_BOARD; j++) {
                gridPane.add(stackPaneBoard[i][j], i, j);
                if (board.getPiecesBoard()[j][i].getPlayer() != PiecesColors.EMPTY) {
                    addPieceToGridPane(board.getPiecesBoard()[j][i], i, j);
                    createClickEventPiece(board.getPiecesBoard()[j][i]);
                }
            }
        }
    }

    private static void addPieceToGridPane(Pieces piece, int i, int j) {
        Circle circle = piece;
        if (singlePlayerManager.getItalianBoard().getBoard()[i][j].getPlayer() != singlePlayerManager.getPlayer())
            stackPaneBoard[i][j].setDisable(true);
        styleCircle(circle);
        if (piece.getType() == PiecesType.MAN) {
            if (piece.getPlayer() == PiecesColors.WHITE)
                circle.setFill(Color.WHITE);
            else
                circle.setFill(Color.BLACK);
        } else {
            transformToKing(piece.getPlayer(), circle);
        }
        gridPane.add(circle, i, j);
    }


    private static void styleCircle(Circle circle) {
        circle.setRadius(29.5);
        circle.setStyle("");
    }

    private static void transformToKing(PiecesColors player, Circle circle) {
        if (player == PiecesColors.WHITE) {
            Image image = new Image("/it/ing/pajc/graphics/Images/WoodenStyle/Kings/whiteKing.JPG", false);
            circle.setFill(new ImagePattern(image));
        } else {
            Image image = new Image("/it/ing/pajc/graphics/Images/WoodenStyle/Kings/blackKing.JPG", false);
            circle.setFill(new ImagePattern(image));
        }
    }

    private static void createClickEventPiece(Pieces piece) {
        ItalianBoard board = singlePlayerManager.getItalianBoard();
        Circle circle = piece;
        circle.setOnMousePressed(event -> {

            resetBoardFXColors();
            GenericTree<Position> genericTree;
            if (piece.getType() == PiecesType.MAN)
                genericTree = ((ItalianMan) (piece)).possibleMoves(board);
            else
                genericTree = ((ItalianKing) (piece)).possibleMoves(board);

            GenericTreeNode<Position> parent = genericTree.getRoot();
            createClickEventForMoveAndDeletion(parent);
            /*
            List<GenericTreeNode> list = genericTree.build(GenericTreeTraversalOrderEnum.PRE_ORDER);

            for (int p = 1; p < list.size(); p++) {
                Position position = (Position) list.get(p).getData();
                stackPaneBoard[position.getPosC()][position.getPosR()].setId("movementHighlight");
                //Generating event for the movement positions with deletion of the eaten pieces
                stackPaneBoard[position.getPosC()][position.getPosR()].setDisable(false);
                stackPaneBoard[position.getPosC()][position.getPosR()].setOnMousePressed(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        //board.getPiecesBoard()[piece.getPosition().getPosR()][piece.getPosition().getPosC()] = new Empty(piece.getPosition());
                       /* RotateTransition shake = new RotateTransition(Duration.millis(3000), gridPane);
                        shake.setByAngle(180);
                        shake.setCycleCount(1);
                        shake.play();*/
                        /*for (int i = 0; i < list.getNumberOfChildren(); i++) {
                            System.out.println(rootPositions.getChildAt(i).getData());
                            CheckerBoardController.createClickEventForDeletion(((MoveAndCapturedPosition) rootPositions.getChildAt(i).getData()).getToCapture(),rootPositions.getChildAt(i).getData());
                        }
                        Move.delete(positionDelete,singlePlayerManager.getItalianBoard());
                        singlePlayerManager.changePlayer();
                        placeBoard();
                    }
                });
            }*/
        });
    }

    public static void createClickEventForMoveAndDeletion(GenericTreeNode parent) {
        Set<Position> leafs = parent.getAllLeafNodes();
        for (Object object : leafs) {
            GenericTreeNode genericTreeNode = (GenericTreeNode) object;
            MoveAndCapturedPosition element = null;
            try {
                GenericTreeNode par = genericTreeNode;
                boolean con = true;
                while (con) {
                    try {
                        stackPaneBoard[(((MoveAndCapturedPosition) par.getData()).getToCapture()).getPosC()][(((MoveAndCapturedPosition) par.getData()).getToCapture()).getPosR()].setId("captureHighlight");
                        par = par.getParent();
                    } catch (Exception e) {
                        con = false;
                    }
                }

                element = (MoveAndCapturedPosition) genericTreeNode.getData();
                stackPaneBoard[element.getPosC()][element.getPosR()].setId("movementHighlight");
                stackPaneBoard[element.getPosC()][element.getPosR()].setDisable(false);
                stackPaneBoard[element.getPosC()][element.getPosR()].setOnMousePressed(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        GenericTreeNode par = genericTreeNode;
                        boolean con = true;
                        while (con) {
                            try {
                                Move.delete((((MoveAndCapturedPosition) par.getData()).getToCapture()), singlePlayerManager.getItalianBoard());
                                par = par.getParent();
                            } catch (Exception e) {
                                con = false;
                            }
                        }
                        singlePlayerManager.changePlayer();
                        resetBoardFXColors();
                        placeBoard();
                    }
                });
            } catch (Exception e) {
            }


        }


    /*
        for (int i = 0; i < leafs.size(); i++) {

            Position position = (Position) leafs..getData();
            MoveAndCapturedPosition moveAndCapturedPosition = (MoveAndCapturedPosition) parent.getChildAt(i).getData();
            stackPaneBoard[position.getPosC()][position.getPosR()].setId("movementHighlight");
            stackPaneBoard[position.getPosC()][position.getPosR()].setDisable(false);
            stackPaneBoard[position.getPosC()][position.getPosR()].setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    GenericTreeNode par = parent;
                    try {
                        while(((MoveAndCapturedPosition)parent.getData()).getToCapture()!=null){
                            Move.delete(((MoveAndCapturedPosition) parent.getData()).getToCapture(),singlePlayerManager.getItalianBoard());
                        }
                    }catch (Exception e){};


                    Move.delete(moveAndCapturedPosition.getToCapture(),singlePlayerManager.getItalianBoard());
                    placeBoard();
                }
            });
            if(parent.getChildAt(i).getNumberOfChildren()!=0)
                createClickEventForMoveAndDeletion(parent.getChildAt(i));
        }*/
    }

    public static void createClickEventForDeletion(Position positionDelete, Position toMoveTo) {
        stackPaneBoard[toMoveTo.getPosC()][toMoveTo.getPosR()].setId("captureHighlight");
        //Generating event for the movement positions with deletion of the eaten pieces
        stackPaneBoard[toMoveTo.getPosC()][toMoveTo.getPosR()].setDisable(false);

        stackPaneBoard[toMoveTo.getPosC()][toMoveTo.getPosR()].setOnMousePressed(event -> {
            Move.delete(positionDelete, singlePlayerManager.getItalianBoard());
            //singlePlayerManager.changePlayer();
            placeBoard();
        });
    }


    public static void addToTextArea(String text) {
        text = textArea.getText() + text;
        textArea.setText(text);
    }

    /**
     * Board initializer using JavaFX.
     */
    public static void initializeBoardFX() {
        CheckerBoardController.stackPaneBoard = new StackPane[DIMENSION_ITALIAN_BOARD][DIMENSION_ITALIAN_BOARD];
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
    private static void resetBoardFXColors() {
        for (int x = 0; x < DIMENSION_ITALIAN_BOARD; x++)
            for (int y = 0; y < DIMENSION_ITALIAN_BOARD; y++) {
                stackPaneBoard[x][y].setDisable(true);
                if ((x % 2 == 0 && y % 2 == 1) || (x % 2 == 1 && y % 2 == 0))
                    stackPaneBoard[x][y].setId("lightSquare");
                else
                    stackPaneBoard[x][y].setId("darkSquare");
            }
    }
}