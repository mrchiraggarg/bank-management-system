import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class User {
    // we made these variables for particular this class
    private Connection connection;
    private Scanner scanner;

    // with the help of contructor, initializing the values of coonection and scanner from their instances of class which is defined in BankingApp class
    public User(Connection connection, Scanner scanner){
        this.connection = connection; // overwriting the value of private variables with the help of this keyword
        this.scanner = scanner; // overwriting the value of private variables with the help of this keyword
    }

    public void register () {
        scanner.nextLine();
        System.out.print("Full Name: ");
        String full_name = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        if (userExist(email)) {
            System.out.println("Account is already created with this email address. Please try again later.");
            return;
        }

        String register_query = "INSERT INTO users (full_name, email, password) VALUES (?, ?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(register_query);
            preparedStatement.setString(1, full_name);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, password);

            int affected_rows = preparedStatement.executeUpdate();
            if (affected_rows > 0) {
                System.out.println("Congrats! User has been registered successfully.");
            } else {
                System.out.println("Oops! Registration has been failed due to some technical error. Please try again later.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String login () {
        scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        String login_query = "SELECT * FROM users WHERE email = ? AND password = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(login_query);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return email;
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean userExist (String email) {
        String userExist_query = "SELECT * FROM users WHERE email = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(userExist_query);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return true;
            } else {
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
