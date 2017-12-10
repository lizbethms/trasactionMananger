package dao;

import configuration.JDBCConnection;
import dao.util.DaoUtil;

import java.sql.*;

public class UserDaoImpl {

    private static final String QUERY_GET_USERS = "Select id, name from users";

    public void getUsers() {

        ResultSet resultSet = DaoUtil.getList(false, QUERY_GET_USERS);
        printUserList(resultSet);
        try {
            resultSet.getStatement().close();
        } catch (SQLException e) {
            e.printStackTrace(System.out);
        }
    }

    private void printUserList(ResultSet resultSet) {
        if (resultSet == null) return;
        try {
            if (!resultSet.next()) {
                System.err.println(">>> No data for Users <<<");
            } else {

                System.out.println("+-------------+");
                System.out.println("|     name    |");
                System.out.println("+-------------+");

                resultSet.beforeFirst();
                while (resultSet.next()) {
                    System.out.print("| " + resultSet.getInt("id") + " | ");
                    System.out.print(resultSet.getString("name"));
                    System.out.print("\t\t" + "|" + "\n");
                }
                System.out.println("+-------------+");
            }
        } catch (SQLException e) {
            e.printStackTrace(System.out);
        }
    }

    private int getCountUsers() {
        int val = 0;
        try {
            Statement statement = JDBCConnection.connection.createStatement();
            ResultSet resultSet = statement.executeQuery("Select count(id) as val from users");
            if (resultSet.next()) {
                val = resultSet.getInt("val");
            }

            statement.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            val = 0;

        } finally {
            return val;
        }
    }

    public void getUsersCurrentlySaved() {
        try (Statement statement = JDBCConnection.connectionReadOnly.createStatement();
             ResultSet resultSet = statement.executeQuery("Select id, name from users")) {

            printUserList(resultSet);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public void insertUser(String name) {

        if (name == null || !(name.matches("[a-zA-Z]{1,10}"))) {
            return;
        }
        try {
            PreparedStatement pr = JDBCConnection.connection.prepareStatement("insert into users (id, name) values (?, ?)");
            int currentRows = getCountUsers();
            pr.setInt(1, currentRows + 1);
            pr.setString(2, name);
            pr.executeUpdate();
            pr.close();
        } catch (SQLException e) {
            rollbackTransaction();
        }

    }

    public void commitTransaction() {
        try {
            JDBCConnection.connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void rollbackTransaction() {
        try {
            JDBCConnection.connection.rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
