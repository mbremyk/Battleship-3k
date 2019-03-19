/**
 * A Rectangle that displayed behind the cursor to visualize your next move
 *
 *
 * @Author Thorkildsen Torje
 */

package game;

import javafx.scene.paint.Color;
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
        setFill(Color.RED);
        setWidth(size * widthBlocks);
        setHeight(size * heightBlocks);
    }

    /**
     * Sets the x and y position of the Rectangle
     *
     * @param x The x position you want to set
     * @param y The y position you want to set
     */
    public void setPos(double x, double y) {
        setTranslateX(x - size / 2);
        setTranslateY(y - size / 2);
    }

    /**
     * Used to snap the MouseFollower-Rectangle to tile (x,y)
     *
     * @param startX The position of the left side of the tile-grid
     * @param startY The position of the top of the tile-grid
     * @param x Tile number x from left to right, 0 to (max number of tiles - 1)
     * @param y Tile number y from left to right, 0 to (max number of tiles - 1)
     */
    public void setTilePos(double startX, double startY, int x, int y) {
        setTranslateX(startX + x * Board.TILE_SIZE);
        setTranslateY(startY + y * Board.TILE_SIZE);
    }

    /**
     *
     * @param boardNumber What board you are currently selecting tiles on
     * @param x Tile number x that has been selected
     * @param y Tile number x that has been selected
     */
    public void setTiles(int boardNumber, int x, int y){
        this.currentBoardNumber = boardNumber;
        this.tileX = x;
        this.tileY = y;
    }

}
