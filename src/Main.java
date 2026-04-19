import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Auth auth = new Auth();
        if (!auth.login(scanner)) {
            scanner.close();
            return;
        }

        TransactionManager manager = new TransactionManager();
        Database db = new Database();
        db.loadInto(manager);
        manager.loadFromFile();

        boolean running = true;
        while (running) {
            System.out.println("\n   MAIN MENU    ");
            System.out.println("1. Make a Withdrawal");
            System.out.println("2. Make a Deposit");
            System.out.println("3. Check Balance");
            System.out.println("4. View All Transactions");
            System.out.println("5. Delete a Transaction     [ADMIN only]");
            System.out.println("6. Export to CSV");
            System.out.println("7. Import from CSV");
            System.out.println("8. Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    handleWithdrawal(scanner, manager);
                    break;
                case "2":
                    handleDeposit(scanner, manager);
                    break;
                case "3":
                    handleBalanceInquiry(scanner, manager);
                    break;
                case "4":
                    manager.printAllTransactions();
                    break;
                case "5":
                    if (!auth.isAdmin()) {
                        System.out.println("Access denied. Only ADMIN can delete transactions.");
                    } else {
                        handleDelete(scanner, manager);
                    }
                    break;
                case "6":
                    manager.exportToCSV("export.csv");
                    break;
                case "7":
                    manager.importFromCSV("export.csv");
                    break;
                case "8":
                    manager.saveToFile();
                    System.out.println("Goodbye! Data saved.");
                    db.saveAll(manager);
                    db.close();
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a number from 1 to 8.");
            }
        }

        scanner.close();
    }
    // Вспомогательные методы для поддержания чистоты main
    private static void handleWithdrawal(Scanner scanner, TransactionManager manager) {
        System.out.print("Enter account number: ");
        String account = scanner.nextLine().trim();
        if (account.isEmpty()) {
            System.out.println("Account number cannot be empty.");
            return;
        }
        System.out.print("Enter withdrawal amount: ");
        double amount = readPositiveAmount(scanner);
        if (amount <= 0) return;
        Withdrawal w = new Withdrawal(account, amount);
        manager.addTransaction(w);
        System.out.println("Withdrawal recorded! " + w.getSummary());
    }

    private static void handleDeposit(Scanner scanner, TransactionManager manager) {
        System.out.print("Enter account number: ");
        String account = scanner.nextLine().trim();
        if (account.isEmpty()) {
            System.out.println("Account number cannot be empty.");
            return;
        }
        System.out.print("Enter deposit amount: ");
        double amount = readPositiveAmount(scanner);
        if (amount <= 0) return;
        Deposit d = new Deposit(account, amount);
        manager.addTransaction(d);
        System.out.println("Deposit recorded! " + d.getSummary());
    }

    private static void handleBalanceInquiry(Scanner scanner, TransactionManager manager) {
        System.out.print("Enter account number: ");
        String account = scanner.nextLine().trim();
        if (account.isEmpty()) {
            System.out.println("Account number cannot be empty.");
            return;
        }
        BalanceInquiry b = new BalanceInquiry(account);
        manager.addTransaction(b);
        System.out.println("Balance inquiry recorded! " + b.getSummary());
    }

    private static void handleDelete(Scanner scanner, TransactionManager manager) {
        manager.printAllTransactions();
        System.out.print("Enter transaction ID to delete: ");
        String idStr = scanner.nextLine().trim();
        try {
            int id = Integer.parseInt(idStr);
            manager.deleteTransaction(id);
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID. Please enter a number.");
        }
    }
    // Считывает положительное число от пользователя, возвращает -1, если оно недействительно
    private static double readPositiveAmount(Scanner scanner) {
        try {
            double amount = Double.parseDouble(scanner.nextLine().trim());
            if (amount <= 0) {
                System.out.println("Amount must be greater than zero.");
                return -1;
            }
            return amount;
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount. Please enter a number.");
            return -1;
        }
    }
}