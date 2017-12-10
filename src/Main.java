import configuration.JDBCConnection;
import dao.UserDaoImpl;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {

        try {
            //JDBCConnection.executeCreateTable();
            UserDaoImpl userDao = new UserDaoImpl();
            userDao.getUsers();
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                System.out.print(" >>>> ");
                String input = br.readLine();

                String operation = input;
                String value = null;

                if (input.indexOf(" ") >= 0){
                    operation = input.substring(0, input.indexOf(" "));
                    value = input.substring(input.indexOf(" ") + 1);
                }

                switch (operation){
                    case "select":
                        userDao.getUsers();
                        break;
                    case "insert":
                        userDao.insertUser(value);
                        break;
                    case "commit":
                        userDao.commitTransaction();
                        break;
                    case "selectinto":
                        userDao.getUsersCurrentlySaved();
                        break;
                    default:
                        break;
                }
                if (operation.equalsIgnoreCase("exit")) {
                    JDBCConnection.connection.close();
                    return;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error executing option");

        } catch (IOException e) {
            System.err.println("Error reading the typed line");
        }
    }
}
