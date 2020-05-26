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
    private static int MAX_DEPTH = 20;
    private final Scene scene;
    private final Player chosenPlayer;
    private ItalianBoard board;
    private Player currentPlayer;
    private ItalianBoard bestBoardEvah;

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

    private void gameVsAI() {
        Controller.timeToChangePlayer = new SimpleBooleanProperty(false);
        Controller.timeToChangePlayer.addListener((observable, oldValue, newValue) -> {
            if (!oldValue && newValue) {
                changePlayer();
            }
        });
    }

    private void changePlayer() {
        Controller.timeToChangePlayer.setValue(false);
        currentPlayer = currentPlayer == Player.WHITE_PLAYER ? Player.BLACK_PLAYER : Player.WHITE_PLAYER;
        checkLost();
        changePlayerFX();
    }

    private void changePlayerFX() {
        if (currentPlayer != chosenPlayer) {
            board = execute(board);
            Controller.timeToChangePlayer.setValue(true);
        } else {
            Controller.placeBoard(board, scene, currentPlayer);
        }
    }

    private ItalianBoard execute(ItalianBoard board) {
        //CALCOLO MOSSE
        ItalianBoard aiBoard = new ItalianBoard(board.getFen());
        aiBoard.rotate();


        //int eval = evalSpettacolo(calculatedBoards);
        int eval = miniMax(aiBoard, MAX_DEPTH, currentPlayer);
        System.out.println("EVAL: " + eval);

        aiBoard = bestBoardEvah;

        //aiBoard.rotate();
        return aiBoard;



    }

    private int miniMax(ItalianBoard board, int maxDepth, Player player) {
        int depth = 0;

        //System.out.println("GIOCA PLAYER:"+player);
        int score;
        int bestMax = -INFINITY;
        int punteggio = bestMax;
        int bestMin = INFINITY;

        //CALCOLO MOSSE
        ItalianBoard aiBoard = new ItalianBoard(board.getFen());
        ArrayList<ItalianBoard> tempBoards = calcoloMosse(aiBoard, player);

        for (ItalianBoard calculatedBoards : tempBoards) {
            //dopo aver fatto le mosse giro e passo
            calculatedBoards.rotate();
            score = minimize(player, calculatedBoards, depth + 1, maxDepth, opponent(player), bestMin, bestMax);
            System.out.println("test: " + score);
            if (score >= punteggio) {
                bestBoardEvah=calculatedBoards;
                punteggio = score;
            }
            if (punteggio >= bestMin) {
                return punteggio;
            }
            bestMax = Math.max(bestMax, punteggio);
        }
        return punteggio;
    }


    private int maximize(Player firstTurn, ItalianBoard board, int depth, int maxDepth, Player player, int bestMin, int bestMax) {
        // System.out.println("maximize depth:"+depth);
        int score;

        if (depth >= maxDepth)
            if (firstTurn !=player)
                return -evaluateBoard(board);
            else
                return evaluateBoard(board);
        else { //branch
            score = -INFINITY;

            //CALCOLO MOSSE
            ItalianBoard aiBoard = new ItalianBoard(board.getFen());
            ArrayList<ItalianBoard> tempBoards = calcoloMosse(aiBoard, player);


            for (ItalianBoard calculatedBoards : tempBoards) {
                calculatedBoards.rotate();
                score = Math.max(score, minimize(firstTurn, calculatedBoards, depth + 1, maxDepth, opponent(player), bestMin, bestMax));
                System.out.println("testMAXIMIZE: " + score);
                //Deve essere >=
                if (score <= bestMin) {
                    return score;
                }
                bestMax = Math.max(bestMax, score);
            }
            return score;
        }
    } //end maximize

    private int minimize(Player firstTurn, ItalianBoard board, int depth, int maxDepth, Player player, int bestMin, int bestMax) {
        //  System.out.println("minimize depth:"+depth);
        int score;

        if (depth >= maxDepth)
            if (firstTurn != player)
                return -evaluateBoard(board);
            else
                return evaluateBoard(board);
        else { //branch
            score = INFINITY;

            //CALCOLO MOSSE
            ItalianBoard aiBoard = new ItalianBoard(board.getFen());
            ArrayList<ItalianBoard> tempBoards = calcoloMosse(aiBoard, player);


            for (ItalianBoard calculatedBoards : tempBoards) {
                calculatedBoards.rotate();
                score = Math.min(score, maximize(firstTurn, calculatedBoards, depth + 1, maxDepth, opponent(player), bestMin, bestMax));
                System.out.println("testMINIMIZE: " + score);

                //Deve essere <=
                if (score <= bestMax) {
                    return score;
                }

                bestMin = Math.min(bestMin, score);
            }

            return score;
        }
    } //end minimize

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

    private int evaluateBoard(ItalianBoard board) { //Valuto dal punto di vista del currentPlayer
        int score = 0;
        int numeroPedine = 0;
        boolean damaTrovata = false;
        boolean pedinaTrovata = false;
        final int LIMITE_PEDINE = 18;
        final int PESO_PEDINA = 100;
        final int PESO_DAMA = 200;
        final int PESO_AVERE_MOSSA = 20;
        final int PESO_RANDOM = 10;

        //controllo in che fase di gioco siamo
        for (int i = 0; i < 8 && !damaTrovata; i++)
            for (int j = 0; j < 8 && !damaTrovata; j++) {
                if (board.getBoard()[i][j].getPlace() != PlaceType.EMPTY)
                    numeroPedine++;
                if (board.getBoard()[i][j].getPiece() == PieceType.KING || board.getBoard()[i][j].getPiece() == PieceType.KING)
                    damaTrovata = true;
            }
        //Da 0 a 3 per lui ci sono i bianchi, per me il != currentPlayer
        if (!damaTrovata || numeroPedine > LIMITE_PEDINE) { //fase iniziale
            for (int i = 0; i < 8; i++)
                for (int j = 0; j < 8; j++) {
                    if (PlaceType.confrontPlayer(board.getBoard()[i][j].getPlace(), currentPlayer)) {
                        if ((7 - i) <= 5) //parte del currentPlayer
                            score += PESO_PEDINA * (8 - j) * (8 - j) * (7 - i) * (7 - i);
                        else
                            score += PESO_PEDINA * j * j * (7 - i) * (7 - i);
                    }
                    if (!PlaceType.confrontPlayer(board.getBoard()[i][j].getPlace(), currentPlayer)) {
                        if (i <= 3) //parte dell'avversario
                            score -= PESO_PEDINA * (8 - j) * (8 - j) * i * i;
                        else
                            score -= PESO_PEDINA * j * j * i * i;
                    }
                }
        } else {//fase finale
            int r = 0, c = 0;
            int numeroCoppiePari = 0;

            for (int i = 0; i < 8; i++)
                for (int j = 0; j < 8; j++) {
                    if (board.getBoard()[i][j].getPlace() != PlaceType.EMPTY) {
                        if (!pedinaTrovata) {
                            r = i;
                            c = j;
                            pedinaTrovata = true;
                        } else {
                            pedinaTrovata = false;
                            if (Math.max(Math.abs(r - i), Math.abs(c - j)) % 2 == 0) { //distanza coppia e' pari
                                numeroCoppiePari++;
                            }
                        }
                        if (PlaceType.confrontPlayer(board.getBoard()[i][j].getPlace(), currentPlayer) && board.getBoard()[i][j].getPiece() == PieceType.MAN) {
                            score += PESO_PEDINA * (7 - i) * (7 - i);
                        } else if (!PlaceType.confrontPlayer(board.getBoard()[i][j].getPlace(), currentPlayer) && board.getBoard()[i][j].getPiece() == PieceType.MAN) {
                            score -= PESO_PEDINA * i * i;
                        } else if (PlaceType.confrontPlayer(board.getBoard()[i][j].getPlace(), currentPlayer) && board.getBoard()[i][j].getPiece() == PieceType.KING) {
                            score += PESO_DAMA;
                        } else if (!PlaceType.confrontPlayer(board.getBoard()[i][j].getPlace(), currentPlayer) && board.getBoard()[i][j].getPiece() == PieceType.KING) {
                            score -= PESO_DAMA;
                        }
                    }
                }
            if ((numeroCoppiePari % 2) == 1) //chi muove e' privilegiato
                if (chosenPlayer != currentPlayer)  //player DEVE essere WHITE o BLACK
                    score += PESO_AVERE_MOSSA;
                else
                    score -= PESO_AVERE_MOSSA;

        }
        score += (int) (Math.random() * PESO_RANDOM);
        return score;
    }

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
            //int eval = miniMax(calculatedBoards, 5, currentPlayer);
            if (eval >= point) {
                point = eval;
                tempBoard = new ItalianBoard(calculatedBoards.getFen());
            }
        }
        return tempBoard;
    }

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
            //int eval = miniMax(calculatedBoards, 5, currentPlayer);
            if (eval >= point) {
                point = eval;
                tempBoard = new ItalianBoard(calculatedBoards.getFen());
            }
        }
        return tempBoard;
    }

    private ItalianBoard executeMoveWithoutCapture(ItalianBoard tempBoard, Position init, Position fin) {
        Move.executeMove(init, fin, tempBoard);
        return tempBoard;
    }

    private ItalianBoard executeMovesOnTempBoard(ItalianBoard tempBoard, Position init, Position fin) {
        //Le prime posizioni su cui si può spostare
        Move.executeMove(init, fin, tempBoard);
        //Posizioni seconde
        ArrayList<Position> finalMovements = CheckPossibleMovements.allPossibleCaptures(tempBoard, fin.getPosR(), fin.getPosC());
        //Se le posizioni seconde non sono di cattura
        if (!CheckPossibleMovements.canCapture(tempBoard, fin.getPosR(), fin.getPosC())) {
            return tempBoard;
        }
        //Se può invece ancora catturare
        for (Position secondCapture : finalMovements) {
            return executeMovesOnTempBoard(tempBoard, fin, secondCapture);
        }
        return null;
    }


    private Player opponent(Player turn) {
        return turn == Player.BLACK_PLAYER ? Player.WHITE_PLAYER : Player.BLACK_PLAYER;
    }

    private ArrayList<ItalianBoard> calcoloMosse(ItalianBoard board, Player player) {
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
        } else //altrimenti solo quelli che si possono spostare
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