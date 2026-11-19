package main;

import java.util.Scanner;

public class AdminMenu extends Menu {
    
    public AdminMenu(Bank bank, Scanner keyboardInput) {
        super(bank, keyboardInput);
    }

    public void displayOptions() {
        System.out.println("1. Collect a fee");
        System.out.println("2. Add an interest payment");

        System.out.println("3. Exit the app");
    }

    public void processInput(int selection) {
        switch (selection) {
            case 1:
                performChargeFee();
                break;
            case 2:
                performInterestPayment();
                break;
        }
    }

    private BankAccount selectAccount() {
        String accountId = selectAccountId(null);
        return accountId == null ? null : bank.getAccount(accountId);
    }

    public void performChargeFee() {
        BankAccount account = selectAccount();
        if (account == null) {
            return;
        }
        if (!hasNumericAmount("charge as a fee")) {
            return;
        }
        try {
            double fee = keyboardInput.nextDouble();
            account.chargeFee(fee);
            System.out.printf("New balance: %.2f%n", account.getBalance());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    public void performInterestPayment() {
        BankAccount account = selectAccount();
        if (account == null) {
            return;
        }
        if (!hasNumericAmount("pay as interest")) {
            return;
        }
        try {
            double amount = keyboardInput.nextDouble();
            account.interestPayment(amount);
            System.out.printf("New balance: %.2f%n", account.getBalance());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public MenuResult run() {
        int selection = -1;
        while (selection != 3) {
            displayOptions();
            selection = getUserSelection(3);
            processInput(selection);
        }
        return MenuResult.EXIT;
    }
}
