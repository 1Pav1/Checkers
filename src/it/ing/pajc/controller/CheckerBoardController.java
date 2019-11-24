package it.ing.pajc.controller;

import it.ing.pajc.Main;
import it.ing.pajc.data.board.ItalianBoard;
import it.ing.pajc.data.movements.*;
import it.ing.pajc.data.pieces.*;
import it.ing.pajc.data.pieces.italian.ItalianKing;
import it.ing.pajc.data.pieces.italian.ItalianMan;
import it.ing.pajc.singleplayer.SinglePlayerManager;
import javafx.animation.TranslateTransition;
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
import javafx.util.Duration;

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
        System.out.println("Turn of : "+singlePlayerManager.getPlayer());
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

    /**
     * Creates the click events for the piece and movements.
     * @param piece in question.
     */

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
            createClickEventForMoveAndDeletion(parent, circle);
        });
    }

    /**
     * Creates the events related to the movement of pieces.
     * @param parent Initial node position.
     */
    public static void createClickEventForMoveAndDeletion(GenericTreeNode parent,Circle circle) {

        if(Move.canCapture(singlePlayerManager.getItalianBoard(),((Position)parent.getData()).getPosR(),((Position) parent.getData()).getPosC()))
            for (int i = 0; i < parent.getNumberOfChildren(); i++) {

                Position position = (Position) parent.getChildAt(i).getData();
                Position moveAndCapturedPosition = (Position) parent.getChildAt(i).getData();
                stackPaneBoard[position.getPosC()][position.getPosR()].setId("movementHighlight");
                stackPaneBoard[moveAndCapturedPosition.getPosition().getPosC()][moveAndCapturedPosition.getPosition().getPosR()].setId("captureHighlight");
                stackPaneBoard[position.getPosC()][position.getPosR()].setDisable(false);

                stackPaneBoard[position.getPosC()][position.getPosR()].setOnMousePressed(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        TranslateTransition tt = new TranslateTransition(Duration.millis(1000),circle);
                        int x = (moveAndCapturedPosition.getPosC())-((Position) parent.getData()).getPosC();
                        int y = (moveAndCapturedPosition.getPosR())-((Position) parent.getData()).getPosR();
                        tt.setByX(x*59);
                        tt.setByY(y*59);
                        tt.play();
                        /*Move.executeMove((Position) parent.getData(),moveAndCapturedPosition,singlePlayerManager.getItalianBoard());
                        if (!Move.canCapture(singlePlayerManager.getItalianBoard(), moveAndCapturedPosition.getPosR(), moveAndCapturedPosition.getPosC()))
                            singlePlayerManager.changePlayer();
                        else
                            disableAllBoard();
                        singlePlayerManager.getItalianBoard().getBoard()[moveAndCapturedPosition.getPosR()][moveAndCapturedPosition.getPosC()].setDisable(false);
                        resetBoardFXColors();
                        placeBoard();*/

                    }
                });
            }
        else{
            for (int i = 0; i < parent.getNumberOfChildren(); i++) {
                Position position = (Position) parent.getChildAt(i).getData();
                stackPaneBoard[position.getPosC()][position.getPosR()].setId("movementHighlight");
                stackPaneBoard[position.getPosC()][position.getPosR()].setDisable(false);
                stackPaneBoard[position.getPosC()][position.getPosR()].setOnMousePressed(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        TranslateTransition tt = new TranslateTransition(Duration.millis(1000),circle);
                        int x = (position.getPosC())-((Position) parent.getData()).getPosC();
                        int y = (position.getPosR())-((Position) parent.getData()).getPosR();
                        tt.setByX(x*35);
                        tt.setByY(y*35);
                        tt.play();
                        /*
                        Move.executeMove((Position) parent.getData(),position,singlePlayerManager.getItalianBoard());singlePlayerManager.changePlayer();
                        resetBoardFXColors();
                        placeBoard();*/
                    }
                });
            }

        }
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

    private static void disableAllBoard(){
        for (int x = 0; x < DIMENSION_ITALIAN_BOARD; x++)
            for (int y = 0; y < DIMENSION_ITALIAN_BOARD; y++) {
                singlePlayerManager.getItalianBoard().getBoard()[x][y].setDisable(true);
            }
        placeBoard();
    }
}