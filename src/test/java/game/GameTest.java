/**
 * GameTest.java
 * @author Grande Trym
 */
package game;

import junit.framework.Assert;
import model.BattleshipUser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    BattleshipUser hostUser;
    BattleshipUser joinUser;
    Game testHostGame;
    Game testJoinGame;

    @BeforeEach
    void setUp() {
        hostUser = new BattleshipUser(1, "dolan", "1234", "dolanmail@gmail.com", 1, 1);
        joinUser = new BattleshipUser(2, "dolan2", "1234", "dolanmail@gmail.com", 1, 1);
        testHostGame = new Game(1, "Dolan's game", hostUser, true);
        testJoinGame = new Game(1, "Dolan's game", hostUser, false);
        this.setJoinUser();
        this.setBoards();
    }

    @AfterEach
    void tearDown() {
    hostUser = null;
    joinUser = null;
    testHostGame = null;
    }

    @Test
    void getGameId() {
        assertEquals(1, testHostGame.getGameId());
    }

    @Test
    void getGameName() { 
        assertEquals("Dolan's game", testHostGame.getGameName());
        assertEquals("Dolan's game", testJoinGame.getGameName());
    }

    @Test
    void isHosting() {
        assertTrue(testHostGame.isHosting());
        assertTrue(testHostGame.isHosting());
    }

    @Test
    void getHostUser() {
        assertEquals(hostUser.getUserId(), testHostGame.getHostUser().getUserId());
    }

    @Test
    void getJoinUser() {
        assertEquals(joinUser, testJoinGame.getJoinUser());

    }

    @Test
    void setJoinUser () {
        testJoinGame.setJoinUser(joinUser);
    }

    @Test
    void addCachedAction() {
        testHostGame.addCachedAction("01,01");
        assertEquals("01,01", testHostGame.getActionCache().get(testHostGame.getActionCache().size()-1));

        testHostGame.addCachedAction("10,10");
        assertEquals("10,10", testHostGame.getActionCache().get(testHostGame.getActionCache().size()-1));
    }

    @Test
    void doCachedAction() {
        testHostGame.doCachedActions();
//        assertEquals(0, testHostGame.getBoard1().getBoard()[0][0]);
    }

    @Test
    void incMoveID() {
        int moveId = testHostGame.getMoveId();
        testHostGame.incMoveID();
        assertEquals(moveId+1, testHostGame.getMoveId());
    }

    @Test
    void isMyTurn() {
        testHostGame.setMyTurn(true);
        assertTrue(testHostGame.isMyTurn());

        testHostGame.setMyTurn(false);
        assertFalse(testHostGame.isMyTurn());
    }

    //duplicated by isMyTurn()
    @Test
     void setMyTurn() {
        testHostGame.setMyTurn(true);
        assertTrue(testHostGame.isMyTurn());

        testHostGame.setMyTurn(false);
        assertFalse(testHostGame.isMyTurn());
    }

    @Test
    void isGameOver() {
        //place ships on board first

    }

    @Test
    void getGameState() {
    }

//    @Test
//    void isGameOver() {
//        testHostGame.getBoard1().getBoard().shipsRemaining = 0;
//        testHostGame.board2.shipsRemaining() = 1;
//        assertTrue(testHostGame.isGameOver());
//
//        testHostGame.board1.shipsRemaining() = 1;
//        testHostGame.board2.shipsRemaining() = 0;
//        assertTrue(testHostGame.isGameOver());
//
//        testHostGame.board1.shipsRemaining() = 1;
//        testHostGame.board2.shipsRemaining() = 0;
//        assertFalse(testHostGame.isGameOver());
//    }

    @Test
    void getMoveId() {
        assertEquals(-1, testHostGame.getMoveId());
    }

    @Test
    void getGameResult() {

    }

    @Test
    void setBoards() {
        //testHostGame.setBoards(new Board()); HJELP
    }

    @Test
    void isGameOpen() {
       assertEquals(true, testHostGame.isGameOpen());
    }

    @Test
    void setGameOpen() {
        testHostGame.setGameOpen(false);
        assertEquals(false, testHostGame.isGameOpen());
    }


    @Test
    void setBoardsReady() {
    }

    @Test
    void isBoardsReady() {
    }

    @Test
    void isShipsMovable() {
    }

    @Test
    void setShipsMovable() {
    }

    @Test
    void testToString() {
        String exp = "Game{" +
                "gameId=1" +
                ", hostUser=" + hostUser +
                ", joinUser=" + joinUser +
                ", board1=" + testHostGame.getBoard1() +
                ", board2=" + testHostGame.getBoard2() +
//				", databaseConnector=" + databaseConnector +
                ", gameName='" + "Dolan's game" + '\'' +
                ", gameOpen=" + testHostGame.isGameOpen() +
                '}';
    }
}