import java.sql.*;
import java.util.Scanner;
import static java.lang.Class.forName;

public class BankingApp {
    // private keyword is used for security purpose, I don't want to share these with anybody. No one can access outside of this class
    // static keyword is used because I don't want I have to create object of class everytime to access these variables
    // final keyword is used because these values are final values. It will never change or overwrite.

    private static final String url = "jdbc:mysql://localhost:1800/java_banking";
    private static final String username = "root";
    private static final String password = "password";

    // throws used for defining exception classes
    public static void main (String[] args) throws ClassNotFoundException, SQLException { // creating constructor for class BankingApp with try catch
        try {
            Class.forName("com.mysql.cj.jbdc.driver"); // we are loading mysql connection in driver with the help of method Class.forName()
        } catch (ClassNotFoundException e) {
            System.err.println("Error Found: " + e.getMessage()); // System.err.println is used for printing a message in red color like an error
        }

        try {
            Connection connection = DriverManager.getConnection(url, username, password); // creating connection object with the help of DriverManager
            Scanner scanner = new Scanner(System.in); // creating scanner object of Scanner class

            // passing connection and scanner objects and getting into all other classes parameterized constructor i.e. User, Accounts, AccountManager
            User user = new User(connection, scanner);
            Accounts accounts = new Accounts(connection, scanner);
            AccountManager accountManager = new AccountManager(connection, scanner);

            String email;
            long account_number;

            while (true) {
                System.out.println("** WELCOME TO BANKING SYSTEM **");
                System.out.println();
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                System.out.println("Please select your desired option: ");
                int case1 = scanner.nextInt();

                switch (case1) {
                    case 1:
                        user.register(); // calling user class method with the help of their object
                        break;
                    case 2:
                        email = user.login();
                        if (email != null) {
                            System.out.println();
                            System.out.println("Congrats! You have logged in successfully.");
                            if (!accounts.accountExist(email)) {
                                System.out.println();
                                System.out.println("1. Open New Bank Account");
                                System.out.println("2. Exit");
                                if (scanner.nextInt() == 1) {
                                    account_number = accounts.openAccount(email);
                                    System.out.println("Hurray! Your account has been created successfully.");
                                    System.out.println("Account Number: " + account_number);
                                } else {
                                    break;
                                }
                            }

                            account_number = accounts.getAccountNumber(email);
                            int case2 = 0;
                            while (case2 != 5) {
                                System.out.println();
                                System.out.println("1. Debit Money");
                                System.out.println("2. Credit Money");
                                System.out.println("3. Transfer Money");
                                System.out.println("4. Check Balance");
                                System.out.println("5. Log Out");
                                System.out.println("Please select your desired option: ");
                                case2 = scanner.nextInt();
                                switch (case2) {
                                    case 1:
                                        accountManager.debitMoney(account_number);
                                        break;
                                    case 2:
                                        accountManager.creditMoney(account_number);
                                        break;
                                    case 3:
                                        accountManager.transferMoney(account_number);
                                        break;
                                    case 4:
                                        accountManager.getBalance(account_number);
                                        break;
                                    case 5:
                                        break;
                                    default:
                                        System.out.println("Oops! You must input the valid choice.");
                                        break;
                                }
                            }

                        } else {
                            System.out.println("Might be you entered the invalid credentials. Please try again later.");
                        }
                    case 3:
                        System.out.println("Thanks for using our banking management system. Good Bye:)");
                        break;
                    default:
                        System.out.println("Oops! You must input the valid choice.");
                        break;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
