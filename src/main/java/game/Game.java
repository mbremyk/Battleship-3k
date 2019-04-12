/**
 * Game.java
 * @author Thorkildsen Torje
 * @author Grande Trym
 */

package game;
import database.DatabaseConnector;
import model.BattleshipUser;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Random;

/**
 * Most of the game logic
 */
public class Game {
    private boolean hosting; //If the local player is hosting
    /**
     *
     */
    private BattleshipUser hostUser;
    private BattleshipUser joinUser;
    private BattleshipUser winner;
    private BattleshipUser loser;
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

    /**
     * @deprecated
     * @param gameid
     * @param gameName
     * @param hostUser
     */
    public Game(int gameid,String gameName, BattleshipUser hostUser) {
        this(gameid,gameName, hostUser, false);
    }

    /**
     * constructs new game either when user hosts or joins game
     * @param gameid used as PK in database to identify game. Hosting game and joining game have the same gameid.
     * @param gameName
     * @param hostUser the user hosting the game
     * @param hosting signalizes whether the local user is hosting the game or not.
     */
    public Game(int gameid,String gameName, BattleshipUser hostUser, boolean hosting) {
//		databaseConnector = new DatabaseConnector(Constants.DB_URL);
        this.gameId = gameid;
        this.gameName = gameName;
        this.hostUser = hostUser;
        this.hosting = hosting;

        Random random = new Random(gameid * 21937); //The seed has to be pretty big
        int result = random.nextInt(2);
        if (hosting && result == 0 || !hosting && result == 1) {
            myTurn = true;
        }
    }

    /**
     * helper method for unit testing
     * @return the local user's board
     */
    public Board getBoard1() {
        return board1;
    }

    /**
     * helper method for unit testing
     * @return the opponent user's board
     */
    public Board getBoard2() {
        return board2;
    }

    /**
     * helper method for unit testing
     * @return list of actions in the cache queued for database upload
     */
    public ArrayList<String> getActionCache() {
        return actionCache;
    }

    /**
     *
     * @return used as PK in database to identify game. Hosting game and joining game have the same gameid.
     */
    public int getGameId() {
        return gameId;
    }

    /**
     *
     * @return used to identify a game to the user in the games table. also appears in-game at the top.
     */
    public String getGameName() {
        return gameName;
    }

    /**
     *
     * @return true if the local user is hosting, false otherwise.
     */
    public boolean isHosting() {
        return hosting;
    }

    /**
     *
     * @return the user hosting the game
     */
    public BattleshipUser getHostUser() {
        return hostUser;
    }

    /**
     *
     * @return the user that joined the game
     */
    public BattleshipUser getJoinUser() {
        return joinUser;
    }

    /**
     * sets a new user to JoinUser
     * @param newUser user to be set as joinUser
     */
    public void setJoinUser(BattleshipUser newUser) {
        this.joinUser = newUser;
    }

    /**
     * executes an attack action on the opponent's board
     * @param coordinates xx,yy for where on the 10x10 board to attack
     */
    public void doAction(String coordinates) {
        String[] coords = coordinates.split(",");
        int x = Integer.parseInt(coords[0]);
        int y = Integer.parseInt(coords[1]);
        int mid = Integer.parseInt(coords[2]);
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

    /**
     * adds an attack action to a cache
     * @param coords xx,yy for where on the 10x10 board to attack
     */
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

    /**
     * sends all queued actions sequentially to be executed in doAction()
     */
    public void doCachedActions() {
        try {
            for (Iterator<String> it = actionCache.iterator(); it.hasNext(); ) {
                String coords = it.next();
                System.out.println(coords);
                if (coords != null) doAction(coords);
                it.remove();
            }
        } catch (ConcurrentModificationException e) {
            doCachedActions();
        }
    }

    /**
     * adds attack to upload cache
     * @param coords string cordinates on the form xx,yy
     */
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

    /**
     * uploads cached actions to database
     * @param remove HJELP
     * @param status HJELP
     */
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

    /**
     * HJELP
     */
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

    /**
     * increments moveId by one
     */
    public void incMoveID() {
        moveId++;
    }

    /**
     *
     * @return true if the local user has the turn, false otherwise
     */
    public boolean isMyTurn() {
        return myTurn;
    }

    /**
     * sets myTurn
     * @param myTurn true for turn to be passed to the local user, false for turn to be passed to opponent
     */
    public void setMyTurn(boolean myTurn) {
        this.myTurn = myTurn;
    }

    /**
     * checks if all actions are uploaded to database
     * @return true if all actions are uploaded, false otherwise
     */
    public boolean allActionsUploaded(){
        for(String s: uploadActionCache){
            if(s!=null) return false;
        }
        return true;
    }

    /**
     * checks if game is over
     * @return true if game is over, otherwise false
     */
    public boolean isGameOver() {
        if (board1.shipsRemaining() == 0 || board2.shipsRemaining() == 0) {
            gameOver = true;
            setMyTurn(false);
        }
        return gameOver;
    }

    /**
     * Sets the gameOver tag
     *
     * @param gameOver
     */
    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    /**
     * @return the id of the current move being executed
     */
    public int getMoveId() {
        return moveId;
    }

    /**
     * checks who won the game, used at the end of the game
     * @return 1 for local user winning, 0 for local user losing, -1 for fuck up/game not ended
     */
    public int getGameResult(){
        if(isGameOver()){
            if(board1.shipsRemaining() != 0){
                return 1;
            }
            else{
                return 0;
            }
        }
        return -1;
    }

    /**
     * gives the game two boards to play on
     * @param board1 the local user's board
     * @param board2 the opponent user's board
     */
    public void setBoards(Board board1, Board board2) {
        this.board1 = board1;
        this.board2 = board2;
    }

    /**
     * checks if game is joinable
     * @return true if the game is joinable, false otherwise
     */
    public boolean isGameOpen() {
        return gameOpen;
    }

    /**
     * sets game to open, used for whenever it is waiting for user to join
     * @param gameOpen true if the game is joinable, false otherwise
     */
    public void setGameOpen(boolean gameOpen) {
        this.gameOpen = gameOpen;
    }

    /**
     * setter method for boardsReady object variable
     * @param boardsReady true if both boards are uploaded to database after both players have pressed 'ready'
     */
    public void setBoardsReady(boolean boardsReady) {
        this.boardsReady = boardsReady;
    }

    /**
     * checks if both boards are initialized and both players have pressed 'ready'.
     * @return true if both are ready, false otherwise
     */
    public boolean isBoardsReady() {
        return boardsReady;
    }

    /**
     * checks if ships are movable or not HJELP
     * @return true if ships are movable, false otherwise
     */
    public boolean isShipsMovable() {
        return shipsMovable;
    }

    /**
     * sets whether ships are movable or not
     * @param shipsMovable true for ships movable, false otherwise
     */
    public void setShipsMovable(boolean shipsMovable) {
        this.shipsMovable = shipsMovable;
    }

    /**
     * desides loser of a game
     * @param loser user that lost the game
     */
    public void setLoser(BattleshipUser loser) {
        this.loser = loser;
        this.winner = Statics.getGame().getHostUser().getUserId() == loser.getUserId() ? Statics.getGame().getJoinUser() : Statics.getGame().getHostUser();
        setGameOver(true);
    }

    /**
     * Returns the winner of the local Game object
     *
     * @return BattleshipUser winner
     */
    public BattleshipUser getWinner() {
        return this.winner;
    }

    /**
     * Returns the loser of the local Game object
     *
     * @return BattleshipUser loser
     */
    public BattleshipUser getLoser() {
        return this.loser;
    }

    /**
     * standard toString()
     * @return textual representation of the game object
     */
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
