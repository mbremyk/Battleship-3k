/**
 * DatabaseConnector.java
 * <p>
 *Pulling from the sql database to check if the oponent has made any new moves
 * </p>
 */

package database;

import java.lang.Thread;

import game.ShipCoordinates;
import model.BattleshipUser;

import static database.Constants.*;
import static database.Login.saltPassword;

import java.sql.*;
import java.util.Arrays;
import java.util.logging.*;

public class PullThread extends Thread{
  private DatabaseConnector db;
  private Game game;

  public PullThread(DatabaseConnector db, Game game){
    this.db = db;
    this.game = game;
  }

  @Override
  public void run(){
    boolean gameOver = game.getGameState();
    int moveId = game.getMoveId();
    while(!gameOver){
      int lastAction = db.lastAction(game);
        if(lastAction != moveId){
          game.move();
      }
      Thread.Sleep(500);
    }
  }
}
