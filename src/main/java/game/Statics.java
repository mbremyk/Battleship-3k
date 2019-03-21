/**
 * Statics.java
 * @author Grande Trym
 * @author Thorkildsen Torje
 * @author Brevik Magnus
 */

package game;

import model.BattleshipUser;

public class Statics {
    private static  Game game;
    private static BattleshipUser localUser;

    public static Game getGame() {
        return game;
    }

    public static BattleshipUser getLocalUser(){
        return localUser;
    }

    public static void setLocalUser(BattleshipUser localUser) {
        Statics.localUser = localUser;
    }

    public static void setGame(Game _game){
        game = _game;
    }
}
