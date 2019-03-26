/**
 * DatabaseConnector.java
 * <p>
 *Pulling from the sql database to check if the oponent has made any new moves
 * </p>
 */

package database;

import java.lang.Thread;

import controller.GameController;
import game.Game;
import game.Statics;

public class PullThread extends Thread{
    private final DatabaseConnector db;
    private final Game game;
    private final GameController gameController;

    public PullThread(DatabaseConnector db, Game game){
        this.db = db;
        this.game = game;
        this.gameController=null;
    }
    public PullThread(Game game){
        this.db = new DatabaseConnector();
        this.game = game;
        this.gameController=null;
    }

    //I think we only need this one now
    public PullThread(GameController gameController){
        this.db = new DatabaseConnector();
        this.game = Statics.getGame();
        this.gameController = gameController;
    }

    @Override
    public void run(){
        boolean gameStart = false;
        while(!gameStart){
            gameStart = db.userJoined(game);
            if(gameStart){
                game.userJoined();
            }
            try{
                Thread.sleep(500);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        gameController.boardsReady();
        boolean gameOver = game.getGameState();
        int moveId = game.getMoveId();
        while(!gameOver){
            int lastAction = db.lastAction(game);
            System.out.println(lastAction);
            if(lastAction != moveId && lastAction != -1){
                System.out.println("MOVED");
                moveId ++;
                game.move(db.getLastCoordinates(moveId, game.getGameId()));//TODO fix this
            }
            gameOver = game.isGameOver();
            try{
                Thread.sleep(500);
            }catch (Exception e){
                e.printStackTrace();
            }

        }
        stop();//TODO remove this because it's depricated
    }
}