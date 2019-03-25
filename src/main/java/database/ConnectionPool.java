package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ConnectionPool{

    private String url;
    private List<Connection> connectionPool;
    private List<Connection> usedConnections = new ArrayList<>();
    private static int INITIAL_POOL_SIZE = 4;

    public ConnectionPool(String url, List<Connection> connectionPool){
        this.url = url;
        this.connectionPool = connectionPool;
    }

    public ConnectionPool(String url, List<Connection> connectionPool, List<Connection> usedConnections) {
        this.url = url;
        this.connectionPool = connectionPool;
        this.usedConnections = usedConnections;
    }

    public static ConnectionPool create(String url) throws SQLException {
        List<Connection> pool = new ArrayList<>(INITIAL_POOL_SIZE);
        for (int i = 0; i < INITIAL_POOL_SIZE; i++) {
            pool.add(createConnection(url));
        }
        return new ConnectionPool(url, pool);
    }

    // standard constructors

    public Connection getConnection() {
        Connection connection = null;
        if(connectionPool.size()>0) {
            connection = connectionPool
                    .remove(connectionPool.size() - 1);
            usedConnections.add(connection);
        }
        return connection;
    }

    public boolean releaseConnection(Connection connection) {
        connectionPool.add(connection);
        return usedConnections.remove(connection);
    }

    private static Connection createConnection(String url)
            throws SQLException {
        return DriverManager.getConnection(url);
    }

    public int getSize() {
        return connectionPool.size() + usedConnections.size();
    }

    // standard getters

}
