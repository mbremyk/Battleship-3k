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

    public static void setGame(Game _game){
        game = _game;
    }
}
