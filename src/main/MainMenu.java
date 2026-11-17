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
        System.out.println("1. Client");
        System.out.println("2. Administrator");
        int selection = Menu.getUserSelection(keyboardInput, 2);

        Menu menu;
        if (selection == 2) {
            menu = new AdminMenu(bank, keyboardInput);
        } else {
            menu = new ClientMenu(bank, keyboardInput);
        }
        menu.run();
    }

    public static void main(String[] args) {
        MainMenu bankApp = new MainMenu();
        bankApp.run();
    }
}
