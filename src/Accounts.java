import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Accounts {
    // we made these variables for particular this class
    private Connection connection;
    private Scanner scanner;

    // with the help of contructor, initializing the values of coonection and scanner from their instances of class which is defined in BankingApp class
    public Accounts (Connection connection, Scanner scanner){
        this.connection = connection; // overwriting the value of private variables with the help of this keyword
        this.scanner = scanner; // overwriting the value of private variables with the help of this keyword
    }

    public long openAccount (String email) {
        if (!accountExist(email)) {
            scanner.nextLine();
            System.out.print("Full Name: ");
            String full_name = scanner.nextLine();
            System.out.print("Initial Amount: ");
            double balance = scanner.nextDouble();
            System.out.print("Security Pin: ");
            int security_pin = scanner.nextInt();

            String openAccount_query = "INSERT INTO accounts (account_number, full_name, email, security_pin, balance) VALUES (?, ?, ?, ?, ?)";
            try {
                long account_number = generateAccountNumber();
                PreparedStatement preparedStatement = connection.prepareStatement(openAccount_query);
                preparedStatement.setLong(1, account_number);
                preparedStatement.setString(2, full_name);
                preparedStatement.setString(3, email);
                preparedStatement.setInt(4, security_pin);
                preparedStatement.setDouble(5, balance);
                int affected_rows = preparedStatement.executeUpdate();

                if(affected_rows > 0) {
                    return account_number;
                } else {
                    throw new RuntimeException("Oops! Due to technical error, right now we are unable to create your account. Please try again later.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        throw new RuntimeException("Oops! You have already an account with this email. Please use that or create a new one with different email. Thanks.");
    }

    public long getAccountNumber (String email) {
        String getAccountNumber_query = "SELECT account_number from accounts WHERE email = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(getAccountNumber_query);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getLong("account_number");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("Oops! The account number you are trying doesn't exist. Please try again later.");
    }

    private long generateAccountNumber () {
        String generateAccountNumber_query = "SELECT account_number from accounts ORDER BY account_number DESC LIMIT 1";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(generateAccountNumber_query);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                long last_account_number = resultSet.getLong("account_number");
                return last_account_number+1;
            } else {
                return 10000100;
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return 10000100;
    }

    public boolean accountExist (String email){
        String query = "SELECT account_number FROM accounts WHERE email = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
