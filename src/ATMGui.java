import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ATMGui extends JFrame {

    private TransactionManager manager = new TransactionManager();
    private JTextArea outputArea;

    public ATMGui() {
        manager.loadFromFile();
        buildWindow();
    }

    private void buildWindow() {
        setTitle("ATM Transaction Log");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(750, 520);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(5, 5));
        getContentPane().setBackground(new Color(13, 27, 42));

        // ── Title bar ──
        JLabel title = new JLabel("  ATM Transaction Log", SwingConstants.LEFT);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setForeground(new Color(20, 184, 166));
        title.setBackground(new Color(27, 58, 92));
        title.setOpaque(true);
        title.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(title, BorderLayout.NORTH);

        // ── Output area ──
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        outputArea.setBackground(new Color(13, 27, 42));
        outputArea.setForeground(new Color(240, 244, 248));
        outputArea.setCaretColor(Color.WHITE);
        outputArea.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        JScrollPane scroll = new JScrollPane(outputArea);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(15, 113, 115), 1));
        add(scroll, BorderLayout.CENTER);

        // ── Button panel ──
        JPanel buttonPanel = new JPanel(new GridLayout(2, 4, 6, 6));
        buttonPanel.setBackground(new Color(13, 27, 42));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));

        String[] labels = {"Withdraw", "Deposit", "Check Balance", "View All",
                "Delete", "Export CSV", "Import CSV", "Exit"};

        for (String label : labels) {
            JButton btn = createButton(label);
            buttonPanel.add(btn);
        }

        add(buttonPanel, BorderLayout.SOUTH);
        setVisible(true);
        print("Welcome! Data loaded. Choose an operation below.");
    }

    private JButton createButton(String label) {
        JButton btn = new JButton(label);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setBackground(new Color(27, 58, 92));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(new Color(15, 113, 115), 1));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(new Color(15, 113, 115));
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(new Color(27, 58, 92));
            }
        });

        btn.addActionListener(e -> handleButton(label));
        return btn;
    }

    private void handleButton(String label) {
        switch (label) {

            case "Withdraw": {
                String acc = askInput("Enter account number:");
                if (acc == null) return;
                String amtStr = askInput("Enter withdrawal amount:");
                if (amtStr == null) return;
                try {
                    double amount = Double.parseDouble(amtStr);
                    if (amount <= 0) { print("Amount must be greater than zero."); return; }
                    Withdrawal w = new Withdrawal(acc, amount);
                    manager.addTransaction(w);
                    print("✓ " + w.getSummary());
                } catch (NumberFormatException ex) {
                    print("Invalid amount. Please enter a number.");
                }
                break;
            }

            case "Deposit": {
                String acc = askInput("Enter account number:");
                if (acc == null) return;
                String amtStr = askInput("Enter deposit amount:");
                if (amtStr == null) return;
                try {
                    double amount = Double.parseDouble(amtStr);
                    if (amount <= 0) { print("Amount must be greater than zero."); return; }
                    Deposit d = new Deposit(acc, amount);
                    manager.addTransaction(d);
                    print("✓ " + d.getSummary());
                } catch (NumberFormatException ex) {
                    print("Invalid amount. Please enter a number.");
                }
                break;
            }

            case "Check Balance": {
                String acc = askInput("Enter account number:");
                if (acc == null) return;
                BalanceInquiry b = new BalanceInquiry(acc);
                manager.addTransaction(b);
                print("✓ " + b.getSummary());
                break;
            }

            case "View All": {
                List<Transaction> list = manager.getTransactions();
                if (list.isEmpty()) {
                    print("No transactions found.");
                    return;
                }
                print("\n--- ALL TRANSACTIONS ---");
                for (Transaction t : list) {
                    print(t.toString());
                }
                print("Total: " + list.size() + " transaction(s).");
                break;
            }

            case "Delete": {
                String idStr = askInput("Enter transaction ID to delete:");
                if (idStr == null) return;
                try {
                    int id = Integer.parseInt(idStr);
                    manager.deleteTransaction(id);
                    print("Transaction #" + id + " deleted.");
                } catch (NumberFormatException ex) {
                    print("Invalid ID. Please enter a number.");
                }
                break;
            }

            case "Export CSV": {
                manager.exportToCSV("export.csv");
                print("✓ Exported to export.csv");
                break;
            }

            case "Import CSV": {
                manager.importFromCSV("export.csv");
                print("✓ Imported from export.csv");
                break;
            }

            case "Exit": {
                manager.saveToFile();
                print("Data saved. Goodbye!");
                System.exit(0);
                break;
            }
        }
    }

    private String askInput(String message) {
        String input = JOptionPane.showInputDialog(this, message);
        if (input == null || input.trim().isEmpty()) {
            print("Cancelled or empty input.");
            return null;
        }
        return input.trim();
    }

    private void print(String text) {
        outputArea.append(text + "\n");
        outputArea.setCaretPosition(outputArea.getDocument().getLength());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ATMGui());
    }
}