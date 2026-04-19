import java.time.LocalDateTime;

// ДОЧЕРНИЙ КЛАСС — наследуется от транзакции
public class Withdrawal extends Transaction {

    private double amount;

    public Withdrawal(String accountNumber, double amount) {
        super(accountNumber, "WITHDRAWAL");
        this.amount = amount;
    }

    // Конструктор для загрузки из файла
    public Withdrawal(int id, String accountNumber, double amount, LocalDateTime dateTime) {
        super(id, accountNumber, "WITHDRAWAL", dateTime);
        this.amount = amount;
    }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

//    Полиморфизм, каждый подкласс предоставляет свою собственную версию функции getSummary().
    @Override
    public String getSummary() {
        return "Withdrew $" + String.format("%.2f", amount) + " from account " + getAccountNumber();
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
