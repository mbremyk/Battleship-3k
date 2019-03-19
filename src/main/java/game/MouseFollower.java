package game;

import javafx.scene.shape.Rectangle;

public class MouseFollower extends Rectangle {
    private int widthBlocks = 1;
    private int heightBlocks = 1;
    private double size = Board.TILE_SIZE;
    private int currentBoardNumber = -1;
    private int tileX = -1;
    private int tileY = -1;

    public MouseFollower() {
        setMouseTransparent(true); //Not blocking MouseEvents
        setWidth(size * widthBlocks);
        setHeight(size * heightBlocks);
    }

    public void setPos(double x, double y) {
        setTranslateX(x - size / 2);
        setTranslateY(y - size / 2);
    }

    public void setTilePos(double startX, double startY, int x, int y) {
        setTranslateX(startX + x * Board.TILE_SIZE);
        setTranslateY(startY + y * Board.TILE_SIZE);
    }

    public void setTiles(int boardNumber, int x, int y){
        this.currentBoardNumber = boardNumber;
        this.tileX = x;
        this.tileY = y;
    }

}
