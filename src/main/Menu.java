package main;

import java.util.List;
import java.util.Scanner;

public abstract class Menu {
    public enum MenuResult {
        BACK,
        EXIT
    }

    
    protected final Bank bank;
    protected final Scanner keyboardInput;

    protected Menu(Bank bank, Scanner keyboardInput) {
        this.bank = bank;
        this.keyboardInput = keyboardInput;
    }

    public abstract MenuResult run();

    protected int getUserSelection(int max) {
        return getUserSelection(keyboardInput, max);
    }

    protected static int getUserSelection(Scanner keyboardInput, int max) {
        int selection = -1;
        while (selection < 1 || selection > max) {
            System.out.print("Please make a selection: ");
            if (keyboardInput.hasNextInt()) {
                selection = keyboardInput.nextInt();
            } else {
                keyboardInput.next();
                System.out.println("Please enter a whole number.");
            }
        }
        return selection;
    }

    protected boolean hasNumericAmount(String action) {
        System.out.print("Please enter the amount to " + action + ": ");
        if (keyboardInput.hasNextDouble()) {
            return true;
        }
        keyboardInput.next();
        System.out.println("Invalid amount. Expected a number.");
        return false;
    }

    protected String selectAccountId(String excludedAccountId) {
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
        int cancelOption = accountIds.size() + 1;
        System.out.println(cancelOption + ". Cancel");
        int choice = getUserSelection(cancelOption);
        if (choice == cancelOption) {
            return null;
        }
        return accountIds.get(choice - 1);
    }
}
