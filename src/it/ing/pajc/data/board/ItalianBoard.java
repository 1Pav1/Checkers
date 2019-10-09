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

    /**
     * Uses Fen notation to create a board based on the player color.
     *
     * @param fen   A string that describes a unique set of positions
     * @param color Rotates the board according to the player color.
     */
    public ItalianBoard(Fen fen, PiecesColors color) {
        player = color;
        if (player == PiecesColors.WHITE)
            piecesBoard = fen.fenToMultidimensionalArray();
        else {
            fen.reverseFen();
            piecesBoard = fen.fenToMultidimensionalArray();
        }
        initializeBoardFX();
    }

    /**
     * Board initializer using JavaFX.
     */
    private void initializeBoardFX() {
        stackPaneBoard = new StackPane[DIMENSION_ITALIAN_BOARD][DIMENSION_ITALIAN_BOARD];
        for (int x = 0; x < DIMENSION_ITALIAN_BOARD; x++)
            for (int y = 0; y < DIMENSION_ITALIAN_BOARD; y++) {
                stackPaneBoard[x][y] = new StackPane();
                stackPaneBoard[x][y].setPrefWidth(60);
                stackPaneBoard[x][y].setPrefHeight(60);
            }
        resetBoardFXColors();
    }

    /**
     * Reset colors of the pieces on the board
     */
    void resetBoardFXColors() {
        for (int x = 0; x < DIMENSION_ITALIAN_BOARD; x++)
            for (int y = 0; y < DIMENSION_ITALIAN_BOARD; y++) {
                stackPaneBoard[x][y].setDisable(true);
                if ((x % 2 == 0 && y % 2 == 1) || (x % 2 == 1 && y % 2 == 0))
                    stackPaneBoard[x][y].setId("lightSquare");
                else
                    stackPaneBoard[x][y].setId("darkSquare");
            }
    }
    //TODO: FALLO TU

    /**
     * Creation of pieces on the board graphically.
     *
     * @param grid   Defines the positions where the pieces can move.
     * @param player Defines the color of the pieces of a specific player.
     */
    public void placeBoard(GridPane grid, PiecesColors player) {

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
                    if (piecesBoard[posR][posC].getPlayer() == PiecesColors.BLACK) {
                        if (piecesBoard[posR][posC].getType() == PiecesType.MAN)
                            System.out.print("[m]");
                        else
                            System.out.print("[k]");
                    } else if (piecesBoard[posR][posC].getPlayer() == PiecesColors.WHITE) {
                        if (piecesBoard[posR][posC].getType() == PiecesType.MAN)
                            System.out.print("[M]");
                        else
                            System.out.print("[K]");
                    }
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
     * Creates a string where the whole board and the pieces postions are saved.
     *
     * @return the string generated or FEN notion
     */
    @Override
    public String toString() {
        Fen fen = new Fen("");
        return fen.toString();
    }

    /**
     * Man's getter throws an exception, pay attention.
     *
     * @param posR Row position.
     * @param posC Column position.
     * @return man object.
     */
    public ItalianMan getMan(int posR, int posC) {
        return (ItalianMan) piecesBoard[posR][posC];
    }

    /**
     * King's getter throws an exception, pay attention.
     *
     * @param posR Row position.
     * @param posC Column position.
     * @return king object.
     */
    public ItalianKing getKing(int posR, int posC) {
        return (ItalianKing) piecesBoard[posR][posC];
    }

    /**
     * Player's getter.
     *
     * @return the color of the player.
     */
    PiecesColors getPlayer() {
        return player;
    }

    /**
     * Getter of pieces board.
     *
     * @return matrix of positions.
     */
    Pieces[][] getPiecesBoard() {
        return piecesBoard;
    }

    /**
     * Getter of stack pane board.
     *
     * @return matrix of graphical board.
     */
    StackPane[][] getStackPaneBoard() {
        return stackPaneBoard;
    }

    /**
     * Getter of grid pane.
     *
     * @return box that contains the pieces.
     */
    GridPane getGridPane() {
        return gridPane;
    }
}