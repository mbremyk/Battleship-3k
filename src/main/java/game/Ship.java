/**
 * Ship.java
 * <p>
 * A ship class for the board to use.
 * It stores its base position, rotation, width and height so the tiles it occupy can be calculated.
 * </p>
 *
 * @author Thorkildsen Torje
 */

package game;

import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

public class Ship extends Rectangle {

    /**
     * First element is the x-position in of the upper left corner of the Ship in the Board's grid
     * Second element is the y-position in of the upper left corner of the Ship in the Board's grid
     */
    private int[] basePosition;

    /**
     * Amount of tiles the Ship has in its width, which is along the x-axis if the ship's rotation is 0
     * (what you would normally call the length of a Ship).
     */
    private int widthTiles;

    /**
     * Amount of tiles the Ship has in its height, which is along the y-axis if the ship's rotation is 0
     * (what you would normally call the width of a Ship).
     */
    private int heightTiles;

    /**
     * How many tiles the Ship occupies in the x-direction in its current rotation
     */
    private int tilesX;

    /**
     * How many tiles the Ship occupies in the y-direction in its current rotation
     */
    private int tilesY;

    /**
     * A Ship's latest x-position-offset (in amount of tiles) to make the ship be displayed at the right position in the Board's grid
     * when it is rotated.
     */
    private int positionOffsetX;

    /**
     * A Ship's latest y-position-offset (in amount of tiles) to make the ship be displayed at the right position in the Board's grid
     * when it is rotated.
     */
    private int positionOffsetY;

    /**
     * A Ship's total x-position-offset (in amount of tiles) to make the ship be displayed at the right position in the Board's grid
     * when it is rotated.
     */
    private int totalPositionOffsetX = 0;

    /**
     * A Ship's total y-position-offset (in amount of tiles) to make the ship be displayed at the right position in the Board's grid
     * when it is rotated.
     */
    private int totalPositionOffsetY = 0;

    /**
     * True if the latest position-offset has been added to the Ship's position
     */
    private boolean offsetAdded = true;

    /**
     * The current rotation of the Ship in degrees
     */
    private int rotation;

    /**
     * The parent Board of the Ship
     */
    private final Board parentBoard;

    /**
     * The x-position of the latest mouse press. Used to make dragging Ships possible.
     */
    private double startDragX;

    /**
     * The y-position of the latest mouse press. Used to make dragging Ships possible.
     */
    private double startDragY;

    /**
     * The amount of tiles the Ship has which have not been attacked
     */
    private int health;

    /**
     * True if the ship has not been destroyed
     */
    private boolean alive = true;

    /**
     * Initializes a ship, loads its image and updates its position, size, rotation and visibility.
     * Creates some event listeners for dragging and rotation functionality.
     *
     * @param visible     if the ship is visible
     * @param tileX       x-position in the board-grid
     * @param tileY       y-position in the board-grid
     * @param width       width in amount of tiles
     * @param height      height in amount of tiles
     * @param rotation    rotation of the ship in degrees
     * @param parentBoard the board that contains the ship
     */
    public Ship(boolean visible, int tileX, int tileY, int width, int height, int rotation, Board parentBoard) {
        this.parentBoard = parentBoard;
        this.basePosition = new int[]{tileX, tileY};
        widthTiles = width;
        heightTiles = height;
        tilesX = width;
        tilesY = height;
        rotateRight(rotation / 90);
        setTilePos(tileX, tileY);
        setVisible(visible);
        loadImage(); //Must happen before updateSize()
        updateSize();
        health = width * height;

        //DELETE LATER
//        setTranslateX(parentBoard.getTranslateX() + tileX * Board.TILE_SIZE);
//        setTranslateY(parentBoard.getTranslateY() + tileY * Board.TILE_SIZE);


        //These make the Ship remember "real" position while being snapped to other tiles
        setOnMousePressed(event -> {
            startDragX = event.getSceneX() - (parentBoard.getTranslateX() + (getTileX()) * Board.TILE_SIZE);
            startDragY = event.getSceneY() - (parentBoard.getTranslateY() + (getTileY()) * Board.TILE_SIZE);
        });

        setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                rotateRight();
                updatePosition();
            }
        });

        setOnMouseDragged(event -> {
            if (Statics.getGame().isShipsMovable() && event.getButton() == MouseButton.PRIMARY) {
                this.setTranslateX(event.getSceneX() - startDragX + totalPositionOffsetX * Board.TILE_SIZE);
                this.setTranslateY(event.getSceneY() - startDragY + totalPositionOffsetY * Board.TILE_SIZE);
                updatePosition();
            }
        });
    }

    /**
     * Adds the saved offset from the Ship's rotation to make calculation correct.
     * Checks if the Ship is inside the parent board, and fixes it if not.
     * Updates the displayed position of the Ship to the its actual position.
     */
    private void updatePosition() {
        addOffset();
        int newTileX = getTileX();
        int newTileY = getTileY();
        if (getTileX() < 0) newTileX = 0;
        if (getTileX() > Board.TILES - tilesX) newTileX = Board.TILES - tilesX;
        if (getTileY() < 0) newTileY = 0;
        if (getTileY() > Board.TILES - tilesY) newTileY = Board.TILES - tilesY;
        setTilePos(newTileX, newTileY);
    }

    /**
     * Adds the current rotations position offset to the Ship's position to make it appear in the right place
     * Also adds the position offset of the current rotation to the totalPositionOffset variables
     */
    private void addOffset() {
        if (!offsetAdded) {
            setTranslateX(getTranslateX() + positionOffsetX * Board.TILE_SIZE);
            setTranslateY(getTranslateY() + positionOffsetY * Board.TILE_SIZE);
            offsetAdded = true;
            totalPositionOffsetX += positionOffsetX;
            totalPositionOffsetY += positionOffsetY;
        }
    }

    /**
     * Used to snap the Ship-Rectangle to tile (x,y) in the parent Board
     *
     * @param x Tile number x from left to right, 0 to (max number of tiles - 1 - width in tiles)
     * @param y Tile number y from left to right, 0 to (max number of tiles - 1 - height in tiles)
     */
    public void setTilePos(int x, int y) {
        setTranslateX(parentBoard.getTranslateX() + (x + totalPositionOffsetX) * Board.TILE_SIZE);
        setTranslateY(parentBoard.getTranslateY() + (y + totalPositionOffsetY) * Board.TILE_SIZE);
        updateBasePosition();
    }


    /**
     * Gets the x-position of the Ship's rotation point in the grid
     *
     * @return the x-position of the Ship's rotation point in the grid
     */
    public int getRotationCenterX() {
        return (int) ((getTranslateX() - parentBoard.getTranslateX() + Board.TILE_SIZE / 2) / Board.TILE_SIZE);
    }

    /**
     * Gets the y-position of the Ship's rotation point in the grid
     *
     * @return the y-position of the Ship's rotation point in the grid
     */
    public int getRotationCenterY() {
        return (int) ((getTranslateY() - parentBoard.getTranslateY() + Board.TILE_SIZE / 2) / Board.TILE_SIZE);
    }

    /**
     * Gets the x-position of the upper left corner of the Ship in the grid
     *
     * @return the x-position of the upper left corner of the Ship in the grid
     */
    public int getTileX() {
        int tileX = getRotationCenterX() - totalPositionOffsetX;
        basePosition[0] = tileX;
        return tileX;
    }

    /**
     * Gets the y-position of the upper left corner of the Ship in the grid
     *
     * @return the y-position of the upper left corner of the Ship in the grid
     */
    public int getTileY() {
        int tileY = getRotationCenterY() - totalPositionOffsetY;
        basePosition[1] = tileY;
        return tileY;
    }

    /**
     * Gets the rotation of the ship in degrees
     *
     * @return the rotation of the ship in degrees
     */
    public int getRotation() {
        return rotation;
    }

    /**
     * Reduces the health of the ship by 1
     *
     * @return the remaining health
     */
    public int reduceHealth() {
        health--;
        if (health <= 0) {
            setVisible(true);
            setOpacity(0.6);
            alive = false;
        }
        return health;
    }

    /**
     * Automatically loads the image file which fits the size of the ship. If that image doesn't exist,
     * the method will load a random color instead.
     */
    public void loadImage() {
        try {
            Image image = new Image("Ship" + widthTiles + "x" + heightTiles + ".png");
            ImagePattern imagePattern = new ImagePattern(image);
            setFill(imagePattern);
        } catch (IllegalArgumentException e) {
            setFill(Color.color(Math.random(), Math.random(), Math.random()));
        }
    }

    /**
     * Gets the status of the "alive" boolean class variable. Alive=false means the ship is completely destroyed.
     *
     * @return status of boolean class variable "alive"
     */
    public boolean isAlive() {
        return alive;
    }

    /**
     * Rotates the Ship 90 degrees clockwise a set amount of times (the parameter).
     *
     * @param times how many times the ship should rotate 90 degrees clockwise
     */
    private void rotateRight(int times) {
        for (int i = 0; i < times; i++) {
            rotateRight();
        }
    }

    /**
     * Rotates the ship 90 degrees clockwise and updates the Ship's position-offset to make the ship
     * be displayed at the right position in the Board's grid.
     */
    private void rotateRight() {
        rotation = (rotation + 90) % 360;

        double rads = Math.toRadians(rotation);
        tilesX = (int) Math.round(Math.abs(widthTiles * Math.cos(rads) + heightTiles * Math.sin(rads)));
        tilesY = (int) Math.round(Math.abs(heightTiles * Math.cos(rads) + widthTiles * Math.sin(rads)));

        offsetAdded = false;
        switch (rotation) {
            case 0:
                positionOffsetX = 0;
                positionOffsetY = -(widthTiles - 1);
                break;
            case 90:
                positionOffsetX = heightTiles - 1;
                positionOffsetY = 0;
                break;
            case 180:
                positionOffsetX = widthTiles - 1 - (heightTiles - 1);
                positionOffsetY = heightTiles - 1;
                break;
            case 270:
                positionOffsetX = -(widthTiles - 1);
                positionOffsetY = widthTiles - 1 - (heightTiles - 1);
                break;
        }
        updateBasePosition();
        updatePosition();

        getTransforms().add(new Rotate(90, Board.TILE_SIZE / 2, Board.TILE_SIZE / 2, 0, Rotate.Z_AXIS));
    }

    /**
     * Updates the size of the ship to be displayed with the correct width and height in the Board's grid
     */
    private void updateSize() {
        setWidth(widthTiles * Board.TILE_SIZE);
        setHeight(heightTiles * Board.TILE_SIZE);
    }

    /**
     * Updates the basePosition class variable so that it stores the x and y position of the Ship's upper left
     * corner in the grid.
     */
    private void updateBasePosition() {
        getTileX();
        getTileY();
    }

    /**
     * Gets the base position of the Ship, which is the upper left corner.
     *
     * @return an array with a length of 2, first element is x, second element is y. This is the position of the
     * upper left corner of the Ship in the Board's grid.
     */
    public int[] getBasePosition() {
        updateBasePosition();
        return basePosition;
    }

    /**
     * Get the amount of tiles the Ship has in its width, which is along the x-axis if the ship's rotation is 0
     * (what you would normally call the length of a Ship).
     *
     * @return how wide the ship (rectangle) is, in tiles
     */
    public int getWidthTiles() {
        return widthTiles;
    }

    /**
     * Get the amount of tiles the Ship has in its height, which is along the y-axis if the ship's rotation is 0
     * (what you would normally call the width of a Ship).
     *
     * @return how tall the ship (rectangle) is, in tiles
     */
    public int getHeightTiles() {
        return heightTiles;
    }

    /**
     * Gets how many tiles the Ship occupies in the x-direction in its current rotation
     *
     * @return how many tiles the Ship occupies in the x-direction in its current rotation
     */
    public int getTilesX() {
        return tilesX;
    }

    /**
     * Gets how many tiles the Ship occupies in the y-direction in its current rotation
     *
     * @return how many tiles the Ship occupies in the y-direction in its current rotation
     */
    public int getTilesY() {
        return tilesY;
    }
}
