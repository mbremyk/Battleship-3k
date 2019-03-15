/**
 * Class that stores coordinates of a user's ships
 *
 * @Author Thorkildsen Torje
 */

package game;

public class ShipCoordinates {
    private final int[] coordinatesX;
    private final int[] coordinatesY;

    public ShipCoordinates(int[] coordinatesX, int[] coordinatesY) {
        this.coordinatesX = coordinatesX;
        this.coordinatesY = coordinatesY;
    }

    public int[] getCoordinatesX() {
        return coordinatesX;
    }

    public int[] getCoordinatesY() {
        return coordinatesY;
    }


    /**
     * testklient
     * @param args
     */
    public static void main(String[] args){
        database.DatabaseConnector databaseConnector = new database.DatabaseConnector(database.Constants.DB_URL);
        ShipCoordinates shipCoordinates = databaseConnector.getShipCoordinates(1,5);

        int[] x = shipCoordinates.getCoordinatesX();
        int[] y = shipCoordinates.getCoordinatesY();

        for (int i = 0; i<x.length; i++){
            System.out.println("{"+x[i]+","+y[i]+"}");
        }
    }
}
