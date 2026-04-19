# ATM Transaction Log

**Student:** Begaim Abdullaeva Comse-25

**Presentation:** https://docs.google.com/presentation/d/1WHHcM8kIhiAqrrm6AdJW-RL1NnqNlvQ9/edit?usp=drivesdk&ouid=113140489680189850969&rtpof=true&sd=true)

**Screenshots:** https://drive.google.com/file/d/1FbRpa4Tvb-CIuvH-QOmLNluNijCfbU5H/view?usp=sharing

## Description

ATM Transaction Log is a Java console application that simulates the transaction recording system of a real ATM machine. The program allows users to log withdrawals, deposits, and balance inquiries through a clean numbered menu in the terminal. All data is saved automatically to a file so nothing is lost between sessions. The project also includes three bonus features: a graphical user interface built with Java Swing, a SQLite database integration, and a login system with Admin and User roles.

## Objectives

- Build a working Java application that demonstrates all three core OOP principles: Encapsulation, Inheritance, and Polymorphism
- Implement full CRUD functionality — Create, Read, Update, and Delete — on transaction records
- Store all data persistently in a file so records survive after the program is closed
- Validate all user input to prevent the program from crashing on empty fields or invalid numbers
- Support CSV export and import so data can be backed up or shared
- Organize the code into logical, reusable classes so each class has one clear responsibility
- Implement a login system with Admin and User roles to control access
- Integrate a SQLite relational database as an alternative storage option
- Build a graphical user interface using Java Swing for users who prefer buttons over text commands

---

## Project Requirements (DONE)

|   | Requirement 
|---|-------------
| 1 | CRUD operations — Create, Read, Update, Delete transactions 
| 2 | Command-line interface with a numbered menu and clear prompts 
| 3 | Input validation — rejects empty account numbers and non-numeric amounts
| 4 | Data persistence — transactions saved to `transactions.txt` on exit, loaded on startup 
| 5 | Modular design — 6 classes, each with one clear responsibility 
| 6 | Documentation — README.md with full explanation in the repository root 
| 7 | Error handling — try-catch blocks on all file operations and number parsing 
| 8 | Encapsulation — all fields are private, accessed only via getters and setters
| 9 | Inheritance — 3 child classes extend the abstract Transaction parent class
| 10 | Polymorphism — `getSummary()` and `toFileLine()` are overridden in each child class 

---

## Project Structure

```
ATMTransactionLog/
├── src/
│   ├── Main.java                ← Entry point: menu loop and input handling
│   ├── Transaction.java         ← Abstract parent class with shared fields
│   ├── Withdrawal.java          ← Child class for withdrawal transactions
│   ├── Deposit.java             ← Child class for deposit transactions
│   ├── BalanceInquiry.java      ← Child class for balance check transactions
│   ├── TransactionManager.java  ← Handles all CRUD operations and file I/O
│   ├── Auth.java                ← Bonus 1: login system with Admin/User roles
│   ├── Database.java            ← Bonus 2: SQLite database integration via JDBC
│   └── ATMGui.java              ← Bonus 3: Swing graphical user interface
├── screenshots/                 ← Screenshots of all features with date/time visible
├── transactions.txt             ← Auto-created on first save — stores all transaction records
├── export.csv                   ← Created when user chooses Export CSV from the menu
├── .gitignore                   ← Excludes compiled files and IDE folders from Git
└── README.md                    ← This file
```

---

## How to Run

**Requirements:** Java JDK 8 or higher

```bash
# Step 1 — Compile all source files into the out/ folder
javac src/*.java -d out/

# Step 2 — Run the console version
java -cp out/ Main

# Step 3 — Run the GUI version (Bonus 3)
java -cp out/ ATMGui
```

**For the Database bonus (Bonus 2):** Download `sqlite-jdbc` JAR from `https://github.com/xerial/sqlite-jdbc/releases`, add it to project libraries in IntelliJ via File → Project Structure → Libraries → +, then recompile with:

```bash
javac -cp sqlite-jdbc.jar src/*.java -d out/
java -cp out/:sqlite-jdbc.jar Main
```

**Test accounts for login (Bonus 1):**

| Username | Password | Role |
|----------|----------|------|
| admin | admin123 | ADMIN — can do everything including delete |
| user1 | pass123 | USER — cannot delete transactions |

---

## OOP Principles

### 1. Encapsulation

All fields in the `Transaction` class are declared `private`. This means no other class can directly read or change them — they can only be accessed through public getter and setter methods. This protects the data from being accidentally modified.

```java
private int id;
private String accountNumber;
private String type;
private LocalDateTime dateTime;

public int getId()                              { return id; }
public String getAccountNumber()               { return accountNumber; }
public void setAccountNumber(String value)     { this.accountNumber = value; }
```

### 2. Inheritance

`Transaction` is an abstract parent class. The three child classes — `Withdrawal`, `Deposit`, and `BalanceInquiry` — extend it using the `extends` keyword. They automatically inherit all shared fields and methods from the parent without rewriting them.

```java
public class Withdrawal    extends Transaction { ... }
public class Deposit       extends Transaction { ... }
public class BalanceInquiry extends Transaction { ... }
```

This means every child class already has `id`, `accountNumber`, `type`, `dateTime`, `getId()`, `getFormattedDate()`, and `toString()` without writing a single line of code for them.

### 3. Polymorphism

The method `getSummary()` is declared `abstract` in the parent class `Transaction`. Each child class provides its own version of it. The same method name produces a completely different result depending on which object is calling it — this is runtime polymorphism.

```java
// Withdrawal.getSummary()   → "Withdrew $200.00 from account ACC001"
// Deposit.getSummary()      → "Deposited $500.00 into account ACC001"
// BalanceInquiry.getSummary() → "Balanced checked for account ACC001"
```

The same applies to `toFileLine()` — each class formats its own line for the file differently.

---

## Documentation

### Class Descriptions

**`Transaction.java`**
Abstract parent class. Stores all data that every transaction type shares: an auto-generated integer ID, the account number, the transaction type as a string, and the date and time recorded automatically with `LocalDateTime.now()` at the moment the transaction is created. Declares two abstract methods — `getSummary()` and `toFileLine()` — that every child class must implement. Also has a second constructor used only when loading saved records from a file, which accepts an existing ID and a pre-parsed `LocalDateTime` instead of generating new ones.

**`Withdrawal.java`**
Child class representing a cash withdrawal. Adds one extra field: `private double amount`. All monetary values are stored as `double` and displayed with exactly 2 decimal places using `String.format("%.2f", amount)`. Implements `getSummary()` to return a withdrawal-specific message and `toFileLine()` to format the record for file storage.

**`Deposit.java`**
Child class representing a cash deposit. Identical structure to `Withdrawal` — adds `private double amount` and provides its own implementations of both abstract methods with deposit-specific output.

**`BalanceInquiry.java`**
Child class representing a balance check. No `amount` field because no money is moved. Implements both abstract methods — `toFileLine()` writes `0` as the amount placeholder so the file format stays consistent across all transaction types.

**`TransactionManager.java`**
The core logic class of the application. Stores all transaction objects in a `List<Transaction>` using `ArrayList`. Provides all four CRUD methods: `addTransaction()` (Create), `printAllTransactions()` (Read), `updateAccountNumber()` (Update), and `deleteTransaction()` (Delete). Also contains `saveToFile()`, `loadFromFile()`, `exportToCSV()`, and `importFromCSV()`. The `getTransactions()` method returns the full list and is used by the GUI and Database bonus classes.

**`Main.java`**
The entry point of the console application. Creates an `Auth` object and calls `login()` before anything else — if login fails after 3 attempts the program exits. Then creates a `TransactionManager`, loads saved data, and enters a `while` loop that displays the menu and reads the user's choice. Each case in the `switch` statement calls a private helper method. The `readPositiveAmount()` helper handles all number input with validation.

**`Auth.java`** *(Bonus 1)*
Implements a simple login system. Stores two users in parallel arrays: usernames, passwords, and roles. The `login()` method gives the user 3 attempts. If credentials match, it stores the username and role internally. The `isAdmin()` method returns `true` or `false` and is checked in `Main.java` before allowing the delete operation.

**`Database.java`** *(Bonus 2)*
Connects to a local SQLite file (`transactions.db`) using JDBC with `DriverManager.getConnection()`. Creates the transactions table automatically on first run with `CREATE TABLE IF NOT EXISTS`. Uses `PreparedStatement` for all INSERT, DELETE, and SELECT queries — this is safer than string concatenation because it prevents SQL injection. The `loadInto()` method reads all rows from the database and uses `addTransactionSilent()` to add them to the manager without printing confirmation messages.

**`ATMGui.java`** *(Bonus 3)*
A graphical user interface built with Java Swing. Extends `JFrame` to create the main window. Uses `BorderLayout` to organize the window into three zones: a title bar at the top (`JLabel`), a scrollable text output area in the center (`JTextArea` inside `JScrollPane`), and a 2×4 grid of buttons at the bottom (`GridLayout`). Each button uses `addActionListener` with a lambda to call `handleButton()`. Input dialogs use `JOptionPane.showInputDialog()`. The window uses a dark navy color scheme.

---

### Algorithms & Data Structures

**ArrayList**
The main data structure of the application. `TransactionManager` stores all transaction objects in `private List<Transaction> transactions = new ArrayList<>()`. ArrayList was chosen because: the number of transactions is not known in advance so a fixed array would not work; adding to the end is fast (O(1) average); removing by object reference is straightforward; iterating with a for-each loop is simple and readable.

**Auto-increment ID**
A `private static int nextId = 1` counter is declared in `Transaction`. Because it is `static`, it belongs to the class itself — not to any individual object. Every time a new transaction is created, `this.id = nextId++` takes the current value and then increments it. When loading from a file, the second constructor checks `if (id >= nextId) { nextId = id + 1; }` to ensure the counter always stays ahead of the highest loaded ID and never produces a duplicate.

**File line format**
Every transaction is stored as one comma-separated line using the format:
```
id,type,accountNumber,amount,dateTime
```
Example lines:
```
1,WITHDRAWAL,ACC001,200.0,2025-01-15 14:30:00
2,DEPOSIT,ACC001,500.0,2025-01-15 14:31:00
3,BALANCE_INQUIRY,ACC001,0,2025-01-15 14:32:00
```
On load, `String.split(",")` breaks each line into a `String[]` array. `parts[0]` is the ID, `parts[1]` is the type, and so on. A switch statement on the type string creates the correct child class object.

**Input validation flow**
All user input follows this pattern: read a string with `scanner.nextLine().trim()`, check if it is empty before proceeding, then for numeric fields call `Double.parseDouble()` inside a try-catch that catches `NumberFormatException`. If parsing fails or the number is zero or negative, the method prints an error message and returns `-1` as a sentinel value. The caller checks `if (amount <= 0) return;` and exits without creating any object.

**SQL with PreparedStatement**
In `Database.java`, all queries use `PreparedStatement` with `?` placeholders instead of building SQL strings with concatenation. The values are set with typed methods: `pstmt.setInt(1, id)`, `pstmt.setString(2, type)`, `pstmt.setDouble(4, amount)`. This separates the SQL structure from the data and prevents SQL injection attacks.

---

### Challenges Faced

**Reconstructing objects from a file**
When saving, each transaction object becomes a plain text line. When loading, the program must rebuild the exact Java object — the right child class with the right field values — from that text. This required adding a second constructor to each child class (`Withdrawal`, `Deposit`, `BalanceInquiry`) that accepts an existing ID and a pre-parsed `LocalDateTime` object. Without this second constructor, loading from file would generate new IDs and new timestamps instead of restoring the originals.

**Keeping the static ID counter consistent across sessions**
The `static int nextId` counter starts at 1 every time the program runs. But when saved records with IDs 1, 2, 3 are loaded from the file, the counter must be updated to 4 — otherwise the next new transaction would get ID 1 again and collide with an existing record. This is solved by checking `if (id >= nextId) { nextId = id + 1; }` inside the file-loading constructor each time a record is read.

**Handling incomplete or corrupted file lines**
If the `transactions.txt` file is manually edited, partially written due to a crash, or missing a field, calling `String.split(",")` might produce an array with fewer elements than expected, and `Integer.parseInt(parts[0])` would throw an exception. Wrapping the entire `parseLineToTransaction()` method body in a try-catch block ensures that any single bad line is skipped with a printed warning while all other lines continue to load normally.

**Mac blocking the SQLite JAR file**
macOS Gatekeeper blocks JAR files downloaded from the internet if the developer is not verified in Apple's system. When the `sqlite-jdbc` JAR is first double-clicked, macOS shows a warning and refuses to open it. The solution is to go to System Settings → Privacy & Security and click the "Open Anyway" button that appears after the first blocked attempt. The JAR itself is safe — this is a macOS security feature, not a virus.

---

## Test Cases and Expected Outputs

| # | Feature | Input | Expected Output |
|---|---------|-------|-----------------|
| 1 | Login — correct admin | `admin` / `admin123` | `Login successful! Welcome, admin [ADMIN]` |
| 2 | Deposit | Account: `ACC001`, Amount: `500` | `Deposited $500.00 into account ACC001` |
| 4 | Withdrawal | Account: `ACC001`, Amount: `200` | `Withdrew $200.00 from account ACC001` |
| 5 | Balance inquiry | Account: `ACC002` | `Balance checked for account ACC002` |
| 6 | View all transactions | Choose option `4` | Full list with ID, type, account, date/time |
| 7 | Empty account number | Press Enter with no text | `Account number cannot be empty.` |
| 8 | Non-numeric amount | Type `abc` | `Invalid amount. Please enter a number.` |
| 9 | Negative amount | Type `-50` | `Amount must be greater than zero.` |
| 10 | Delete — valid ID (admin) | Enter existing ID | `Transaction #N deleted.` |
| 11 | Delete — invalid ID | Enter non-existent ID | `Transaction with ID N not found.` |
| 12 | Delete — user role | Login as `user1`, choose option `5` | `Access denied. Only ADMIN can delete transactions.` |
| 13 | Export CSV | Choose option `6` | `Exported to export.csv` |
| 14 | Import CSV | Choose option `7` | `Imported N new transaction(s) from export.csv` |
| 15 | Load on startup | Restart the program | `Loaded N transaction(s) from file.` |

---

## Files Used for Data Storage

| File | Format | Created by | Contains |
|------|--------|------------|----------|
| `transactions.txt` | Plain text, comma-separated | Auto-created on Exit | All transaction records, one per line |
| `export.csv` | CSV with header row | Option 6 in menu | All transactions in spreadsheet-compatible format |
| `transactions.db` | SQLite binary database | Bonus 2 on first run | All transactions in a relational table |

---

## Bonus Features

| Bonus | File | Description | 
|-------|------|-------------|
| GUI with Swing | `ATMGui.java` | Dark-themed window with 8 buttons, output area, scroll support 
| SQLite Database | `Database.java` | Replaces file storage with a real SQL database via JDBC 
| Login & Roles | `Auth.java` + `Main.java` | Login before use, Admin can delete, User cannot 

---

## Screenshots
Each screenshot includes the system date and time visible on the screen.
1. Main Menu
2. Choosing a role 
3. Adding a deposit
4. Adding withdrawal
5. Checking the balance
6. List of all transactions
7. Deleting a transaction
8. Error - only the admin can delete 
9. CSV export
10. Loading at startup
## Author **Begaim Abdullaeva**
