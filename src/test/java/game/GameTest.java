/**
 * GameTest.java
 * @author Grande Trym
 */
package game;

import javafx.scene.layout.AnchorPane;
import junit.framework.Assert;
import model.BattleshipUser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javax.swing.text.html.ImageView;
import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit testing of game logic in Game class. Excludes testing of methods involving graphics initialization and database connection.
 */
class GameTest {
    BattleshipUser hostUser;
    BattleshipUser joinUser;
    Game testHostGame;
    Game testJoinGame;

    /**
     * sets up local test users with games and boards. Database related methods are not tested.
     */
    @BeforeEach
    void setUp() {
        hostUser = new BattleshipUser(1, "dolan", "1234", "dolanmail@gmail.com", 1, 1);
        joinUser = new BattleshipUser(2, "dolan2", "1234", "dolanmail@gmail.com", 1, 1);
        testHostGame = new Game(1, "Dolan's game", hostUser, true);
        testJoinGame = new Game(1, "Dolan's game", hostUser, false);
//        testJoinGame.setJoinUser(joinUser);
        testJoinGame.setBoards(new Board(new AnchorPane(), 0, 0), new Board(new AnchorPane(), 0, 0));
    }

    /**
     * removes local user and game object after test is completed
     */
    @AfterEach
    void tearDown() {
    hostUser = null;
    joinUser = null;
    testHostGame = null;
    testJoinGame = null;
    }

    /**
     * checks that the inputed gameid is returned
     */
    @Test
    void getGameId() {
        assertEquals(1, testHostGame.getGameId());
    }

    /**
     * checks that the inputed game name is returned
     */
    @Test
    void getGameName() {
        assertEquals("Dolan's game", testHostGame.getGameName());
        assertEquals("Dolan's game", testJoinGame.getGameName());
    }

    /**
     * checks that the inputed hoosting variable is returned
     */
    @Test
    void isHosting() {
        assertTrue(testHostGame.isHosting());
        assertTrue(testHostGame.isHosting());
    }

    /**
     * checks that the set host user is returned
     */
    @Test
    void getHostUser() {
        assertEquals(hostUser.getUserId(), testHostGame.getHostUser().getUserId());
    }

    /**
     * checks that the set join user is returned
     */
    @Test
    void getJoinUser() {
        testJoinGame.setJoinUser(joinUser);
        assertEquals(joinUser, testJoinGame.getJoinUser());

    }

    /**
     * checks if the board returns the expected string array of the board
     * test fails although the expected string is identical to the actual one
     */
    @Test
    void getBoard1() {
        System.out.println(testHostGame.getBoard1());
        assertEquals("|0,0,0,0,0,0,0,0,0,0|\n" +
                "|0,0,0,0,0,0,0,0,0,0|\n" +
                "|0,0,0,0,0,0,0,0,0,0|\n" +
                "|0,0,0,0,0,0,0,0,0,0|\n" +
                "|0,0,0,0,0,0,0,0,0,0|\n" +
                "|0,0,0,0,0,0,0,0,0,0|\n" +
                "|0,0,0,0,0,0,0,0,0,0|\n" +
                "|0,0,0,0,0,0,0,0,0,0|\n" +
                "|0,0,0,0,0,0,0,0,0,0|\n" +
                "|0,0,0,0,0,0,0,0,0,0|\n", testHostGame.getBoard1());
    }

    /**
     * checks that the joinUser can be set
     */
    @Test
    void setJoinUser () {
        testJoinGame.setJoinUser(joinUser);
        assertEquals(joinUser, testJoinGame.getJoinUser());
    }

    /**
     * checks if action has been added to the cache
     */
    @Test
    void addCachedAction() {
        testHostGame.addCachedAction("01,01");
        assertEquals("01,01", testHostGame.getActionCache().get(testHostGame.getActionCache().size()-1));

        testHostGame.addCachedAction("10,10");
        assertEquals("10,10", testHostGame.getActionCache().get(testHostGame.getActionCache().size()-1));
    }

    /**
     * checks if action is executed on board and board updates given coordinate
     */
    @Test
    void doCachedAction() {
        testHostGame.doCachedActions();
//        assertEquals(0, testHostGame.getBoard1().getBoard()[0][0]);
    }

    /**
     * checks if moveId is incremented
     */
    @Test
    void incMoveID() {
        int moveId = testHostGame.getMoveId();
        testHostGame.incMoveID();
        assertEquals(moveId+1, testHostGame.getMoveId());
    }

    /**
     * checks if the value set is returned in method
     * checks if the get method returns set value
     */
    @Test
    void getMyTurnAndIsMyTurn() {
        testHostGame.setMyTurn(true);
        assertTrue(testHostGame.isMyTurn());

        testHostGame.setMyTurn(false);
        assertFalse(testHostGame.isMyTurn());
    }

    @Test
    void getGameState() {
    }

    /**
     * checks if set moveId is returned
     */
    @Test
    void getMoveId() {
        assertEquals(-1, testHostGame.getMoveId());
    }

    /**
     * checks if method returns isGameOpen
     */
    @Test
    void isGameOpen() {
       assertEquals(true, testHostGame.isGameOpen());
    }

    /**
     * checks if method sets gameOpen to given value
     */
    @Test
    void setGameOpen() {
        testHostGame.setGameOpen(false);
        assertEquals(false, testHostGame.isGameOpen());
    }

    /**
     * checks if correct string representation is returned
     */
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