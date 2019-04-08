package game;

import model.BattleshipUser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    BattleshipUser hostUser;
    BattleshipUser joinUser;
    Game testGame;

    @BeforeEach
    void setUp() {
        hostUser = new BattleshipUser(1, "dolan", "1234", "dolanmail@gmail.com", 1, 1);
        joinUser = new BattleshipUser(2, "dolan2", "1234", "dolanmail@gmail.com", 1, 1);
        testGame = new Game(1, hostUser);
    }

    @AfterEach
    void tearDown() {
    hostUser = null;
    joinUser = null;
    testGame = null;
    }

    @Test
    void getGameId() {
        assertEquals(1, testGame.getGameId());
    }

    @Test
    void getGameName() {

    }

    @Test
    void isHosting() {

    }

    @Test
    void getHostUser() {

    }

    @Test
    void getJoinUser() {
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
        int moveId = testGame.getMoveId();
        testGame.incMoveID();
        assertEquals(moveId+1, testGame.getMoveId());
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
//        testGame.board1.shipsRemaining() = 0;
//        testGame.board2.shipsRemaining() = 1;
//        assertTrue(testGame.isGameOver());
//
//        testGame.board1.shipsRemaining() = 1;
//        testGame.board2.shipsRemaining() = 0;
//        assertTrue(testGame.isGameOver());
//
//        testGame.board1.shipsRemaining() = 1;
//        testGame.board2.shipsRemaining() = 0;
//        assertFalse(testGame.isGameOver());
//    }

    @Test
    void getMoveId() {
        assertEquals(-1, testGame.getMoveId());
    }

    @Test
    void getGameResult() {

    }

    @Test
    void setBoards() {
    }

    @Test
    void isGameOpen() {
       assertEquals(true, testGame.isGameOpen());
    }

    @Test
    void setGameOpen() {
        testGame.setGameOpen(false);
        assertEquals(false, testGame.isGameOpen());
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