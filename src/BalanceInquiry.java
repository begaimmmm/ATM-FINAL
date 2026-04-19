import java.time.LocalDateTime;

// ДОЧЕРНИЙ КЛАСС — наследуется от транзакции
public class BalanceInquiry extends Transaction {

    public BalanceInquiry(String accountNumber) {
        super(accountNumber, "BALANCE_INQUIRY");
    }

    // Конструктор для загрузки из файла
    public BalanceInquiry(int id, String accountNumber, LocalDateTime dateTime) {
        super(id, accountNumber, "BALANCE_INQUIRY", dateTime);
    }

    @Override
    public String getSummary() {
        return "Balance checked for account " + getAccountNumber();
    }

    @Override
    public String toFileLine() {
        // Сумма для запроса баланса отсутствует, поэтому ставится 0
        return getId() + "," + getType() + "," + getAccountNumber() + ",0," + getFormattedDate();
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
