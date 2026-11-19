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
        while (true) {
            System.out.println("1. Customer");
            System.out.println("2. Administrator");
            System.out.println("3. Exit the app");
            int selection = Menu.getUserSelection(keyboardInput, 3);
            if (selection == 3) {
                return;
            }
            Menu menu;
            if (selection == 1) {
                menu = new ClientMenu(bank, keyboardInput);
            } else {
                menu = new AdminMenu(bank, keyboardInput);
            }
            if (menu.run() == Menu.MenuResult.EXIT) {
                return;
            }
        }
    }

    public static void main(String[] args) {
        MainMenu bankApp = new MainMenu();
        bankApp.run();
    }
}
