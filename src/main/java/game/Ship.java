/**
 * @Author Thorkildsen Torje
 */

package game;

import controller.GameController;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Ship extends Rectangle {
    private int[] basePosition;
    private final int[][] size;
    private final Board parentBoard;

    private double startDragX;
    private double startDragY;


    public Ship(boolean visible, int tileX, int tileY, int width, int height, Board parentBoard) {
        this.parentBoard = parentBoard;
        this.basePosition = new int[]{tileX, tileY};
        this.size = new int[width][height];
        setVisible(visible);
        setFill(Color.color(Math.random(), Math.random(), Math.random()));
        setWidth(size.length * Board.TILE_SIZE);
        setHeight(size[0].length * Board.TILE_SIZE);
        setTranslateX(parentBoard.getTranslateX() + tileX * Board.TILE_SIZE);
        setTranslateY(parentBoard.getTranslateY() + tileY * Board.TILE_SIZE);


        setOnMousePressed(event -> {
            startDragX = event.getX();
            startDragY = event.getY();
        });

        setOnMouseDragged(event -> {
            if (GameController.isShipsMovable()) {
                this.setTranslateX(event.getSceneX() - startDragX);
                this.setTranslateY(event.getSceneY() - startDragY);
                if (isInsideBoard()) {
                    this.setTranslateX(parentBoard.getTranslateX() + getTileX() * Board.TILE_SIZE);
                    this.setTranslateY(parentBoard.getTranslateY() + getTileY() * Board.TILE_SIZE);
                }
            }
        });

        setOnMouseReleased(event -> {
            if (!isInsideBoard()) {
                int newTileX = getTileX();
                int newTileY = getTileY();
                if (getTileX() < 0) newTileX = 0;
                if (getTileX() > Board.TILES - width) newTileX = Board.TILES - width;
                if (getTileY() < 0) newTileY = 0;
                if (getTileY() > Board.TILES - height) newTileY = Board.TILES - height;
                setTilePos(newTileX, newTileY);
            }
        });
    }

    /**
     * Checks if the ship is completely inside the parent Board
     *
     * @return true if ship is located completely inside the parent Board, else false
     */
    private boolean isInsideBoard() {
        if (getTranslateX() > parentBoard.getTranslateX() && getTranslateY() > parentBoard.getTranslateY()
                && getTranslateX() + size.length * Board.TILE_SIZE < parentBoard.getTranslateX() + parentBoard.getFitWidth()
                && getTranslateY() + size[0].length * Board.TILE_SIZE < parentBoard.getTranslateY() + parentBoard.getFitHeight()) {
            return true;
        }
        return false;
    }

    /**
     * Used to snap the Ship-Rectangle to tile (x,y) in the parent Board
     *
     * @param x Tile number x from left to right, 0 to (max number of tiles - 1 - width in tiles)
     * @param y Tile number y from left to right, 0 to (max number of tiles - 1 - height in tiles)
     */
    public void setTilePos(int x, int y) {
        setTranslateX(parentBoard.getTranslateX() + x * Board.TILE_SIZE);
        setTranslateY(parentBoard.getTranslateY() + y * Board.TILE_SIZE);
        basePosition[0] = x;
        basePosition[1] = y;
    }

    public int getTileX() {
        int tileX = (int) ((getTranslateX() - parentBoard.getTranslateX() + Board.TILE_SIZE / 2) / Board.TILE_SIZE);
        basePosition[0] = tileX;
        return tileX;
    }

    public int getTileY() {
        int tileY = (int) ((getTranslateY() - parentBoard.getTranslateY() + Board.TILE_SIZE / 2) / Board.TILE_SIZE);
        basePosition[1] = tileY;
        return tileY;
    }


    public int[] getBasePosition() {
        return basePosition;
    }

    public int[][] getSize() {
        return size;
    }
}
