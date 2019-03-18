package game;

import model.BattleshipUser;

/**
 * pre game lobby for hosting before someone joins
 * shows game info for joining player
 */

public class GameLobby {
    private BattleshipUser user1;
    private BattleshipUser user2;
    private String gameName;
    private BattleshipUser hostUser;
    private boolean gameOpen = false; //

    GameLobby(BattleshipUser user1, String gameName) {
        this.user1 = user1;
        this.gameName = gameName;
        waitForPlayer();
    }

    //whenever there is only one player (hosting game)
    public BattleshipUser waitForPlayer() { //waits for user2 to join game lobby

        //list game info in "join game window"

        //let user2 join lobby
        //return user2;
        createGame();
    }
    //whenever two players are in the lobby
    public void createGame() {
        gameOpen = true;
        new Game(user1, user2);
    }

}
