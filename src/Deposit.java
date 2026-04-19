import java.time.LocalDateTime;

// ДОЧЕРНИЙ КЛАСС — наследуется от транзакции
public class Deposit extends Transaction {

    private double amount;

    public Deposit(String accountNumber, double amount) {
        super(accountNumber, "DEPOSIT");
        this.amount = amount;
    }

    // Конструктор для загрузки из файла
    public Deposit(int id, String accountNumber, double amount, LocalDateTime dateTime) {
        super(id, accountNumber, "DEPOSIT", dateTime);
        this.amount = amount;
    }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    @Override
    public String getSummary() {
        return "Deposited $" + String.format("%.2f", amount) + " into account " + getAccountNumber();
    }

    @Override
    public String toFileLine() {
        return getId() + "," + getType() + "," + getAccountNumber() + "," + amount + "," + getFormattedDate();
    }

    @Override
    public String toString() {
        return super.toString() + " | Amount: $" + String.format("%.2f", amount);
    }
}
