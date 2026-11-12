package main;

import java.util.List;
import java.util.Scanner;

public class MainMenu {

    private Bank bank;
    private String selectedAccountId;
    private Scanner keyboardInput;
    private boolean isAdmin;

    public MainMenu() {
        this.bank = new Bank();
        this.bank.createAccount();
        this.keyboardInput = new Scanner(System.in);
    }

    public void displayOptions() {

        if (isAdmin) {
            System.out.println("1. Collect a fee");
            System.out.println("2. Add an interest payment");
            System.out.println("3. Exit the app");
        } else {
            System.out.println("Account: " + selectedAccountId);
            System.out.println("1. Make a deposit");
            System.out.println("2. Make a withdrawal");
            System.out.println("3. Close this account");
            System.out.println("4. Transfer money");
            System.out.println("5. Open a new account");
            System.out.println("6. Choose another account");
            System.out.println("7. Exit the app");
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
                case 2:
                    performInterestPayment();
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
                case 3:
                    performCloseAccount();
                    break;
                case 4:
                    performTransfer();
                    break;
                case 5:
                    performCreateAccount();
                    break;
                case 6:
                    selectedAccountId = null;
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
        try {
            BankAccount account = bank.getAccount(selectedAccountId);
            account.deposit(depositAmount);
            System.out.printf("New balance: %.2f%n", account.getBalance());
        } catch (IllegalArgumentException e) {
            System.out.println("Deposit failed: " + e.getMessage());
        }
    }

    public void performWithdrawal() {
        double withdrawAmount = -1;
        while (withdrawAmount <= 0) {
            System.out.print("How much would you like to withdraw: ");
            withdrawAmount = keyboardInput.nextDouble();
        }
        try {
            BankAccount account = bank.getAccount(selectedAccountId);
            account.withdraw(withdrawAmount);
            System.out.printf("New balance: %.2f%n", account.getBalance());
        } catch (IllegalArgumentException e) {
            System.out.println("Withdrawal failed: " + e.getMessage());
        }
    }

    public void performTransfer() {
        String destinationId = selectAccountId(selectedAccountId);
        if (destinationId == null) {
            return;
        }
        System.out.print("How much would you like to transfer: ");
        if (!keyboardInput.hasNextDouble()) {
            keyboardInput.next();
            System.out.println("Please enter a number.");
            return;
        }
        try {
            BankAccount source = bank.getAccount(selectedAccountId);
            source.transfer(bank.getAccount(destinationId), keyboardInput.nextDouble());
            System.out.printf("Transfer successful! New balance: %.2f%n", source.getBalance());
        } catch (IllegalArgumentException e) {
            System.out.println("Transfer failed: " + e.getMessage());
        }
    }

    private String selectAccountId(String excludedAccountId) {
        List<String> accountIds = bank.getAccountIds();
        String prompt = "Select an account:";
        String emptyMessage = "There are no accounts available.";
        if (excludedAccountId != null) {
            accountIds.remove(excludedAccountId);
            prompt = "Select the account to transfer to:";
            emptyMessage = "You need another account to transfer to.";
        }
        if (accountIds.isEmpty()) {
            System.out.println(emptyMessage);
            return null;
        }

        System.out.println(prompt);
        for (int i = 0; i < accountIds.size(); i++) {
            System.out.println((i + 1) + ". " + accountIds.get(i));
        }
        int choice = getUserSelection(accountIds.size());
        return accountIds.get(choice - 1);
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

    public void performInterestPayment() {
        BankAccount account = selectAccount();
        if (account == null) {
            return;
        }
        System.out.print("Enter the interest payment amount: ");
        if (!keyboardInput.hasNextDouble()) {
            keyboardInput.next();
            System.out.println("Please enter a number.");
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

    public void performCloseAccount() {
        try {
            bank.closeAccount(selectedAccountId);
            selectedAccountId = null;
            System.out.println("Account closed successfully!");
        } catch (IllegalArgumentException e) {
            System.out.println("Could not close account: " + e.getMessage());
        }
    }

    public void performCreateAccount() {
        try {
            String newAccountId = bank.createAccount();
            System.out.println("New account created: " + newAccountId);
            System.out.println("Use \"Choose another account\" to switch to it.");
        } catch (IllegalArgumentException e) {
            System.out.println("Could not create account: " + e.getMessage());
        }
    }

    private String selectOrCreateAccountId() {
        String accountId = selectAccountId(null);
        if (accountId != null) {
            return accountId;
        }
        System.out.println("1. Open a new account");
        System.out.println("2. Exit the app");
        if (getUserSelection(2) == 1) {
            return bank.createAccount();
        }
        return null;
    }

    public void run() {
        System.out.println("Welcome to the 237 Bank App!");
        System.out.println("1. Client");
        System.out.println("2. Administrator");
        this.isAdmin = getUserSelection(2) == 2;

        int exitSelection = isAdmin ? 3 : 7;
        int selection = -1;
        while (selection != exitSelection) {
            if (!isAdmin && selectedAccountId == null) {
                selectedAccountId = selectOrCreateAccountId();
                if (selectedAccountId == null) {
                    return;
                }
            }
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
