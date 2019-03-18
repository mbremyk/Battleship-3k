package game;

import model.BattleshipUser;

public class Player {
    private BattleshipUser user;
    private Board board;

    Player(BattleshipUser user, int boardNumber) {
        board = new Board();
    }

}
