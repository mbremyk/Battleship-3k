/**
 * DatabaseConnector.java
 * <p>
 *Pulling from the sql database to check if the oponent has made any new moves
 * </p>
 */

package database;

import java.lang.Thread;

import game.Game;

public class PullThread extends Thread{
    private DatabaseConnector db;
    private Game game;

    public PullThread(DatabaseConnector db, Game game){
        this.db = db;
        this.game = game;
    }

    @Override
    public void run(){
        boolean gameStart = false;
        while(!gameStart){
            gameStart = db.gameStarted(game);
            if(gameStart){
                game.userJoined();
            }
        }
        boolean gameOver = game.getGameState();
        int moveId = game.getMoveId();
        while(!gameOver){
            int lastAction = db.lastAction(game);
            if(lastAction != moveId && lastAction != 0){
                moveId ++;
                game.move(db.getLastCoordinates(moveId, game.getGameId()));
            }
            gameOver = game.getGameState();
            try{
                Thread.sleep(500);
            }catch (Exception e){
                e.printStackTrace();
            }

        }
        stop();
    }
}