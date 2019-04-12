/**
 * A Rectangle that displayed behind the cursor to visualize a user's next move.
 *
 * @author Thorkildsen Torje
 */

package game;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class MouseFollower extends Rectangle {

    /**
     * Width of the attack in amount of tiles in the grid, 1 if normal attack
     */
    private int widthBlocks = 1;

    /**
     * Height of the attack in amount of tiles in the grid, 1 if normal attack
     */
    private int heightBlocks = 1;

    /**
     * The width and height og the MouseFollower in pixels
     */
    private double size = Board.TILE_SIZE;

    /**
     * Initializes a new MouseFollower.
     * This object can be moved by a parent AnchorPane to make it follow the mouse or something else.
     */
    public MouseFollower() {
        setMouseTransparent(true); //Not blocking MouseEvents
        setOpacity(0.6);
        pressed(false);
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
     * Changes color if the object is pressed
     *
     * @param pressed true if this object has been pressed/selected
     */
    public void pressed(boolean pressed) {
        if (pressed) {
//            setFill(Color.GRAY);
            setFill(new ImagePattern(new Image("./AttackPressed.png")));
        } else {
//            setFill(Color.WHITE);
            setFill(new ImagePattern(new Image("./AttackNotPressed.png")));
        }
    }

}
