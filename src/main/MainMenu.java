package main;

import java.util.Scanner;

public class MainMenu {
    private final Bank bank;
    private final Scanner keyboardInput;

    public MainMenu() {
        bank = new Bank();
        bank.createAccount();
        keyboardInput = new Scanner(System.in);
    }

    public void run() {
        System.out.println("Welcome to the 237 Bank App!");
        ClientMenu clientMenu = new ClientMenu(bank, keyboardInput);
        int startingSelection = 0;

        while (startingSelection != 4) {
            System.out.println("1. Register as a new customer");
            System.out.println("2. Continue as a customer");
            System.out.println("3. Administrator");
            System.out.println("4. Exit the app");

            startingSelection = Menu.getUserSelection(keyboardInput, 4);

            if (startingSelection == 1) {
                clientMenu.performRegistration();
            } else if (startingSelection == 2) {
                clientMenu.run();
                return;
            } else if (startingSelection == 3) {
                new AdminMenu(bank, keyboardInput).run();
                return;
            }
        }
    }

    public static void main(String[] args) {
        MainMenu bankApp = new MainMenu();
        bankApp.run();
    }
}
