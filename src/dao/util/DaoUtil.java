package dao.util;

import configuration.JDBCConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DaoUtil {

    public static ResultSet getList(boolean isConnectionReadOnly, String queryString) {
        Connection connection = isConnectionReadOnly ? JDBCConnection.connectionReadOnly : JDBCConnection.connection;
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(queryString);
            return resultSet;

        } catch (SQLException e) {
            System.err.print("Error in queryString");
            return null;
        }
    }
}
