import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// РОДИТЕЛЬСКИЙ КЛАСС — все типы транзакций расширяют это
public abstract class Transaction {

    // Приватные поля (Encapsulation)
    private int id;
    private String accountNumber;
    private String type;
    private LocalDateTime dateTime;

    private static int nextId = 1; // auto-increment ID counter

//    конструктор
    public Transaction(String accountNumber, String type) {
        this.id = nextId++;
        this.accountNumber = accountNumber;
        this.type = type;
        this.dateTime = LocalDateTime.now();
    }

    //Конструктор, используемый при загрузке из файла (идентификатор уже существует)
    public Transaction(int id, String accountNumber, String type, LocalDateTime dateTime) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.type = type;
        this.dateTime = dateTime;
        if (id >= nextId) {
            nextId = id + 1; // держит счетчик впереди
        }
    }

    public int getId() { return id; }
    public String getAccountNumber() { return accountNumber; }
    public String getType() { return type; }
    public LocalDateTime getDateTime() { return dateTime; }

    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }

    // Каждый подкласс должен реализовывать это (полиморфизм)
    public abstract String getSummary();

//    Каждый подкласс должен реализовать это для сохранения файла
    public abstract String toFileLine();

    public String getFormattedDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dateTime.format(formatter);
    }

    // используется при печати всех транзакций
    @Override
    public String toString() {
        return "[" + id + "] " + type + " | Account: " + accountNumber + " | " + getFormattedDate();
    }
}
