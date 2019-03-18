/**
 * Class that stores coordinates of a user's ships
 * To get an object with information from the database, use the DatabaseConnector getCoordinates method
 *
 * @Author Thorkildsen Torje
 *
 * @see database.DatabaseConnector#getShipCoordinates(int, int)
 */

package game;

public class ShipCoordinates {
    private final int[][] coordinates;


    public ShipCoordinates(int[][] coordinates) {
        this.coordinates = coordinates;
    }

    public int[][] getCoordinates() {
        return coordinates;
    }

    @Override
    public String toString() {
        String ret = super.toString()+"\n";
        for (int i = 0; i < coordinates.length; i++) {
            ret += "{" + coordinates[i][0] + "," + coordinates[i][1] + "}";
        }
        return ret;
    }

    /**
     * testklient
     *
     * @param args
     */
    public static void main(String[] args) {
        database.DatabaseConnector databaseConnector = new database.DatabaseConnector(database.Constants.DB_URL);
        ShipCoordinates shipCoordinates = databaseConnector.getShipCoordinates(1, 5);

        System.out.println(shipCoordinates.toString());
    }
}
