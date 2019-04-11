/**
 * DatabaseConnector.java
 * <p>
 * Pulling from the sql database to check if the oponent has made any new moves
 * </p>
 */

package database;

import java.lang.Thread;

import controller.GameController;
import game.Game;
import game.Statics;

public class PullThread extends Thread {
    private final DatabaseConnector db;
    private final Game game;
    private final GameController gameController;
    private boolean running = false;

    public PullThread(DatabaseConnector db, Game game) {
        this.db = db;
        this.game = game;
        this.gameController = null;
    }

    public PullThread(Game game) {
        this.db = new DatabaseConnector();
        this.game = game;
        this.gameController = null;
    }

    //I think we only need this one now
    public PullThread(GameController gameController) {
        this.db = new DatabaseConnector();
        this.game = Statics.getGame();
        this.gameController = gameController;
    }

    @Override
    public void run() {
        running = true;
        boolean gameStart = false;
        while (!gameStart && running) {
            gameStart = db.userJoined(game);
//            if (gameStart) {
//                game.userJoined();
//            }
            try {
                Thread.sleep(500);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        gameController.boardsReady();

        boolean gameOver = game.isGameOver();
        int timer = 0;
        while ((!gameOver || !game.allActionsUploaded()) && running) {
            if (!game.isMyTurn()) {
                db.lastAction(game);
            }

            //To make it upload every second instead of 0.5 seconds
            timer++;
            timer %= 2;
            if (timer == 0) {
                game.uploadCachedActions();
            }
            gameOver = game.isGameOver();
            try {
                Thread.sleep(500);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public void terminate() {
        running = false;
        this.interrupt();
    }

}