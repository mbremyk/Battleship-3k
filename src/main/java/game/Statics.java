/**
 * Statics.java
 * <p>
 * Static objects to be available from the whole project
 *
 * @author Grande Trym
 * @author Thorkildsen Torje
 * @author Brevik Magnus
 */

package game;

import database.PullThread;
import model.BattleshipUser;

public class Statics {
    private static Game game;
    private static BattleshipUser localUser;
    private static PullThread pullThread;

    /**
     * Gets the Thread polling the database
     *
     * @return reference to Statics.pullThread if there is one
     */
    public static PullThread getPullThread() {
        return pullThread;
    }

    /**
     * Sets the static pullThread to a specific PullThread
     *
     * @param pullThread the new PullThread
     */
    public static void setPullThread(PullThread pullThread) {
        Statics.pullThread = pullThread;
    }

    /**
     * Gets the local Game object
     *
     * @return reference to the local Game object if there is one
     */
    public static Game getGame() {
        return game;
    }

    /**
     * Gets the local logged in user
     *
     * @return reference to the local logged in user if there is one
     */
    public static BattleshipUser getLocalUser() {
        return localUser;
    }

    /**
     * Sets the local user. Used in logging in a user and adding them to a game
     *
     * @param localUser the new BattleshipUser
     */
    public static void setLocalUser(BattleshipUser localUser) {
        Statics.localUser = localUser;
    }

    /**
     * Sets the local game
     *
     * @param _game the new Game
     */
    public static void setGame(Game _game) {
        game = _game;
    }
}
