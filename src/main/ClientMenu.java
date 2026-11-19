package main;

import java.util.List;
import java.util.Scanner;

public class ClientMenu extends Menu {
    private String selectedAccountId;

    public ClientMenu(Bank bank, Scanner keyboardInput) {
        super(bank, keyboardInput);
    }

    public void performRegistration() {
        // Clear the newline left by the menu selection.
        keyboardInput.nextLine();

        String username = "";

        while (username.isEmpty()) {
            System.out.print("Enter a username: ");
            String enteredUsername = keyboardInput.nextLine();

            if (enteredUsername.trim().isEmpty()) {
                System.out.println("Username cannot be empty.");
            } else if (enteredUsername.contains(" ")) {
                System.out.println("Username cannot contain spaces.");
            } else {
                try {
                    bank.getCustomer(enteredUsername);
                    System.out.println("Username already exists. Try another.");
                } catch (IllegalArgumentException e) {
                    username = enteredUsername;
                }
            }
        }

        String password = "";

        while (password.isEmpty()) {
            System.out.print("Enter a password: ");
            if (System.console() != null && System.console().isTerminal()) {
                password = new String(System.console().readPassword());
            } else {
                password = keyboardInput.nextLine();
            }

            if (password.isEmpty()) {
                System.out.println("Password cannot be empty.");
            }
        }

        bank.registerCustomer(username, password);
        System.out.println("Registration successful!");
    }

    public void displayOptions() {
        System.out.println("Account: " + selectedAccountId);
        System.out.println("1. Make a deposit");
        System.out.println("2. Make a withdrawal");
        System.out.println("3. Close this account");
        System.out.println("4. Transfer money");
        System.out.println("5. View transaction history");
        System.out.println("6. Check your balance");
        System.out.println("7. Open a new account");
        System.out.println("8. Choose another account");
        System.out.println("9. Exit the app");
    }

    public void processInput(int selection) {
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
                performViewTransactionHistory();
                break;
            case 6:
                performCheckBalance();
                break;
            case 7:
                performCreateAccount();
                break;
            case 8:
                selectedAccountId = null;
                break;
        }
    }

    public void performCheckBalance() {
        BankAccount account = bank.getAccount(selectedAccountId);
        System.out.printf("Current balance: %.2f%n", account.getBalance());
    }

    public void performDeposit() {
        double depositAmount = -1;
        while (depositAmount <= 0) {
            if (!hasNumericAmount("deposit")) {
                return;
            }
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
            if (!hasNumericAmount("withdraw")) {
                return;
            }
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
        if (!hasNumericAmount("transfer")) {
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

    public void performCloseAccount() {
        try {
            bank.closeAccount(selectedAccountId);
            selectedAccountId = null;
            System.out.println("Account closed successfully!");
        } catch (IllegalArgumentException e) {
            System.out.println("Could not close account: " + e.getMessage());
        }
    }

    public void performViewTransactionHistory() {
        try {
            List<String> history = bank.getAccount(selectedAccountId).getTransactionHistory();
            if (history.isEmpty()) {
                System.out.println("No transactions yet for " + selectedAccountId + ".");
                return;
            }
            System.out.println("Transaction history for " + selectedAccountId + ":");
            for (int i = 0; i < history.size(); i++) {
                System.out.println((i + 1) + ". " + history.get(i));
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Could not load transaction history: " + e.getMessage());
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

    @Override
    public MenuResult run() {
        int selection = 0;
        while (selection != 3) {
            System.out.println("1. Register");
            System.out.println("2. Continue as a customer");
            System.out.println("3. Back");
            selection = getUserSelection(3);
            if (selection == 1) {
                performRegistration();
            } else if (selection == 2) {
                runBankingMenu();
                return MenuResult.EXIT;
            }
        }
        return MenuResult.BACK;
    }

    private void runBankingMenu() {
        int selection = -1;
        while (selection != 9) {
            if (selectedAccountId == null) {
                selectedAccountId = selectOrCreateAccountId();
                if (selectedAccountId == null) {
                    return;
                }
            }
            displayOptions();
            selection = getUserSelection(9);
            processInput(selection);
        }
    }
}
