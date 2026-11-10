package main;

import java.util.List;
import java.util.Scanner;

public class MainMenu {

    private Bank bank;
    private BankAccount userAccount;
    private Scanner keyboardInput;
    private boolean isAdmin;

    public MainMenu() {
        this.bank = new Bank();
        this.userAccount = new BankAccount();
        this.bank.addAccount("account1", this.userAccount);
        this.keyboardInput = new Scanner(System.in);
    }

    public void displayOptions() {

        if (isAdmin) {
            System.out.println("1. Collect a fee");
            System.out.println("2. Exit the app");
        } else {
            System.out.println("1. Make a deposit");
            System.out.println("2. Make a withdrawal");
            System.out.println("3. Exit the app");
        }

    }

    public int getUserSelection(int max) {
        int selection = -1;
        while (selection < 1 || selection > max) {
            System.out.print("Please make a selection: ");
            selection = keyboardInput.nextInt();
        }
        return selection;
    }

    public void processInput(int selection) {
        if (isAdmin) {
            switch (selection) {
                case 1:
                    performChargeFee();
                    break;
            }
        } else {
            switch (selection) {
                case 1:
                    performDeposit();
                    break;
                case 2:
                    performWithdrawal();
                    break;
            }
        }
    }

    public void performDeposit() {
        double depositAmount = -1;
        while (depositAmount <= 0) {
            System.out.print("How much would you like to deposit: ");
            depositAmount = keyboardInput.nextDouble();
        }
        userAccount.deposit(depositAmount);
    }

    public void performWithdrawal() {
        double withdrawAmount = -1;
        while (withdrawAmount <= 0) {
            System.out.print("How much would you like to withdraw: ");
            withdrawAmount = keyboardInput.nextDouble();
        }
        userAccount.withdraw(withdrawAmount);
    }

    private BankAccount selectAccount() {
        List<String> accountIds = bank.getAccountIds();
        if (accountIds.isEmpty()) {
            System.out.println("There are no accounts available.");
            return null;
        }

        System.out.println("Select an account:");
        for (int i = 0; i < accountIds.size(); i++) {
            System.out.println((i + 1) + ". " + accountIds.get(i));
        }
        int choice = getUserSelection(accountIds.size());
        String accountId = accountIds.get(choice - 1);
        return bank.getAccount(accountId);
    }

    public void performChargeFee() {
        BankAccount account = selectAccount();
        if (account == null) {
            return;
        }
        System.out.print("Enter the fee amount: ");
        if (!keyboardInput.hasNextDouble()) {
            keyboardInput.next();
            System.out.println("Please enter a number.");
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

    public void run() {
        System.out.println("Welcome to the 237 Bank App!");
        System.out.println("1. Client");
        System.out.println("2. Administrator");
        this.isAdmin = getUserSelection(2) == 2;

        int exitSelection = isAdmin ? 2 : 3;
        int selection = -1;
        while (selection != exitSelection) {
            displayOptions();
            selection = getUserSelection(exitSelection);
            processInput(selection);
        }
    }

    public static void main(String[] args) {
        MainMenu bankApp = new MainMenu();
        bankApp.run();
    }

}
