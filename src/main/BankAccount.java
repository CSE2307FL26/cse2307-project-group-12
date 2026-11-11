package main;

import java.util.ArrayList;
import java.util.List;

public class BankAccount {

    private double balance;
    private List<String> transactionHistory;

    public BankAccount() {
        this.balance = 0;
        this.transactionHistory = new ArrayList<>();
    }

    public void deposit(double amount) {
        if(amount > 0) {
            this.balance += amount;
            recordTransaction("Deposit", amount);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void withdraw(double amount) {
        if(amount > 0 && amount <= this.balance) {
            this.balance -= amount;
            recordTransaction("Withdrawal", amount);
        } else {
            throw new IllegalArgumentException("Invalid withdrawal amount. Withdrawals must be positive and cannot exceed the current balance.");
        }
    }

    public double getBalance() {
        return this.balance;
    }

    // Transfer money to another account
    public void transfer(BankAccount otherAccount, double amount) {
        if (otherAccount == null) {
            throw new IllegalArgumentException("Account does not exist.");
        }

        if (otherAccount == this) {
            throw new IllegalArgumentException("Cannot transfer to the same account.");
        }

        this.withdraw(amount);
        otherAccount.deposit(amount);
    }

    public void chargeFee(double amount) {
        if(amount > 0) {
            this.balance -= amount;
            recordTransaction("Fee", amount);
        } else {
            throw new IllegalArgumentException("Invalid fee amount. Fee must be a positive amount");
        }

    }

    public void interestPayment(double amount) {
        if (amount > 0) {
            this.balance += amount;
            recordTransaction("Interest", amount);
        } else {
            throw new IllegalArgumentException("Interest payment must be positive.");
        }
    }

    // Returns a copy so the history can only be changed through transactions on the account
    public List<String> getTransactionHistory() {
        return new ArrayList<>(this.transactionHistory);
    }

    private void recordTransaction(String type, double amount) {
        this.transactionHistory.add(String.format("%s: %.2f | Balance: %.2f", type, amount, this.balance));
    }
}