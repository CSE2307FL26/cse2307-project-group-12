package main;

public class BankAccount {

    private double balance;

    public BankAccount() {
        this.balance = 0;
    }

    public void deposit(double amount) {
        if(amount > 0) {
            this.balance += amount;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void withdraw(double amount) {
        if(amount > 0 && amount <= this.balance) {
            this.balance -= amount;
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
        } else {
            throw new IllegalArgumentException("Invalid fee amount. Fee must be a positive amount");
        }

    }

    public void interestPayment(double amount) {
        if (amount > 0) {
            this.balance += amount;
        } else {
            throw new IllegalArgumentException("Interest payment must be positive.");
        }
    }
}
