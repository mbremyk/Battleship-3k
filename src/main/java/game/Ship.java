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
    private int positionOffsetX;
    private int positionOffsetY;
    private int totalPositionOffsetX = 0;
    private int totalPositionOffsetY = 0;
    private boolean offsetAdded = true;
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
        tilesX = width;
        tilesY = height;
        rotateRight(rotation / 90);
        setTilePos(tileX,tileY);
        setVisible(visible);
        loadImage(); //Must happen before updateSize()
        updateSize();
        health = width * height;
        setTranslateX(parentBoard.getTranslateX() + tileX * Board.TILE_SIZE);
        setTranslateY(parentBoard.getTranslateY() + tileY * Board.TILE_SIZE);

        setOpacity(0.6);

        //These make the Ship remember "real" position while being snapped to other tiles
        setOnMousePressed(event -> {
            startDragX = event.getSceneX()-(parentBoard.getTranslateX()+(getTileX())*Board.TILE_SIZE);
            startDragY = event.getSceneY()-(parentBoard.getTranslateY()+(getTileY())*Board.TILE_SIZE);
            System.out.println(startDragX+","+startDragY);
        });

        setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                rotateRight();
                updatePosition();
            }
        });

        setOnMouseDragged(event -> {
            if (Statics.getGame().isShipsMovable() && event.getButton() == MouseButton.PRIMARY) {
                this.setTranslateX(event.getSceneX() - startDragX+totalPositionOffsetX*Board.TILE_SIZE);
                this.setTranslateY(event.getSceneY() - startDragY+totalPositionOffsetY*Board.TILE_SIZE);
                updatePosition();
            }
        });
    }

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

    private void addOffset() {
        if (!offsetAdded) {
            setTranslateX(getTranslateX() + positionOffsetX * Board.TILE_SIZE);
            setTranslateY(getTranslateY() + positionOffsetY * Board.TILE_SIZE);
            offsetAdded = true;
            totalPositionOffsetX += positionOffsetX;
            totalPositionOffsetY += positionOffsetY;
        }
//        positionOffsetX = 0;
//        positionOffsetY = 0;
    }

    /**
     * Checks if the ship is completely inside the parent Board
     *
     * @return true if ship is located completely inside the parent Board, else false
     */
    private boolean isInsideBoard() {
//        double posX = getTileX()*Board.TILE_SIZE;//-positionOffsetX*Board.TILE_SIZE;
//        double posY = getTileX()*Board.TILE_SIZE;//-positionOffsetY*Board.TILE_SIZE;
//        if (posX > parentBoard.getTranslateX() && posY > parentBoard.getTranslateY()
//                && posX + tilesX * Board.TILE_SIZE < parentBoard.getTranslateX() + parentBoard.getFitWidth()
//                && posY + tilesY * Board.TILE_SIZE < parentBoard.getTranslateY() + parentBoard.getFitHeight()) {
//            return true;
//        }
//        return false;

        return false;
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

    public int getRotationCenterX(){
        return (int) ((getTranslateX() - parentBoard.getTranslateX() + Board.TILE_SIZE / 2) / Board.TILE_SIZE);
    }

    public int getRotationCenterY(){
        return (int) ((getTranslateY() - parentBoard.getTranslateY() + Board.TILE_SIZE / 2) / Board.TILE_SIZE);
    }

    public int getTileX() {
        int tileX = getRotationCenterX() - totalPositionOffsetX;
        basePosition[0] = tileX;
        return tileX;
    }

    public int getTileY() {
        int tileY = getRotationCenterY() - totalPositionOffsetY;
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

    private void rotateRight(int times) {
        for (int i = 0; i < times; i++) {
            rotateRight();
        }
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
//                positionOffsetX = -2;
//                positionOffsetY = 1;
                break;
        }
//        positionOffsetX = Board.TILE_SIZE;
//        System.out.println(positionOffsetX);
//        System.out.println(getTranslateX());
        updateBasePosition();
        updatePosition();

//        System.out.println(getTileX() + "," + getTileY() + "," + positionOffsetX + "," + positionOffsetY);
        System.out.println(getTileX() + "," + getTileY() + "," + totalPositionOffsetX + "," + totalPositionOffsetY);

//        updateSize();
//        setRotationAxis(new Point3D(getTranslateX(),getTranslateY(),0));
//        setRotate(rotation);
//        setTilePos(getTileX(),getTileY());
//        if (rotation == 90)
//            getTransforms().add(new Rotate(90, Board.TILE_SIZE / 2, Board.TILE_SIZE / 2, 0, Rotate.Z_AXIS));
        getTransforms().add(new Rotate(90, Board.TILE_SIZE / 2, Board.TILE_SIZE / 2, 0, Rotate.Z_AXIS));
//        getTransforms().add(new Rotate(90, Board.TILE_SIZE/(2/heightTiles), Board.TILE_SIZE/(2/heightTiles),0, Rotate.Z_AXIS));
    }

    private void updateSize() {
        setWidth(widthTiles * Board.TILE_SIZE);
        setHeight(heightTiles * Board.TILE_SIZE);
    }

    private void updateBasePosition() {
        getTileX();
        getTileY();
    }


    public int[] getBasePosition() {
        updateBasePosition();
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
