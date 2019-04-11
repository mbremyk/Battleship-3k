/**
 * DatabaseConnector.java
 *  @author Thorkildsen Torje
 *  @author Granli Hans Kristian Olsen
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

    /**
     *
     * @param gameController the game's main controller, this makes us able to get the game-class as
     *                       well as calling the game to register when a new move has been made
     */

    //I think we only need this one now
    public PullThread(GameController gameController) {
        this.db = new DatabaseConnector();
        this.game = Statics.getGame();
        this.gameController = gameController;
    }

    /**
     * Overrides the void run in the thread class, this will run in a loop,
     * first loop checks if another user has joined the game and readied up- if it tells the game and the game starts
     *
     * The second loop checks if a new move has been made, it will only run if its the opposing user's turn to make a move
     * if that's the case, it will check the database for a new move every second.
     * In addition it will check its upload every second. 
     */
    @Override
    public void run() {
        running = true;
        boolean gameStart = false;
        while (!gameStart && running) {
            gameStart = db.userJoined(game);
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

            /**
            *Does an upload check every second
             */
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