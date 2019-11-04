package it.ing.pajc.data.board;

import it.ing.pajc.controller.CheckerBoardController;
import it.ing.pajc.data.pieces.*;
import it.ing.pajc.data.pieces.italian.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

/**
 * Creates and manage ItalianBoard
 */
public class ItalianBoard implements Board {
    private PiecesColors player;
    private Pieces[][] piecesBoard;
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
        CheckerBoardController.initializeBoardFX();
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
    public PiecesColors getPlayer() {
        return player;
    }

    /**
     * Getter of pieces board.
     *
     * @return matrix of positions.
     */
    public Pieces[][] getPiecesBoard() {
        return piecesBoard;
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
        Fen fen = new Fen(this);
        return fen.getFen().toString();
    }

    /**
     * Getter of grid pane.
     *
     * @return box that contains the pieces.
     */
    public GridPane getGridPane() {
        return gridPane;
    }

    public void setPlayer(PiecesColors player) {
        this.player = player;
    }

    public void setPiecesBoard(Pieces[][] piecesBoard) {
        this.piecesBoard = piecesBoard;
    }


    public void setGridPane(GridPane gridPane) {
        this.gridPane = gridPane;
    }
}

