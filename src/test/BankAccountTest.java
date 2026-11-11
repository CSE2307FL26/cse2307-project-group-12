package test;

import main.BankAccount;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.jupiter.api.Test;

public class BankAccountTest {

    @Test
    public void testDeposit() {
        BankAccount testAccount = new BankAccount();
        testAccount.deposit(50);
        assertEquals(50, testAccount.getBalance(), 0.01);
    }

    @Test
    public void testInvalidDeposit() {
        BankAccount testAccount = new BankAccount();
        try {
            testAccount.deposit(-50);
            fail();
        } catch (IllegalArgumentException e) {
            // do nothing, test passes
        }
    }

    @Test
    public void testWithdraw() {

        BankAccount testAccount = new BankAccount();
        testAccount.deposit(100);
        testAccount.withdraw(50);

        assertEquals(50, testAccount.getBalance(), 0.01);

    }

    @Test
    public void testTooLargeWithdraw() {

        BankAccount testAccount = new BankAccount();
        testAccount.deposit(100);

        try {
            testAccount.withdraw(150); // cannot withdraw more than the balance
            fail();
        } catch (IllegalArgumentException e) {
            // do nothing, test passes
        }

    }

    @Test
    public void testNegativeWithdraw() {

        BankAccount testAccount = new BankAccount();
        testAccount.deposit(100);

        try {
            testAccount.withdraw(-50); // cannot withdraw a negative amount
            fail();
        } catch (IllegalArgumentException e) {
            // do nothing, test passes
        }

    }

    @Test
    public void testWithdrawToZeroBalance() {

        BankAccount testAccount = new BankAccount();
        testAccount.deposit(100);
        testAccount.withdraw(100);

        assertEquals(0, testAccount.getBalance(), 0.01);

    }

    @Test
    public void testTransfer() {
       
        BankAccount account1 = new BankAccount();
        BankAccount account2 = new BankAccount();

        account1.deposit(100);
        account2.deposit(50);

        account1.transfer(account2, 30);

        assertEquals(70, account1.getBalance(), 0.01);
        assertEquals(80, account2.getBalance(), 0.01);
    }

    @Test
    public void testTooLargeTransfer() {
        BankAccount account1 = new BankAccount();
        BankAccount account2 = new BankAccount();

        account1.deposit(100);

        try {
            account1.transfer(account2, 150);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected exception
        }

        assertEquals(100, account1.getBalance(), 0.01);
        assertEquals(0, account2.getBalance(), 0.01);
    }

    @Test
    public void testNegativeTransfer() {
        BankAccount account1 = new BankAccount();
        BankAccount account2 = new BankAccount();

        account1.deposit(100);

        try {
            account1.transfer(account2, -50);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected exception
        }

        assertEquals(100, account1.getBalance(), 0.01);
        assertEquals(0, account2.getBalance(), 0.01);
    }

    @Test
    public void testInvalidTransferAccount() {
        BankAccount account1 = new BankAccount();
        account1.deposit(100);

        try {
            account1.transfer(null, 50);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected exception
        }

        assertEquals(100, account1.getBalance(), 0.01);
    }

@Test
    public void testTransferToSameAccount() {
        BankAccount account1 = new BankAccount();
        account1.deposit(100);

        try {
            account1.transfer(account1, 50);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected exception
        }

        assertEquals(100, account1.getBalance(), 0.01);
    }
    
    @Test
    public void testChargeFeeReducesBalance() {
        BankAccount account = new BankAccount();
        account.deposit(100);

        account.chargeFee(10);

        assertEquals(90, account.getBalance(), 0.01);
    }

    @Test
    public void testChargeFeeRejectsNegativeAmount() {
        BankAccount account = new BankAccount();
        account.deposit(100);

        try {
            account.chargeFee(-10);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected exception.
        }
    }

    @Test
    public void testChargeFeeAllowsNegativeBalance() {
        BankAccount account = new BankAccount();
        account.deposit(5);

        account.chargeFee(10);

        assertEquals(-5, account.getBalance(), 0.01);
    }

    @Test
    public void testChargeFeeRejectsZeroAmount() {
        BankAccount account = new BankAccount();
        account.deposit(100);

        try {
            account.chargeFee(0);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected exception.
        }
    }

    @Test
    public void testNegativeFeeLeavesBalanceUnchanged() {
        BankAccount account = new BankAccount();
        account.deposit(100);

        try {
            account.chargeFee(-10);
        } catch (IllegalArgumentException e) {
            // Negative fee rejection is tested above this test is to check if the balance is unchanged by a call to chargeFee with a negative amount.
        }

        assertEquals(100, account.getBalance(), 0.01);
    }

}
