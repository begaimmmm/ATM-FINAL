import java.util.Scanner;

public class Auth {

    private String loggedInUser = null;
    private String loggedInRole = null;

    private String[] usernames = {"admin", "user1"};
    private String[] passwords = {"admin123", "pass123"};
    private String[] roles     = {"ADMIN",   "USER"};

    public boolean login(Scanner scanner) {
        System.out.println("      ATM Login System       ");
        System.out.println("Accounts:");
        System.out.println("  admin / admin123  (ADMIN role)");
        System.out.println("  user1 / pass123   (USER role)");

        for (int attempts = 0; attempts < 3; attempts++) {
            System.out.print("Username: ");
            String username = scanner.nextLine().trim();

            System.out.print("Password: ");
            String password = scanner.nextLine().trim();

            for (int i = 0; i < usernames.length; i++) {
                if (usernames[i].equals(username) && passwords[i].equals(password)) {
                    loggedInUser = username;
                    loggedInRole = roles[i];
                    System.out.println("Login successful! Welcome, " + username + " [" + loggedInRole + "]");
                    return true;
                }
            }

            int attemptsLeft = 2 - attempts;
            System.out.println("Wrong username or password. Attempts left: " + attemptsLeft);
        }

        System.out.println("Too many failed attempts. Exiting.");
        return false;
    }

    public boolean isAdmin() {
        return "ADMIN".equals(loggedInRole);
    }

    public String getLoggedInUser() {
        return loggedInUser;
    }

    public String getLoggedInRole() {
        return loggedInRole;
    }
}
