import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Database {

    private static final String URL = "jdbc:sqlite:transactions.db";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private Connection connection;

    public Database() {
        try {
            connection = DriverManager.getConnection(URL);
            createTable();
            System.out.println("Database connected: transactions.db");
        } catch (SQLException e) {
            System.out.println("Database connection error: " + e.getMessage());
        }
    }

    private void createTable() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS transactions (" +
                "id       INTEGER PRIMARY KEY," +
                "type     TEXT    NOT NULL," +
                "account  TEXT    NOT NULL," +
                "amount   REAL    NOT NULL," +
                "datetime TEXT    NOT NULL)";
        Statement stmt = connection.createStatement();
        stmt.execute(sql);
    }

    public void save(Transaction t) {
        String sql = "INSERT OR REPLACE INTO transactions (id, type, account, amount, datetime) VALUES (?,?,?,?,?)";
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, t.getId());
            pstmt.setString(2, t.getType());
            pstmt.setString(3, t.getAccountNumber());

            if (t instanceof Withdrawal) {
                pstmt.setDouble(4, ((Withdrawal) t).getAmount());
            } else if (t instanceof Deposit) {
                pstmt.setDouble(4, ((Deposit) t).getAmount());
            } else {
                pstmt.setDouble(4, 0);
            }

            pstmt.setString(5, t.getFormattedDate());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error saving to database: " + e.getMessage());
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM transactions WHERE id = ?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error deleting from database: " + e.getMessage());
        }
    }

    public void loadInto(TransactionManager manager) {
        String sql = "SELECT * FROM transactions ORDER BY id";
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            int count = 0;
            while (rs.next()) {
                int id         = rs.getInt("id");
                String type    = rs.getString("type");
                String account = rs.getString("account");
                double amount  = rs.getDouble("amount");
                LocalDateTime dt = LocalDateTime.parse(rs.getString("datetime"), FORMATTER);

                Transaction t = null;
                switch (type) {
                    case "WITHDRAWAL":
                        t = new Withdrawal(id, account, amount, dt);
                        break;
                    case "DEPOSIT":
                        t = new Deposit(id, account, amount, dt);
                        break;
                    case "BALANCE_INQUIRY":
                        t = new BalanceInquiry(id, account, dt);
                        break;
                }

                if (t != null) {
                    manager.addTransactionSilent(t);
                    count++;
                }
            }
            System.out.println("Loaded " + count + " transaction(s) from database.");

        } catch (SQLException e) {
            System.out.println("Error loading from database: " + e.getMessage());
        }
    }

    public void saveAll(TransactionManager manager) {
        for (Transaction t : manager.getTransactions()) {
            save(t);
        }
        System.out.println("All transactions saved to database.");
    }

    public void close() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            System.out.println("Error closing database: " + e.getMessage());
        }
    }
}
