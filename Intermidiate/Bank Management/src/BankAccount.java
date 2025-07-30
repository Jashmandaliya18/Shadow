import java.util.ArrayList;
import java.util.List;

public class BankAccount {
    private String accountHolderName;
    private double balance;
    private List<String> transactionHistory;

    public BankAccount(String accountHolderName, double initialBalance) {
        this.accountHolderName = accountHolderName;
        this.balance = initialBalance;
        this.transactionHistory = new ArrayList<>();
        transactionHistory.add("Account opened with balance: " + initialBalance);
    }

    public void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive.");
        }
        balance += amount;
        transactionHistory.add("Deposited: " + amount);
    }

    public void withdraw(double amount) {
        if (amount > balance) {
            transactionHistory.add("Failed withdrawal attempt: " + amount);
            throw new IllegalArgumentException("Insufficient funds.");
        }
        balance -= amount;
        transactionHistory.add("Withdrew: " + amount);
    }

    public double getBalance() {
        return balance;
    }

    public String getOwnerName() {
        return accountHolderName;
    }

    public List<String> getTransactionHistory() {
        return transactionHistory;
    }
}
