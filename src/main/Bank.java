package main;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Bank {

    private Map<String, BankAccount> bankAccounts;
    private int nextAccountNumber;

    public Bank() {
        this.bankAccounts = new LinkedHashMap<>();
        this.nextAccountNumber = 1;
    }

    public void addAccount(String accountId, BankAccount account) {
        if (accountId == null || accountId.isEmpty()) {
            throw new IllegalArgumentException("Account ID cannot be empty.");
        }

        if (account == null) {
            throw new IllegalArgumentException("Account cannot be null.");
        }

        if (bankAccounts.containsKey(accountId)) {
            throw new IllegalArgumentException("Account ID already exists.");
        }

        bankAccounts.put(accountId, account);
    }

    // Opens a new empty account and returns its generated ID (account1, account2, ...)
    public String createAccount() {
        String accountId = "account" + nextAccountNumber;
        while (bankAccounts.containsKey(accountId)) {
            nextAccountNumber++;
            accountId = "account" + nextAccountNumber;
        }
        nextAccountNumber++;

        addAccount(accountId, new BankAccount());
        return accountId;
    }

    public BankAccount getAccount(String accountId) {
        BankAccount account = bankAccounts.get(accountId);

        if (account == null) {
            throw new IllegalArgumentException("Account not found.");
        }

        return account;
    }

    public List<String> getAccountIds() {
        return new ArrayList<>(bankAccounts.keySet());
    }

    public void closeAccount(String accountId) {
        BankAccount account = getAccount(accountId);

        if (account.getBalance() != 0) {
            throw new IllegalArgumentException("Account must have a zero balance to close.");
        }

        bankAccounts.remove(accountId);
    }
}