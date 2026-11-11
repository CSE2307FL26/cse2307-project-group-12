package test;

import main.BankAccount;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

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
    @Test
    public void testNewAccountHasEmptyHistory() {
        BankAccount account = new BankAccount();

        assertTrue(account.getTransactionHistory().isEmpty());
    }

    @Test
    public void testDepositIsRecordedInHistory() {
        BankAccount account = new BankAccount();
        account.deposit(50);

        List<String> history = account.getTransactionHistory();

        assertEquals(1, history.size());
        assertEquals("Deposit: 50.00 | Balance: 50.00", history.get(0));
    }

    @Test
    public void testHistoryRecordsTransactionsInOrder() {
        BankAccount account = new BankAccount();
        account.deposit(100);
        account.withdraw(30);
        account.chargeFee(5);

        List<String> history = account.getTransactionHistory();

        assertEquals(3, history.size());
        assertEquals("Deposit: 100.00 | Balance: 100.00", history.get(0));
        assertEquals("Withdrawal: 30.00 | Balance: 70.00", history.get(1));
        assertEquals("Fee: 5.00 | Balance: 65.00", history.get(2));
    }

    @Test
    public void testInvalidTransactionsAreNotRecorded() {
        BankAccount account = new BankAccount();
        account.deposit(100);

        try {
            account.withdraw(500);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected exception.
        }

        try {
            account.deposit(-20);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected exception.
        }

        assertEquals(1, account.getTransactionHistory().size());
    }

    @Test
    public void testHistoryCannotBeModifiedFromOutside() {
        BankAccount account = new BankAccount();
        account.deposit(100);

        account.getTransactionHistory().clear();

        assertEquals(1, account.getTransactionHistory().size());
    }
}
