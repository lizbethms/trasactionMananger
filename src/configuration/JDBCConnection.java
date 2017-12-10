package configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class JDBCConnection {

    private final static String DATABASE_DRIVER = "com.mysql.jdbc.Driver";
    private final static String DATABASE_URL = "jdbc:mysql://localhost:3306/test";
    private final static String USERNAME = "root";
    private final static String PASSWORD = "root";
    public final static Connection connection;
    public final static Connection connectionReadOnly;

    static {
        connection = getConnection();
        connectionReadOnly = getConnection();
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public static Connection getConnection() {
        Connection connection = null;
        try {

            Class.forName(DATABASE_DRIVER);
            Properties properties = new Properties();
            properties.put("user", USERNAME);
            properties.put("password", PASSWORD);

            connection = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD);
            System.out.println("Connection initialized");

        } catch (ClassNotFoundException | SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            return connection;
        }
    }


    public static void executeCreateTable() throws SQLException {
        Statement statement = null;

        try {
            statement = connection.createStatement();

            StringBuilder query = new StringBuilder();
            query.append(" CREATE TABLE IF NOT EXISTS USERS ( ");
            query.append(" `name` VARCHAR(10) NOT NULL ) ");

            statement.execute(query.toString());

        } catch (SQLException e) {
            System.err.println("Error creating table");
            System.err.println(e.getMessage());


        } finally {
            if (statement != null){
                statement.close();
                System.out.println("Statement tx closed");
            }
            if (connection != null) {
                connection.close();
                System.out.println("Connection closed");

            }
        }

    }
}
