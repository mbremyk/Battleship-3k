package game;

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
    void setJoinUser (BattleshipUser joinUser) {

    }

    @Test
    void doAction() {

    }

    @Test
    void addCachedAction() {

    }

    @Test
    void doCachedAction() {

    }

    @Test
    void addUploadCachedActions() {

    }

    @Test
    void uploadCachedActions() {

    }

    @Test
    void uploadCachedActions() {

    }

    @Test
    void incMoveID() {
        int moveId = testHostGame.getMoveId();
        testHostGame.incMoveID();
        assertEquals(moveId+1, testHostGame.getMoveId());
    }

    @Test
    void isMyTurn() {

    }

    @Test
     void setMyTurn() {

    }

    @Test
    void allActionsUploaded() {

    }

    @Test
    void isGameOver() {

    }

    @Test
    void getGameResult() {

    }


    @Test
    void move() {
        //trenger koordinat-syntaks
    }

    @Test
    void getGameState() {
    }

//    @Test
//    void isGameOver() {
//        testHostGame.board1.shipsRemaining() = 0;
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
    }
}