package it.ing.pajc.manager;

import it.ing.pajc.movements.CheckPossibleMovements;
import it.ing.pajc.controller.Controller;
import it.ing.pajc.movements.Move;
import it.ing.pajc.data.board.ItalianBoard;
import it.ing.pajc.movements.*;
import it.ing.pajc.data.pieces.PieceType;
import it.ing.pajc.data.pieces.PlaceType;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.util.ArrayList;

import static it.ing.pajc.controller.FXUtility.changeScene;
import static it.ing.pajc.data.board.Board.DIMENSION_ITALIAN_BOARD;

public class LocalGameVsEngine {
    private final static int INFINITY = Integer.MAX_VALUE;
    private static int MAX_DEPTH = 7;
    private final Scene scene;
    private final Player chosenPlayer;
    private ItalianBoard board;
    private Player currentPlayer;
    private ItalianBoard bestBoard;

    /**
     * Constructor of local game vs engine
     *
     * @param chosenPlayer the chosen player
     * @param scene        taken in consideration
     */
    public LocalGameVsEngine(Player chosenPlayer, Scene scene) {
        StringBuilder fen = new StringBuilder("memememe/emememem/memememe/eeeeeeee/eeeeeeee/eMeMeMeM/MeMeMeMe/eMeMeMeM");
        board = new ItalianBoard(fen);
        this.currentPlayer = Player.WHITE_PLAYER;
        this.scene = scene;
        this.chosenPlayer = chosenPlayer;
        if (chosenPlayer != Player.WHITE_PLAYER)
            board.rotate();
        Controller.placeBoard(board, scene, chosenPlayer);
        gameVsAI();

        if (chosenPlayer != Player.WHITE_PLAYER) {
            board = execute(board);
            Controller.timeToChangePlayer.setValue(true);
            Controller.placeBoard(board, scene, chosenPlayer);
        }
    }

    /**
     * Start the game vs AI
     */
    private void gameVsAI() {
        Controller.timeToChangePlayer = new SimpleBooleanProperty(false);
        Controller.timeToChangePlayer.addListener((observable, oldValue, newValue) -> {
            if (!oldValue && newValue) {
                changePlayer();
            }
        });
    }

    /**
     * Change the player
     */
    private void changePlayer() {
        Controller.timeToChangePlayer.setValue(false);
        currentPlayer = currentPlayer == Player.WHITE_PLAYER ? Player.BLACK_PLAYER : Player.WHITE_PLAYER;
        checkLost();
        changePlayerFX();
    }

    /**
     * Change the player
     */
    private void changePlayerFX() {
        if (currentPlayer != chosenPlayer) {
            board = execute(board);
            Controller.timeToChangePlayer.setValue(true);
        } else {
            Controller.placeBoard(board, scene, currentPlayer);
        }
    }

    /**
     * Execute the move
     *
     * @param board chosen
     * @return the board
     */
    private ItalianBoard execute(ItalianBoard board) {
        ItalianBoard aiBoard = new ItalianBoard(board.getFen());
        aiBoard.rotate();
        int eval = miniMax(aiBoard, MAX_DEPTH, currentPlayer);
        System.out.println("EVAL: " + eval);
        aiBoard = bestBoard;
        return aiBoard;
    }

    /**
     * MiniMax function
     *
     * @param board    chosen
     * @param maxDepth calculation
     * @param player   chosen
     * @return the calculated value
     */
    private int miniMax(ItalianBoard board, int maxDepth, Player player) {
        int depth = 0;

        int score;
        int bestMax = -INFINITY;
        int punteggio = bestMax;
        int bestMin = INFINITY;

        ItalianBoard aiBoard = new ItalianBoard(board.getFen());
        ArrayList<ItalianBoard> tempBoards = calculateMoves(aiBoard, player);

        for (ItalianBoard calculatedBoards : tempBoards) {
            calculatedBoards.rotate();
            score = minimize(player, calculatedBoards, depth + 1, maxDepth, opponent(player), bestMin, bestMax);
            System.out.println("test: " + score);
            if (score >= punteggio) {
                bestBoard = calculatedBoards;
                punteggio = score;
            }
            if (punteggio >= bestMin) {
                return punteggio;
            }
            bestMax = Math.max(bestMax, punteggio);
        }
        return punteggio;
    }

    /**
     * Maximize of minimax
     *
     * @param firstTurn the first player
     * @param board     chosen
     * @param depth     reached
     * @param maxDepth  to reach
     * @param player    turn
     * @param bestMin   value of the function until now
     * @param bestMax   value of the function until now
     * @return the maximized value
     */
    private int maximize(Player firstTurn, ItalianBoard board, int depth, int maxDepth, Player player, int bestMin, int bestMax) {
        int score;

        if (depth >= maxDepth)
            if (firstTurn != player)
                return -evaluateBoard(board);
            else
                return evaluateBoard(board);
        else { //branch
            score = -INFINITY;

            //Move calculation
            ItalianBoard aiBoard = new ItalianBoard(board.getFen());
            ArrayList<ItalianBoard> tempBoards = calculateMoves(aiBoard, player);


            for (ItalianBoard calculatedBoards : tempBoards) {
                calculatedBoards.rotate();
                score = Math.max(score, minimize(firstTurn, calculatedBoards, depth + 1, maxDepth, opponent(player), bestMin, bestMax));

                if (score <= bestMin) {
                    System.out.println("testMAXIMIZE: " + score);
                    return score;
                }
                bestMax = Math.max(bestMax, score);
            }
            return score;
        }
    } //end maximize

    /**
     * Minimize of minimax
     *
     * @param firstTurn the first player
     * @param board     chosen
     * @param depth     reached
     * @param maxDepth  to reach
     * @param player    turn
     * @param bestMin   value of the function until now
     * @param bestMax   value of the function until now
     * @return the minimized value
     */
    private int minimize(Player firstTurn, ItalianBoard board, int depth, int maxDepth, Player player, int bestMin, int bestMax) {
        int score;

        if (depth >= maxDepth)
            if (firstTurn != player)
                return -evaluateBoard(board);
            else
                return evaluateBoard(board);
        else { //branch
            score = INFINITY;

            //Moves calculation
            ItalianBoard aiBoard = new ItalianBoard(board.getFen());
            ArrayList<ItalianBoard> tempBoards = calculateMoves(aiBoard, player);


            for (ItalianBoard calculatedBoards : tempBoards) {
                calculatedBoards.rotate();
                score = Math.min(score, maximize(firstTurn, calculatedBoards, depth + 1, maxDepth, opponent(player), bestMin, bestMax));

                if (score <= bestMax) {
                    System.out.println("testMINIMIZE: " + score);
                    return score;
                }

                bestMin = Math.min(bestMin, score);
            }

            return score;
        }
    } //end minimize

    /**
     * Check if the game is lost or not
     */
    private void checkLost() {
        if (currentPlayer != chosenPlayer)
            board.rotate();

        boolean someoneCanDoSomething = false;

        if (Move.canSomebodyDoSomething(board, currentPlayer))
            someoneCanDoSomething = true;

        if (currentPlayer != chosenPlayer)
            board.rotate();

        if (!someoneCanDoSomething) {
            Parent root = null;
            try {
                if (currentPlayer != Player.WHITE_PLAYER) {
                    root = FXMLLoader.load(getClass().getResource("../GUI/WhiteWins.fxml"));
                } else {
                    root = FXMLLoader.load(getClass().getResource("../GUI/BlackWins.fxml"));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            assert root != null;
            Scene scene = new Scene(root);
            changeScene(root, scene);
        }
    }

    /**
     * Evaluate the board from the currentPlayer side
     *
     * @param board chosen
     * @return the evaluation of the board
     */
    private int evaluateBoard(ItalianBoard board) {
        int score = 0;
        int manNumberOnTheBoard = 0;
        boolean kingFound = false;
        boolean manFound = false;
        final int manLimit = 18;
        final int manWeight = 100;
        final int kingWeight = 200;
        final int weightToHaveMove = 20;
        final int RandomWeight = 10;

        //check the phase of the game
        for (int i = 0; i < 8 && !kingFound; i++)
            for (int j = 0; j < 8 && !kingFound; j++) {
                if (board.getBoard()[i][j].getPlace() != PlaceType.EMPTY)
                    manNumberOnTheBoard++;
                if (board.getBoard()[i][j].getPiece() == PieceType.KING)
                    kingFound = true;
            }
        //from 0 to 3 there are white for the computer, for me the != currentPlayer
        if (!kingFound || manNumberOnTheBoard > manLimit) { //Starting phase
            for (int i = 0; i < 8; i++)
                for (int j = 0; j < 8; j++) {
                    if (PlaceType.confrontPlayer(board.getBoard()[i][j].getPlace(), currentPlayer)) {
                        if ((7 - i) <= 5) //currentPlayer side
                            score += manWeight * (8 - j) * (8 - j) * (7 - i) * (7 - i);
                        else
                            score += manWeight * j * j * (7 - i) * (7 - i);
                    }
                    if (!PlaceType.confrontPlayer(board.getBoard()[i][j].getPlace(), currentPlayer)) {
                        if (i <= 3) //enemy side
                            score -= manWeight * (8 - j) * (8 - j) * i * i;
                        else
                            score -= manWeight * j * j * i * i;
                    }
                }
        } else {//final phase
            int r = 0, c = 0;
            int numberEvenCouple = 0;

            for (int i = 0; i < 8; i++)
                for (int j = 0; j < 8; j++) {
                    if (board.getBoard()[i][j].getPlace() != PlaceType.EMPTY) {
                        if (!manFound) {
                            r = i;
                            c = j;
                            manFound = true;
                        } else {
                            manFound = false;
                            if (Math.max(Math.abs(r - i), Math.abs(c - j)) % 2 == 0) { //the distance of the pair is even
                                numberEvenCouple++;
                            }
                        }
                        if (PlaceType.confrontPlayer(board.getBoard()[i][j].getPlace(), currentPlayer) && board.getBoard()[i][j].getPiece() == PieceType.MAN) {
                            score += manWeight * (7 - i) * (7 - i);
                        } else if (!PlaceType.confrontPlayer(board.getBoard()[i][j].getPlace(), currentPlayer) && board.getBoard()[i][j].getPiece() == PieceType.MAN) {
                            score -= manWeight * i * i;
                        } else if (PlaceType.confrontPlayer(board.getBoard()[i][j].getPlace(), currentPlayer) && board.getBoard()[i][j].getPiece() == PieceType.KING) {
                            score += kingWeight;
                        } else if (!PlaceType.confrontPlayer(board.getBoard()[i][j].getPlace(), currentPlayer) && board.getBoard()[i][j].getPiece() == PieceType.KING) {
                            score -= kingWeight;
                        }
                    }
                }
            if ((numberEvenCouple % 2) == 1) //the one who move is privileged
                if (chosenPlayer != currentPlayer)  //player DEVE essere WHITE o BLACK
                    score += weightToHaveMove;
                else
                    score -= weightToHaveMove;

        }
        score += (int) (Math.random() * RandomWeight);
        return score;
    }

    /**
     * Calculation for the best capture
     *
     * @param aiBoard a copy of the board
     * @param i       row
     * @param j       column
     * @return the final board
     */
    private ItalianBoard calculateBestCapture(ItalianBoard aiBoard, int i, int j) {
        int point = -INFINITY;
        Position init = new Position(i, j);
        ArrayList<Position> finalMovements = CheckPossibleMovements.allPossibleMoves(aiBoard, i, j);

        ItalianBoard tempBoard = new ItalianBoard(aiBoard.getFen());
        ArrayList<ItalianBoard> executedTempBoards = new ArrayList<>();

        for (Position fin : finalMovements) {
            executedTempBoards.add(executeMovesOnTempBoard(tempBoard, init, fin));
        }

        for (ItalianBoard calculatedBoards : executedTempBoards) {
            int eval = evaluateBoard(calculatedBoards);
            if (eval >= point) {
                point = eval;
                tempBoard = new ItalianBoard(calculatedBoards.getFen());
            }
        }
        return tempBoard;
    }

    /**
     * Calculation for the best movement
     *
     * @param aiBoard a copy of the board
     * @param i       row
     * @param j       column
     * @return the final board
     */
    private ItalianBoard calculateBestMovement(ItalianBoard aiBoard, int i, int j) {
        int point = -INFINITY;
        Position init = new Position(i, j);
        ArrayList<Position> finalMovements = CheckPossibleMovements.allPossibleMoves(aiBoard, i, j);

        ItalianBoard tempBoard = new ItalianBoard(aiBoard.getFen());
        ArrayList<ItalianBoard> executedTempBoards = new ArrayList<>();

        for (Position fin : finalMovements) {
            executedTempBoards.add(executeMoveWithoutCapture(tempBoard, init, fin));
        }

        for (ItalianBoard calculatedBoards : executedTempBoards) {
            int eval = evaluateBoard(calculatedBoards);
            if (eval >= point) {
                point = eval;
                tempBoard = new ItalianBoard(calculatedBoards.getFen());
            }
        }
        return tempBoard;
    }

    /**
     * Execute the move without capture
     *
     * @param tempBoard copy of the board
     * @param init      initial position
     * @param fin       final position
     * @return the final board
     */
    private ItalianBoard executeMoveWithoutCapture(ItalianBoard tempBoard, Position init, Position fin) {
        Move.executeMove(init, fin, tempBoard);
        return tempBoard;
    }

    /**
     * Execute all the moves on the copied board
     *
     * @param tempBoard copy of the board
     * @param init      initial position
     * @param fin       final position
     * @return the final board
     */
    private ItalianBoard executeMovesOnTempBoard(ItalianBoard tempBoard, Position init, Position fin) {
        //First positions where it can move
        Move.executeMove(init, fin, tempBoard);
        //Second positions
        ArrayList<Position> finalMovements = CheckPossibleMovements.allPossibleCaptures(tempBoard, fin.getPosR(), fin.getPosC());
        //if second position aren't of capture
        if (!CheckPossibleMovements.canCapture(tempBoard, fin.getPosR(), fin.getPosC())) {
            return tempBoard;
        }
        //if it can still capture
        for (Position secondCapture : finalMovements) {
            return executeMovesOnTempBoard(tempBoard, fin, secondCapture);
        }
        return null;
    }

    /**
     * Change turn
     *
     * @param turn actual turn
     * @return the enemy
     */
    private Player opponent(Player turn) {
        return turn == Player.BLACK_PLAYER ? Player.WHITE_PLAYER : Player.BLACK_PLAYER;
    }

    /**
     * Moves calculation
     *
     * @param board  chosen
     * @param player who is playing
     * @return all the moves
     */
    private ArrayList<ItalianBoard> calculateMoves(ItalianBoard board, Player player) {
        ArrayList<ItalianBoard> bestBoards = new ArrayList<>();
        if (CheckPossibleMovements.canSomebodyCapture(board, player)) {
            for (int i = 0; i < DIMENSION_ITALIAN_BOARD; i++) {
                for (int j = 0; j < DIMENSION_ITALIAN_BOARD; j++) {
                    if (PlaceType.confrontPlayer(board.getBoard()[i][j].getPlace(), player) &&
                            CheckPossibleMovements.canCapture(board, i, j)) {
                        bestBoards.add(calculateBestCapture(board, i, j));
                    }
                }
            }
        } else //otherwise only the ones that can move
            for (int i = 0; i < DIMENSION_ITALIAN_BOARD; i++) {
                for (int j = 0; j < DIMENSION_ITALIAN_BOARD; j++) {
                    if (PlaceType.confrontPlayer(board.getBoard()[i][j].getPlace(), player) &&
                            CheckPossibleMovements.canMove(board, i, j)) {
                        bestBoards.add(calculateBestMovement(board, i, j));
                    }
                }
            }
        return bestBoards;
    }

}//end class engine