package it.ing.pajc.controller;

import it.ing.pajc.Main;
import it.ing.pajc.data.board.Board;
import it.ing.pajc.data.board.ItalianBoard;
import it.ing.pajc.data.movements.Position;
import it.ing.pajc.data.pieces.*;
import it.ing.pajc.manager.Player;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;


/**
 * Controller of the board displaying page.
 */
public class Controller {
    public static BooleanProperty timeToChangePlayer;

    public static void placeBoard(ItalianBoard board, Scene scene, Player player) {
        board.printBoardConsole();
        GridPane gridPane = (GridPane) scene.lookup("#grid");
        gridPane.getChildren().clear();
        StackPane[][] stackPaneBoard = initializeBoardFX();
        boolean canSomebodyCapture = CheckPossibleMovements.canSomebodyCapture(board, player);
        for (int i = 0; i < Board.DIMENSION_ITALIAN_BOARD; i++) {
            for (int j = 0; j < Board.DIMENSION_ITALIAN_BOARD; j++) {
                //The method .add is the opposite than normal matrix
                gridPane.add(stackPaneBoard[i][j], j, i);
                if (board.getBoard()[i][j].getPlace() != PlaceType.EMPTY) {
                    Circle circle = addPieceToGridPane(board, i, j, gridPane, player, canSomebodyCapture);
                    createClickEventPiece(circle, board, stackPaneBoard, i, j, scene, player);
                }
            }
        }
        System.err.println("la verità sarà scoperta: "+canSomebodyCapture);
    }


    /**
     * Board initializer using JavaFX.
     */
    public static StackPane[][] initializeBoardFX() {
        StackPane[][] stackPaneBoard;
        stackPaneBoard = new StackPane[Board.DIMENSION_ITALIAN_BOARD][Board.DIMENSION_ITALIAN_BOARD];
        for (int x = 0; x < Board.DIMENSION_ITALIAN_BOARD; x++) {
            for (int y = 0; y < Board.DIMENSION_ITALIAN_BOARD; y++) {
                stackPaneBoard[x][y] = new StackPane();
                stackPaneBoard[x][y].setPrefWidth(59);
                stackPaneBoard[x][y].setPrefHeight(59);
            }
        }
        resetBoardFXColors(stackPaneBoard);
        return stackPaneBoard;
    }

    /**
     * Reset colors of the pieces on the board
     */
    private static void resetBoardFXColors(StackPane[][] stackPaneBoard) {
        for (int x = 0; x < Board.DIMENSION_ITALIAN_BOARD; x++)
            for (int y = 0; y < Board.DIMENSION_ITALIAN_BOARD; y++) {
                stackPaneBoard[x][y].setDisable(true);
                if ((x % 2 == 0 && y % 2 == 1) || (x % 2 == 1 && y % 2 == 0))
                    stackPaneBoard[x][y].setId("lightSquare");
                else
                    stackPaneBoard[x][y].setId("darkSquare");
            }
    }


    private static Circle addPieceToGridPane(ItalianBoard board, int i, int j, GridPane gridPane, Player player, boolean canSomebodyCapture) {
        Circle circle = new Circle();
        styleCircle(circle);

        if (board.getBoard()[i][j].getPiece() == PieceType.MAN)
            circle.setFill(board.getBoard()[i][j].getPlace() == PlaceType.WHITE ? Color.WHITE : Color.BLACK);
        else
            transformToKing(board.getBoard()[i][j].getPlace(), circle);

        if (!PlaceType.confrontPlayer(board.getBoard()[i][j].getPlace(), player))
            circle.setDisable(true);

        //If somebody can capture and this piece can't then it is disabled
        if (canSomebodyCapture && !CheckPossibleMovements.canCapture(board, i, j)) circle.setDisable(true);

        //The method .add is the opposite than normal matrix
        gridPane.add(circle, j, i);
        return circle;
    }


    private static void styleCircle(Circle circle) {
        circle.setRadius(30);
        circle.setStyle("");
    }

    private static void transformToKing(PlaceType placeType, Circle circle) {
        Image image;
        if (placeType == PlaceType.WHITE) {
            image = new Image("/it/ing/pajc/GUI/Images/WoodenStyle/Kings/whiteKing.JPG", false);
        } else {
            image = new Image("/it/ing/pajc/GUI/Images/WoodenStyle/Kings/blackKing.JPG", false);
        }
        circle.setFill(new ImagePattern(image));
    }


    private static void keepCapturing(ItalianBoard board, Scene scene, int posR, int posC, Player player) {
        board.printBoardConsole();
        GridPane gridPane = (GridPane) scene.lookup("#grid");
        gridPane.getChildren().clear();
        StackPane[][] stackPaneBoard = initializeBoardFX();
        for (int i = 0; i < Board.DIMENSION_ITALIAN_BOARD; i++) {
            for (int j = 0; j < Board.DIMENSION_ITALIAN_BOARD; j++) {
                //The method .add is the opposite than normal matrix
                gridPane.add(stackPaneBoard[i][j], j, i);
                if (board.getBoard()[i][j].getPlace() != PlaceType.EMPTY) {
                    Circle circle = addDisabledPieceToGridPane(board, i, j, gridPane);
                    if (i == posR && j == posC) {
                        circle.setDisable(false);
                        createClickEventPiece(circle, board, stackPaneBoard, i, j, scene, player);
                    }
                }
            }
        }
    }

    private static Circle addDisabledPieceToGridPane(ItalianBoard board, int i, int j, GridPane gridPane) {
        Circle circle = new Circle();
        styleCircle(circle);

        if (board.getBoard()[i][j].getPiece() == PieceType.MAN)
            circle.setFill(board.getBoard()[i][j].getPlace() == PlaceType.WHITE ? Color.WHITE : Color.BLACK);
        else
            transformToKing(board.getBoard()[i][j].getPlace(), circle);

        circle.setDisable(true);

        //The method .add is the opposite than normal matrix
        gridPane.add(circle, j, i);
        return circle;
    }


    private static void createClickEventPiece(Circle circle, ItalianBoard board, StackPane[][] stackPanes, int i, int j, Scene scene, Player player) {
        circle.setOnMousePressed(event -> {
            resetBoardFXColors(stackPanes);
            ArrayList<Position> moves = CheckPossibleMovements.allPossibleMoves(board, i, j);
            createClickEventForMoveAndDeletion(board, stackPanes, i, j, moves, scene, player);
        });
    }


    public static void createClickEventForMoveAndDeletion(ItalianBoard board, StackPane[][] stackPanes, int i, int j, ArrayList<Position> moves, Scene scene, Player player) {
        if (CheckPossibleMovements.canCapture(board, i, j))
            for (Position moveAndCapturedPosition : moves) {
                stackPanes[i][j].setId("movementHighlight");
                stackPanes[moveAndCapturedPosition.getPosR()][moveAndCapturedPosition.getPosC()].setId("captureHighlight");
                stackPanes[moveAndCapturedPosition.getPosR()][moveAndCapturedPosition.getPosC()].setDisable(false);

                stackPanes[moveAndCapturedPosition.getPosR()][moveAndCapturedPosition.getPosC()].setOnMousePressed(event -> {
                    Move.executeMove(new Position(i, j), moveAndCapturedPosition, board);
                    //resetBoardFXColors(stackPanes);
                    //TODO GUARDA CHE NON è FIRST DIAMINE DAMN IT
                    if (CheckPossibleMovements.canCapture(board, moveAndCapturedPosition.getPosR(), moveAndCapturedPosition.getPosC()))
                        keepCapturing(board, scene, moveAndCapturedPosition.getPosR(), moveAndCapturedPosition.getPosC(), player);
                    else {
                        //placeBoard(board, scene, player);

                        timeToChangePlayer.setValue(true);
                    }
                });
            }
        else {
            for (Position position : moves) {
                stackPanes[position.getPosR()][position.getPosC()].setId("movementHighlight");
                stackPanes[position.getPosR()][position.getPosC()].setDisable(false);
                stackPanes[position.getPosR()][position.getPosC()].setOnMousePressed(event -> {
                    Move.executeMove(new Position(i, j), position, board);
                    //placeBoard(board, scene, player);
                    timeToChangePlayer.setValue(true);
                    //TODO GUARDA CHE NON è FIRST DIAMINE DAMN IT
                });
            }

        }

    }


    /**
     * Changes the current scene.
     *
     * @param root  Graphics file.
     * @param scene Graphics file scene.
     */
    private static void changeScene(Parent root, Scene scene) {
        AtomicReference<Double> x = new AtomicReference<>((double) 0);
        AtomicReference<Double> y = new AtomicReference<>((double) 0);
        Main.getPrimaryStage().setTitle("Home");
        Main.getPrimaryStage().setScene(scene);
        root.setOnMousePressed(event -> {
            x.set(event.getSceneX());
            y.set(event.getSceneY());
        });
        root.setOnMouseDragged(event -> {
            Main.getPrimaryStage().setX(event.getScreenX() - x.get());
            Main.getPrimaryStage().setY(event.getScreenY() - y.get());
        });
    }


    /**
     * Goes back to home page.
     */
    //Se non va la colpa è di ANDREA era getClass().getResource("../GUI/WhiteWins.fxml") cmq si colpa sua.
    public void back() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("../GUI/Home.fxml"));
            Scene scene = new Scene(root);
            changeScene(root, scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void finishInATie(){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("../GUI/Tie.fxml"));
            Scene scene = new Scene(root);
            changeScene(root, scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resign(){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("../GUI/Tie.fxml"));
            Scene scene = new Scene(root);
            changeScene(root, scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    //TODO POTREBBERO ACCADERE COSE MAGICHE ATTENTO ALLA X E ALLA Y ;)

    /**
     * Closes the game.
     */
    public void close() {
        Platform.exit();
    }
}