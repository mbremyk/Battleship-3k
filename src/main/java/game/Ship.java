/**
 * @Author Thorkildsen Torje
 */

package game;

import controller.GameController;
import javafx.geometry.Point3D;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

public class Ship extends Rectangle {
    private int[] basePosition;
    private int widthTiles;
    private int heightTiles;
    private int tilesX;
    private int tilesY;
    private double positionOffsetX;
    private double positionOffsetY;
    private int rotation;
    private final Board parentBoard;

    private double startDragX;
    private double startDragY;

    private int health;
    private boolean alive = true;


    public Ship(boolean visible, int tileX, int tileY, int width, int height, int rotation, Board parentBoard) {
        this.parentBoard = parentBoard;
        this.basePosition = new int[]{tileX, tileY};
        widthTiles = width;
        heightTiles = height;
        this.rotation = rotation;
        setVisible(visible);
        //setFill(Color.color(Math.random(), Math.random(), Math.random()));
        loadImage(); //Must happen before updateSize()
        updateSize();
        health = width * height;
        setTranslateX(parentBoard.getTranslateX() + tileX * Board.TILE_SIZE);
        setTranslateY(parentBoard.getTranslateY() + tileY * Board.TILE_SIZE);

        setOpacity(0.6); //TODO Remove/discuss this

        //These make the Ship remember "real" position while being snapped to other tiles
        setOnMousePressed(event -> {
            startDragX = event.getX();
            startDragY = event.getY();
        });

        setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                rotateRight();
                updatePosition();
            }
        });

        setOnMouseDragged(event -> {
            if (Statics.getGame().isShipsMovable() && event.getButton() == MouseButton.PRIMARY) {
                this.setTranslateX(event.getSceneX() - startDragX);
                this.setTranslateY(event.getSceneY() - startDragY);
                updatePosition();
            }
        });
    }

    private void updatePosition() {
        if (isInsideBoard()) {
            this.setTranslateX(parentBoard.getTranslateX() + getTileX() * Board.TILE_SIZE);
            this.setTranslateY(parentBoard.getTranslateY() + getTileY() * Board.TILE_SIZE);
        } else {
            int newTileX = getTileX();
            int newTileY = getTileY();
            if (getTileX() < 0) newTileX = 0;
            if (getTileX() > Board.TILES - tilesX) newTileX = Board.TILES - tilesX;
            if (getTileY() < 0) newTileY = 0;
            if (getTileY() > Board.TILES - tilesY) newTileY = Board.TILES - tilesY;
            setTilePos(newTileX, newTileY);
        }
    }

    /**
     * Checks if the ship is completely inside the parent Board
     *
     * @return true if ship is located completely inside the parent Board, else false
     */
    private boolean isInsideBoard() {
        double posX = getTranslateX() -positionOffsetX;
        double posY = getTranslateY() -positionOffsetY;
        if (posX > parentBoard.getTranslateX() && posY > parentBoard.getTranslateY()
                && posX + tilesX * Board.TILE_SIZE < parentBoard.getTranslateX() + parentBoard.getFitWidth()
                && posY + tilesY * Board.TILE_SIZE < parentBoard.getTranslateY() + parentBoard.getFitHeight()) {
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
        setTranslateX(parentBoard.getTranslateX()+positionOffsetX + x * Board.TILE_SIZE);
        setTranslateY(parentBoard.getTranslateY()+positionOffsetY + y * Board.TILE_SIZE);
        basePosition[0] = x;
        basePosition[1] = y;
    }

    public int getTileX() {
        int tileX = (int) ((getTranslateX() - parentBoard.getTranslateX() + Board.TILE_SIZE / 2-positionOffsetX) / Board.TILE_SIZE);
        basePosition[0] = tileX;
        return tileX;
    }

    public int getTileY() {
        int tileY = (int) ((getTranslateY() - parentBoard.getTranslateY() + Board.TILE_SIZE / 2-positionOffsetY) / Board.TILE_SIZE);
        basePosition[1] = tileY;
        return tileY;
    }

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

    public void loadImage() {
        try {
            Image image = new Image("Ship" + widthTiles + "x" + heightTiles + ".png");
            ImagePattern imagePattern = new ImagePattern(image);
            setFill(imagePattern);
        } catch (IllegalArgumentException e) {
            setFill(Color.color(Math.random(), Math.random(), Math.random()));
        }
    }

    public boolean isAlive() {
        return alive;
    }

    private void rotateRight() {
        rotation = (rotation + 90) % 360;
//        int tempWidth = widthTiles;
//        widthTiles = heightTiles;
//        heightTiles = tempWidth;

        double rads = Math.toRadians(rotation);
        tilesX = (int) Math.round(Math.abs(widthTiles * Math.cos(rads) + heightTiles * Math.sin(rads)));
        tilesY = (int) Math.round(Math.abs(heightTiles * Math.cos(rads) + widthTiles * Math.sin(rads)));

//        setTilePos(getTileX()+1,getTileY());//+tilesY/2);
//        setTranslateX(getTranslateX()+Board.TILE_SIZE);
        switch (rotation){
            case 0:
                positionOffsetX = 0;
                break;
            case 90:
                positionOffsetX = Board.TILE_SIZE;
                break;
            case 180:
                positionOffsetX = Board.TILE_SIZE*2;
                positionOffsetY = Board.TILE_SIZE;
                break;
            case 270:
                positionOffsetX = 0;
                positionOffsetY = Board.TILE_SIZE*2;
                break;
        }

        System.out.println(getTileX() + "," + getTileY() + "," + tilesX + "," + tilesY);

//        updateSize();
//        setRotationAxis(new Point3D(getTranslateX(),getTranslateY(),0));
//        setRotate(rotation);
        getTransforms().add(new Rotate(90, Board.TILE_SIZE / 2, Board.TILE_SIZE / 2, 0, Rotate.Z_AXIS));
//        getTransforms().add(new Rotate(90, Board.TILE_SIZE/(2/heightTiles), Board.TILE_SIZE/(2/heightTiles),0, Rotate.Z_AXIS));
    }

    private void updateSize() {
        setWidth(widthTiles * Board.TILE_SIZE);
        setHeight(heightTiles * Board.TILE_SIZE);
//        setRotate(rotation);
        getTransforms().add(new Rotate(rotation, 0, 0, 0, Rotate.Z_AXIS));
    }


    public int[] getBasePosition() {
        return basePosition;
    }

    public int getWidthTiles() {
        return widthTiles;
    }

    public int getHeightTiles() {
        return heightTiles;
    }

    public int getTilesX() {
        return tilesX;
    }

    public int getTilesY() {
        return tilesY;
    }
}
