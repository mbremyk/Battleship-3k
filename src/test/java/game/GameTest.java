//package game;
//
//import model.BattleshipUser;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class GameTest {
//    BattleshipUser hostUser;
//    BattleshipUser joinUser;
//    Game testGame;
//
//    @BeforeEach
//    void setUp() {
//        hostUser = new BattleshipUser(1, "dolan", "1234", "dolanmail@gmail.com", 1, 1);
//        joinUser = new BattleshipUser(2, "dolan2", "1234", "dolanmail@gmail.com", 1, 1);
//        testGame = new Game(1, hostUser);
//    }
//
//    @AfterEach
//    void tearDown() {
//    hostUser = null;
//    joinUser = null;
//    testGame = null;
//    }
//
//    @Test
//    void getGameId() {
//        assertEquals(1, testGame.getGameId());
//    }
//
//    @Test
//    void getHostUser() {
//    }
//
//    @Test
//    void getJoinUser() {
//    }
//
//    @Test
//    void setJoinUser() {
//    }
//
//    @Test
//    void doAction() {
//
//    }
//
//    @Test
//    void incMoveID() {
//        int moveId = testGame.getMoveId();
//        assertEquals(moveId+1, testGame.getMoveId());
//    }
//
//
//    @Test
//    void move() {
//
//    }
//
//    @Test
//    void getGameState() {
//    }
//
//    @Test
//    void isGameOver() {
//    }
//
//    @Test
//    void getMoveId() {
//    }
//
//    @Test
//    void userJoined() {
//    }
//
//    @Test
//    void setBoards() {
//    }
//
//    @Test
//    void isGameOpen() {
//    }
//
//    @Test
//    void setGameOpen() {
//    }
//
//    @Test
//    void setBoardsReady() {
//    }
//
//    @Test
//    void isBoardsReady() {
//    }
//
//    @Test
//    void isShipsMovable() {
//    }
//
//    @Test
//    void setShipsMovable() {
//    }
//
//    @Test
//    void testToString() {
//    }
//}