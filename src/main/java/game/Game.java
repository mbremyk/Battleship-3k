/**
 * Game.java
 *
 * @author Grande Trym
 */

package game;

import database.Constants;
import database.DatabaseConnector;
import model.BattleshipUser;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Random;

/**
 * two boards
 * what players are playing
 * graphical logic
 */
public class Game {
    private boolean hosting; //If the local player is hosting
    private BattleshipUser hostUser;
    private BattleshipUser joinUser;
    private Board board1;
    private Board board2;
    private boolean boardsReady = false;
    //	private DatabaseConnector databaseConnector;
    private String gameName;
    private boolean gameOpen = true; //open to join
    private int gameId;
    private boolean gameOver = false;
    private boolean shipsMovable = true;

    private boolean myTurn = false;
    private int moveId = -1;
    private int largestMoveIdDone = -1;
    private boolean opponentMissed = false;

    private ArrayList<String> actionCache = new ArrayList<>();
    private ArrayList<String> uploadActionCache = new ArrayList<>();

    public Game(int gameid, BattleshipUser hostUser) {
        this(gameid, hostUser, false);
    }

    public Game(int gameid, BattleshipUser hostUser, boolean hosting) {
//		databaseConnector = new DatabaseConnector(Constants.DB_URL);
        this.gameId = gameid;
        this.hostUser = hostUser;
        this.hosting = hosting;

        Random random = new Random(gameid * 21937); //The seed has to be pretty big
        int result = random.nextInt(2);
        if (hosting && result == 0 || !hosting && result == 1) {
            myTurn = true;
        }
    }

    public int getGameId() {
        return gameId;
    }

    public BattleshipUser getHostUser() {
        return hostUser;
    }

    public BattleshipUser getJoinUser() {
        return joinUser;
    }

    public void setJoinUser(BattleshipUser newUser) {
        this.joinUser = newUser;
    }

    //whenever there is only one player (hosting game)
    public void waitForUser() { //waits for joinUser to join game

        //list game info in "join game window"
        //create thread
        //Poll database for user join
        //joinUser = databaseGetUser();
        gameOpen = false; //game full
    }

    public void doAction(String coordinates) {
        String[] coords = coordinates.split(",");
        int x = Integer.parseInt(coords[0]);
        int y = Integer.parseInt(coords[1]);
        int mid = Integer.parseInt(coords[2]);
        System.out.println(mid);
        int attack = board1.attack(x, y, false);

        if (attack == 0) {
            opponentMissed = true;
        }

        if(mid > largestMoveIdDone){
            largestMoveIdDone = mid;
        }

        if(opponentMissed && moveId==largestMoveIdDone){
            myTurn = true;
            opponentMissed = false;
        }
    }

    public void addCachedAction(String coords) {
        try {
            for (Iterator<String> it = actionCache.iterator(); it.hasNext(); ) {
                String s = it.next();
                if (s.equals(coords)) return;
            }
            actionCache.add(coords);
        } catch (ConcurrentModificationException e) {
            addCachedAction(coords);
        }
    }

    public void doCachedActions() {
        try {
            for (Iterator<String> it = actionCache.iterator(); it.hasNext(); ) {
                String coords = it.next();
                if (coords != null) doAction(coords);
                it.remove();
            }
        } catch (ConcurrentModificationException e) {
            doCachedActions();
        }
    }

    public void addUploadAction(String coords) {
        try {
            for (Iterator<String> it = uploadActionCache.iterator(); it.hasNext(); ) {
                String s = it.next();
                if (s.equals(coords)) return;
            }
            uploadActionCache.add(coords);
        } catch (ConcurrentModificationException e) {
            addUploadAction(coords);
        }
    }

    public void uploadCachedActions(String remove, int status) {
//        System.out.println("REUP: "+moveId);
        try {
            if (status == 2) {
                uploadActionCache.remove(remove);
            }
            uploadCachedActions();
        } catch (ConcurrentModificationException e) {
            uploadCachedActions(remove, status);
        }
    }

    public void uploadCachedActions() {
        DatabaseConnector db = new DatabaseConnector();
        int status = 0;
        String coords = null;
        try {
            for (int i = 0; i < uploadActionCache.size(); i++) {
                coords = uploadActionCache.get(i);
                status = 1;
                db.doAction(coords);
                status = 2;
                uploadActionCache.remove(coords);
                i--;
            }
        } catch (ConcurrentModificationException e) {
            uploadCachedActions(coords, status);
        }
    }

    public void incMoveID() {
        moveId++;
    }

    public boolean isMyTurn() {
        return myTurn;
    }

    public void setMyTurn(boolean myTurn) {
        this.myTurn = myTurn;
    }

    public boolean allActionsUploaded(){
        for(String s: uploadActionCache){
            if(s!=null) return false;
        }
        return true;
    }

    public boolean isGameOver() {
        boolean status = false;
        if (board1.shipsRemaining() == 0 || board2.shipsRemaining() == 0) {
            status = true;
            setMyTurn(false);
        }
        return status;
    }

    public int getMoveId() {
        return moveId;
    }

//    public void userJoined() {
//    }

    // mOtherfuckers be making method for gameover and last move id, and incomming move

    public void setBoards(Board board1, Board board2) {
        this.board1 = board1;
        this.board2 = board2;
    }

    public boolean isGameOpen() {
        return gameOpen;
    }

    public void setGameOpen(boolean gameOpen) {
        this.gameOpen = gameOpen;
    }

    public void setBoardsReady(boolean boardsReady) {
        this.boardsReady = boardsReady;
    }

    public boolean isBoardsReady() {
        return boardsReady;
    }

    public boolean isShipsMovable() {
        return shipsMovable;
    }

    public void setShipsMovable(boolean shipsMovable) {
        this.shipsMovable = shipsMovable;
    }

    @Override
    public String toString() {
        return "Game{" +
                "gameId=" + gameId +
                ", hostUser=" + hostUser +
                ", joinUser=" + joinUser +
                ", board1=" + board1 +
                ", board2=" + board2 +
//				", databaseConnector=" + databaseConnector +
                ", gameName='" + gameName + '\'' +
                ", gameOpen=" + gameOpen +
                '}';
    }
}
