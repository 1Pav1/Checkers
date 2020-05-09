package it.ing.pajc.manager;

import it.ing.pajc.controller.CheckPossibleMovements;
import it.ing.pajc.controller.Controller;
import it.ing.pajc.controller.Move;
import it.ing.pajc.data.board.ItalianBoard;
import it.ing.pajc.data.movements.*;
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

    private ItalianBoard board;
    private final Scene scene;
    private final Player chosenPlayer;
    private Player currentPlayer;

    public LocalGameVsEngine(Player chosenPlayer, Scene scene) {
        StringBuilder fen = new StringBuilder("memememe/emememem/memememe/eeeeeeee/eeeeeeee/eMeMeMeM/MeMeMeMe/eMeMeMeM");
        board = new ItalianBoard(fen);
        this.currentPlayer = Player.FIRST;
        this.scene = scene;
        this.chosenPlayer = chosenPlayer;
        if(chosenPlayer!=Player.FIRST)
            board.rotate();
        Controller.placeBoard(board, scene, chosenPlayer);
        gameVsAI();

        if(chosenPlayer!=Player.FIRST) {
            board = execute(board);
            Controller.timeToChangePlayer.setValue(true);
            Controller.placeBoard(board, scene, chosenPlayer);
        }

    }

    public void gameVsAI() {
        Controller.timeToChangePlayer = new SimpleBooleanProperty(false);
        Controller.timeToChangePlayer.addListener((observable, oldValue, newValue) -> {
            if (!oldValue && newValue) {
                changePlayer();
            }
        });
    }

    private void changePlayer() {
        Controller.timeToChangePlayer.setValue(false);
        currentPlayer = currentPlayer == Player.FIRST ? Player.SECOND : Player.FIRST;
        checkLost();
        changePlayerFX();
    }

    private void changePlayerFX() {

        if(currentPlayer!=chosenPlayer) {
            board = execute(board);
            Controller.timeToChangePlayer.setValue(true);
        }
        else{
            Controller.placeBoard(board, scene, currentPlayer);
        }
    }

    private void checkLost() {
        if(currentPlayer!=chosenPlayer)
            board.rotate();

        boolean someoneCanDoSomething = false;

        if(Move.canSomebodyDoSomething(board,currentPlayer))
            someoneCanDoSomething = true;

        if(currentPlayer!=chosenPlayer)
            board.rotate();

        if (!someoneCanDoSomething) {
            Parent root = null;
            try {
                if (currentPlayer!=Player.FIRST){
                    root = FXMLLoader.load(getClass().getResource("../GUI/WhiteWins.fxml"));
                }
                else{

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


    private ItalianBoard execute(ItalianBoard board) {
        int point = 13;
        ArrayList<ItalianBoard> tempBoards = new ArrayList<>();
        ItalianBoard aiBoard = new ItalianBoard(board.getFen());
        aiBoard.rotate();
        for (int i = 0; i < DIMENSION_ITALIAN_BOARD; i++) {
            for (int j = 0; j < DIMENSION_ITALIAN_BOARD; j++) {
                if(PlaceType.confrontPlayer(aiBoard.getBoard()[i][j].getPlace(), currentPlayer) &&
                !CheckPossibleMovements.allPossibleMoves(aiBoard,i,j).isEmpty()) {
                    tempBoards.add(calculateBestOption(aiBoard, i, j, point));
                }

            }
        }

        /*
        int i=1;
        for(ItalianBoard calculatedBoards: tempBoards){
            System.out.println(i);
            calculatedBoards.printBoardConsole();
            i++;
        }
        */
        for(ItalianBoard calculatedBoards: tempBoards){
            int eval = countPiecesOnBoard(calculatedBoards);
            if(eval<=point){
                point = eval;
                aiBoard = calculatedBoards;
            }
        }
        aiBoard.rotate();
        return aiBoard;

    }


    private ItalianBoard calculateBestOption(ItalianBoard aiBoard, int i, int j, int point) {
        Position init = new Position(i, j);
        ArrayList<Position> finalMovements = CheckPossibleMovements.allPossibleMoves(aiBoard, i, j);

        ItalianBoard tempBoard = new ItalianBoard(aiBoard.getFen());
        ArrayList<ItalianBoard> executedTempBoards = new ArrayList<>();

        for (Position fin : finalMovements) {
            if(CheckPossibleMovements.canCapture(tempBoard,i,j))
                executedTempBoards.add(executeMovesOnTempBoard(tempBoard,init,fin));
            else{
                executedTempBoards.add(executeMoveWithoutCapture(tempBoard,init,fin));
            }
        }

        for(ItalianBoard calculatedBoards: executedTempBoards){
            int eval = countPiecesOnBoard(calculatedBoards);
            if(eval<=point){
                point = eval;
                tempBoard = new ItalianBoard(calculatedBoards.getFen());
            }
        }


        return tempBoard;
    }

    private ItalianBoard executeMoveWithoutCapture(ItalianBoard tempBoard, Position init, Position fin) {
        Move.executeMove(init,fin,tempBoard);

        return tempBoard;
    }

    private ItalianBoard executeMovesOnTempBoard(ItalianBoard tempBoard, Position init, Position fin) {
        //Le prime posizioni su cui si può spostare
        Move.executeMove(init, fin, tempBoard);
        //Posizioni seconde
        ArrayList<Position> finalMovements = CheckPossibleMovements.allPossibleCaptures(tempBoard, fin.getPosR(), fin.getPosC());
        //Se le posizioni seconde non sono di cattura
        if(!CheckPossibleMovements.canCapture(tempBoard, fin.getPosR(), fin.getPosC())){
            return tempBoard;
        }
        //Se può invece ancora catturare

        for (Position secondCapture : finalMovements) {
            return executeMovesOnTempBoard(tempBoard,fin,secondCapture);
        }

        return null;
    }


    public int countPiecesOnBoard(ItalianBoard tempBoard) {
        int count = 0;
        for (int i = 0; i < DIMENSION_ITALIAN_BOARD; i++) {
            for (int j = 0; j < DIMENSION_ITALIAN_BOARD; j++) {
                if (PlaceType.confrontPlayer(tempBoard.getBoard()[i][j].getPlace(), chosenPlayer))
                    count++;
            }
        }
        return count;
    }


}
