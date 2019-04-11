/**
 * ConnectionPool.java
 *
 * <p>
 * Connection pool class for saving, listing, and distributing connections where they are needed.
 * Used instead of opening and closing connections every time a connection is needed, as these are expensive options
 * </p>
 *
 * @author Bjerke Thomas
 */

package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ConnectionPool {

    /**
     * The url for the JDBC connections
     */
    private String url;

    /**
     * ArrayList of available connections
     */
    private List<Connection> connectionPool;

    /**
     * ArrayList of connections in use
     */
    private List<Connection> usedConnections = new ArrayList<>();

    /**
     * The connection pool size. This should be as small as possible, and not smaller than the maximum number of concurrent threads.
     * A larger size means fewer clients can have the application open at the same time, limiting the concurrent active userbase
     */
    private static int INITIAL_POOL_SIZE = 2;

    /**
     * Initializes a new ConnectionPool with the specified url and List of connections
     *
     * @param url            String containing the URL for the JDBC connections
     * @param connectionPool List<Connection> of an already existing set of connections
     */
    public ConnectionPool(String url, List<Connection> connectionPool) {
        this.url = url;
        this.connectionPool = connectionPool;
    }

    /**
     * Initializes a new ConnectionPool with the specified url, List of connections and List of connections in use
     *
     * @param url             String containing the URL for the JDBC connections
     * @param connectionPool  List<Connection> of an already existing set of connections
     * @param usedConnections List<Connection> if an already existing set of connections in use
     */
    public ConnectionPool(String url, List<Connection> connectionPool, List<Connection> usedConnections) {
        this.url = url;
        this.connectionPool = connectionPool;
        this.usedConnections = usedConnections;
    }

    /**
     * Creates a new List of Connections with a size equal to INITIAL_POOL_SIZE and returns a new ConnectionPool
     *
     * @param url String containing the URL for the JDBC connections
     * @return a ConnectionPool with the specified url and a List of Connections
     * @throws SQLException if initialization of Connections failed
     */
    public static ConnectionPool create(String url) throws SQLException {
        List<Connection> pool = new ArrayList<>(INITIAL_POOL_SIZE);
        for (int i = 0; i < INITIAL_POOL_SIZE; i++) {
            pool.add(createConnection(url));
        }
        return new ConnectionPool(url, pool);
    }

    /**
     * Checks if the last Connection in the connectionPool List is valid, creates a new one if not, and returns a valid Connection
     *
     * @return Connection from the connection pool
     */
    public Connection getConnection() {
        Connection connection = null;
        if (connectionPool.size() > 0) {
            connection = connectionPool
                    .remove(connectionPool.size() - 1);
            try {
                if (!connection.isValid(0)) {
                    connection.close();
                    connection = createConnection(url);
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Exception in ConnectionPool");
            }
            usedConnections.add(connection);
        }
        return connection;
    }

    /**
     * Releases the specified Connection to the ConnectionPool for future use
     *
     * @param connection the Connection in use to release
     * @return true if the Connection was successfully released, false if not
     */
    public boolean releaseConnection(Connection connection) {
        connectionPool.add(connection);
        return usedConnections.remove(connection);
    }

    /**
     * Creates a new Connection with the spesified URL
     *
     * @param url the URL of the JDBC server
     * @return Connection to the JDBC server
     * @throws SQLException if a new Connections could not be created
     */
    private static Connection createConnection(String url)
            throws SQLException {
        return DriverManager.getConnection(url);
    }

    /**
     * Returns the amount of Connections, both free and in use
     *
     * @return int number of Connections
     */
    public int getSize() {
        return connectionPool.size() + usedConnections.size();
    }
}
