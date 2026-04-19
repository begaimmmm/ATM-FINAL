import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

// Этот класс управляет всеми транзакциями
public class TransactionManager {

    private List<Transaction> transactions = new ArrayList<>();
    private static final String DATA_FILE = "transactions.txt";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    public void addTransaction(Transaction t) {
        transactions.add(t);
        System.out.println("Transaction #" + t.getId() + " added.");
    }

    //печать всех транзакций
    public void printAllTransactions() {
        if (transactions.isEmpty()) {
            System.out.println("No transactions found.");
            return;
        }
        System.out.println("\n--- ALL TRANSACTIONS ---");
        for (Transaction t : transactions) {
            System.out.println(t);
        }
        System.out.println("Total: " + transactions.size() + " transaction(s).");
    }

    //  изменить номер счета транзакции по идентификатору
    public void updateAccountNumber(int id, String newAccountNumber) {
        Transaction found = findById(id);
        if (found == null) {
            System.out.println("Transaction with ID " + id + " not found.");
            return;
        }
        found.setAccountNumber(newAccountNumber);
        System.out.println("Transaction #" + id + " updated.");
    }

    // удалить транзакцию по идентификатору
    public void deleteTransaction(int id) {
        Transaction found = findById(id);
        if (found == null) {
            System.out.println("Transaction with ID " + id + " not found.");
            return;
        }
        transactions.remove(found);
        System.out.println("Transaction #" + id + " deleted.");
    }

    // Returns the full list — used by GUI (Bonus 3) and Database (Bonus 2)
    public List<Transaction> getTransactions() {
        return transactions;
    }

    // Adds a transaction without printing "Transaction #N added." — used when loading from DB
    public void addTransactionSilent(Transaction t) {
        transactions.add(t);
    }

    // find a transaction by its ID
    private Transaction findById(int id) {
        for (Transaction t : transactions) {
            if (t.getId() == id) {
                return t;
            }
        }
        return null;
    }

    // SAVE to file — writes each transaction as one line
    public void saveToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(DATA_FILE))) {
            for (Transaction t : transactions) {
                writer.println(t.toFileLine());
            }
            System.out.println("Data saved to " + DATA_FILE);
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    // LOAD from file — reads each line and recreates transaction objects
    public void loadFromFile() {
        File file = new File(DATA_FILE);
        if (!file.exists()) return; // no file yet, that's okay

        try (BufferedReader reader = new BufferedReader(new FileReader(DATA_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Transaction t = parseLineToTransaction(line);
                if (t != null) {
                    transactions.add(t);
                }
            }
            System.out.println("Loaded " + transactions.size() + " transaction(s) from file.");
        } catch (IOException e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }

    // EXPORT to CSV
    public void exportToCSV(String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("ID,Type,AccountNumber,Amount,DateTime");
            for (Transaction t : transactions) {
                writer.println(t.toFileLine());
            }
            System.out.println("Exported to " + filename);
        } catch (IOException e) {
            System.out.println("Error exporting: " + e.getMessage());
        }
    }

    // IMPORT from CSV
    public void importFromCSV(String filename) {
        File file = new File(filename);
        if (!file.exists()) {
            System.out.println("File not found: " + filename);
            return;
        }

        int count = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean firstLine = true;
            while ((line = reader.readLine()) != null) {
                if (firstLine) { firstLine = false; continue; } // skip header row
                Transaction t = parseLineToTransaction(line);
                if (t != null && findById(t.getId()) == null) {
                    transactions.add(t);
                    count++;
                }
            }
            System.out.println("Imported " + count + " new transaction(s) from " + filename);
        } catch (IOException e) {
            System.out.println("Error importing: " + e.getMessage());
        }
    }

    // Parses one line (CSV format) into a Transaction object
    private Transaction parseLineToTransaction(String line) {
        try {
            String[] parts = line.split(",");
            int id = Integer.parseInt(parts[0]);
            String type = parts[1];
            String account = parts[2];
            double amount = Double.parseDouble(parts[3]);
            LocalDateTime dateTime = LocalDateTime.parse(parts[4], FORMATTER);

            switch (type) {
                case "WITHDRAWAL":
                    return new Withdrawal(id, account, amount, dateTime);
                case "DEPOSIT":
                    return new Deposit(id, account, amount, dateTime);
                case "BALANCE_INQUIRY":
                    return new BalanceInquiry(id, account, dateTime);
                default:
                    System.out.println("Unknown type in file: " + type);
                    return null;
            }
        } catch (Exception e) {
            System.out.println("Could not parse line: " + line);
            return null;
        }
    }
}