# ATM Transaction Log System

> **Final OOP Project** | Object-Oriented Programming Course

-----

## Author
**Begaim Abdullaeva**
Ala-Too International University/ Comse-25

-----

## Project Description

The **ATM Transaction Log System** is a Java-based console application that simulates and manages ATM operations. Users can perform and log key ATM transactions — **withdrawals**, **deposits**, and **balance inquiries** — all tied to individual bank accounts. The system supports full CRUD operations on transaction records, validates all user input, and persists data between sessions using file storage.

A bonus **JavaFX GUI** version was implemented alongside the CLI, allowing users to interact through a graphical interface with buttons and forms. Additionally, the system supports **SQLite database** storage as an alternative to file-based persistence, and includes a **role-based authentication system** (Admin / User).

-----

## Objectives

- Simulate real ATM transaction logging with full CRUD functionality
- Apply OOP principles: Encapsulation, Inheritance, and Polymorphism
- Ensure data persistence across sessions via file storage (JSON / CSV)
- Validate all user inputs to prevent invalid operations (e.g., overdraft, empty fields)
- Provide a clean and intuitive command-line interface
- Implement import/export of transaction logs in CSV and JSON formats
- (Bonus) Build a JavaFX graphical interface for all operations
- (Bonus) Integrate SQLite database for persistent relational storage
- (Bonus) Add login system with Admin and User roles

-----

## Project Requirements List

|  |Requirement                                                                                          
|--|-----------------------------------------------------------------------------------------------------
|1 |CRUD operations on transaction records (Create, Read, Update, Delete)                                
|2 |Command-line interface with clear menus and prompts                                                  
|3 |Input validation (amount > 0, sufficient balance, non-empty fields)                                  
|4 |Data persistence via file storage (JSON) between sessions                                            
|5 |Modular design — separate classes/packages for each responsibility                                   
|6 |README documentation with project description, objectives, and usage                                 
|7 |Error handling for unexpected situations (file errors, invalid input)                                
|8 |Encapsulation — private fields with getters and setters in all model classes                         
|9 |Inheritance — `Transaction` parent class with `Withdrawal`, `Deposit`, `BalanceInquiry` child classes
|10|Polymorphism — overridden `display()` and `getType()` methods in each subclass                       
|11|Export transaction log to CSV and JSON formats                                                       
|12|Import transaction history from CSV / JSON file                                                      

-----

## Project Structure

```
ATM-Transaction-Log/
│
├── src/
│   ├── main/
│   │   └── Main.java                  # Entry point
│   ├── model/
│   │   ├── Transaction.java           # Abstract parent class
│   │   ├── Withdrawal.java            # Child class
│   │   ├── Deposit.java               # Child class
│   │   ├── BalanceInquiry.java        # Child class
│   │   └── Account.java               # Bank account model
│   ├── service/
│   │   ├── TransactionService.java    # Business logic / CRUD
│   │   └── AccountService.java        # Account management
│   ├── storage/
│   │   ├── FileStorage.java           # JSON file read/write
│   │   └── DatabaseStorage.java       # SQLite integration (bonus)
│   ├── ui/
│   │   ├── ConsoleUI.java             # CLI menus and prompts
│   │   └── GuiApp.java                # JavaFX GUI (bonus)
│   ├── auth/
│   │   └── AuthService.java           # Login & roles (bonus)
│   └── util/
│       ├── Validator.java             # Input validation
│       └── ExportImportUtil.java      # CSV / JSON export-import
│
├── data/
│   ├── transactions.json              # Persisted transaction data
│   └── accounts.json                  # Persisted account data
│
├── screenshots/                       # Test case screenshots
├── presentation/                      # Slides link (see below)
└── README.md
```

-----

## OOP Design

### Encapsulation

All model classes use **private fields** with public **getters and setters**:

```java
public class Account {
    private String accountNumber;
    private double balance;

    public String getAccountNumber() { return accountNumber; }
    public void setBalance(double balance) { this.balance = balance; }
}
```

### Inheritance

`Transaction` is the **abstract parent class**. Three child classes extend it:

```
Transaction (abstract)
├── Withdrawal
├── Deposit
└── BalanceInquiry
```

### Polymorphism

Each child class **overrides** `display()` and `getType()`:

```java
@Override
public String getType() { return "Withdrawal"; }

@Override
public void display() {
    System.out.println("[WITHDRAWAL] Amount: $" + amount + " | Date: " + date);
}
```

Usage via polymorphism:

```java
Transaction t = new Withdrawal(...);
t.display(); // calls Withdrawal's overridden version
```

-----

## Algorithms & Data Structures

`ArrayList<Transaction>` - Stores all transaction records in memory                        
`HashMap<String, Account>` - Maps account numbers to account objects for O(1) lookup         
File I/O (JSON)  - `Gson` library used to serialize/deserialize transaction objects
Input Validation - Numeric checks for amounts; format checks for account numbers   
Export/Import - CSV uses `BufferedWriter`/`BufferedReader`; JSON uses `Gson`    

**Key challenge:** Maintaining consistency between in-memory state and file storage after every operation — solved by saving to file automatically after each CRUD action.

-----

## How to Run

### Prerequisites

- Java 17+
- Maven (or run directly with `javac`)
- (Bonus) JavaFX SDK 17+ for GUI mode

### Run CLI Version

```bash
git clone https://github.com/[your-username]/atm-transaction-log.git
cd atm-transaction-log
javac -cp . src/**/*.java
java main.Main
```

### Run GUI Version (Bonus)

```bash
java --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml -cp . ui.GuiApp
```

-----

## CLI Menu Overview

```
MAIN MENU    
1. Make a Withdrawal
2. Make a Deposit
3. Check Balance
4. View All Transactions
5. Delete a Transaction     [ADMIN only]
6. Export to CSV
7. Import from CSV
8. Exit
Choose an option: 
```

-----

## Test Cases & Expected Outputs

|# |Input                                      |Expected Output                                    |
|--|-------------------------------------------|---------------------------------------------------|
|1 |Withdrawal: account `ACC001`, amount `$500`|Transaction logged, balance reduced by $500        |
|2 |Withdrawal exceeding balance               |Error: “Insufficient funds. Transaction cancelled.”|
|3 |Deposit: account `ACC001`, amount `$200`   |Transaction logged, balance increased by $200      |
|4 |Balance inquiry: account `ACC001`          |Displays current balance, no balance change        |
|5 |Enter negative amount (`-100`)             |Error: “Amount must be greater than zero.”         |
|6 |Enter empty account number                 |Error: “Account number cannot be empty.”           |
|7 |View all transactions (empty log)          |Message: “No transactions found.”                  |
|8 |Delete non-existent transaction ID         |Error: “Transaction not found.”                    |
|9 |Export to CSV                              |File `transactions.csv` created successfully       |
|10|Import from malformed CSV                  |Error: “Invalid file format. Import failed.”       |

### Screenshots
https://api105.ilovepdf.com/v1/download/yk1v1n8fxcrsbh3x26bpArtb1ythkhmr91kqbl5cnfwjsxw32gxztx28k9gp8tnbhr4q9nrkfj46mhdz9y8wxm36qvqq0n9bqcmjz384jk43hffsc1c2vn3tsf8zA1wdsmdy994rtss26yynb7fwswl1nzr3z1hvA5kAn4fmpwqyfvkqnmzq

-----

## Export & Import

### Export

- **CSV:** Each transaction is written as a row: `id, type, accountNumber, amount, date`
- **JSON:** Full transaction objects serialized using the `Gson` library

### Import

- The system reads CSV/JSON files and loads records into the current session
- Duplicate detection prevents importing already-existing transaction IDs

-----

## Bonus Features

### GUI (JavaFX)

- Full graphical interface with separate screens for each operation
- Table view for browsing all transactions
- Form-based input for creating and editing records
- File chooser dialog for import/export

### Database Integration (SQLite)

- All transactions stored in a local `atm.db` SQLite database
- CRUD operations executed via JDBC with prepared statements
- Database storage is selectable at startup (file vs. database mode)

### Authentication & User Roles

- Login screen requires username and password
- **Admin role:** Can view, edit, and delete any transaction
- **User role:** Can only view their own account’s transactions and create new ones
- Passwords stored as SHA-256 hashed strings

-----

## Presentation

🔗 **Presentation Slides:** https://docs.google.com/presentation/d/1WHHcM8kIhiAqrrm6AdJW-RL1NnqNlvQ9/edit?usp=drivesdk&ouid=113140489680189850969&rtpof=true&sd=true
-----

## Files Used for Data Storage

|File                              |Purpose                             |
|----------------------------------|------------------------------------|
|`data/transactions.json`          |Stores all transaction records      |
|`data/accounts.json`              |Stores account details and balances |
|`data/users.json`                 |Stores user credentials (bonus auth)|
|`atm.db`                          |SQLite database file (bonus)        |
|`exports/transactions.csv`        |Generated CSV export                |
|`exports/transactions_export.json`|Generated JSON export               |

-----

- All monetary values are stored as `double` and displayed with 2 decimal places
- Date/time is recorded automatically using `LocalDateTime.now()` at the moment of the transaction
