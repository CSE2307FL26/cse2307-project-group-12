package test;

import main.BankAccount;
import main.Bank;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertSame;

import org.junit.jupiter.api.Test;

public class BankTest {

    @Test
    public void testGetAccountReturnsCorrectAccount() {
        Bank bank = new Bank();
        BankAccount expectedAccount = new BankAccount();
        bank.addAccount("12345", expectedAccount);

        BankAccount actualAccount = bank.getAccount("12345");

        assertSame(expectedAccount, actualAccount);
    }

    @Test
    public void testGetAccountRejectsInvalidId() {
        Bank bank = new Bank();

        try {
            bank.getAccount("12345");
            fail();
        } catch (IllegalArgumentException e) {
            // Expected exception.
        }
    }
    
    @Test
    public void testAddAccountRejectsDuplicateId() {
        Bank bank = new Bank();
        bank.addAccount("12345", new BankAccount());

        try {
            bank.addAccount("12345", new BankAccount());
            fail();
        } catch (IllegalArgumentException e) {
            // Expected exception.
        }

    }

    @Test
    public void testAddAccountRejectsEmptyId() {
        Bank bank = new Bank();

        try {
            bank.addAccount("", new BankAccount());
            fail();
        } catch (IllegalArgumentException e) {
            // Expected exception.
        }
    }

    @Test
    public void testAddAccountRejectsNullId() {
        Bank bank = new Bank();

        try {
            bank.addAccount(null, new BankAccount());
            fail();
        } catch (IllegalArgumentException e) {
            // Expected exception.
        }
    }

    @Test
    public void testAddAccountRejectsNullAccount() {
        Bank bank = new Bank();

        try {
            bank.addAccount("12345", null);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected exception.
        }
    }

    @Test
    public void testDuplicateIdKeepsOriginalAccount() {
        Bank bank = new Bank();
        BankAccount originalAccount = new BankAccount();
        bank.addAccount("12345", originalAccount);

        try {
            bank.addAccount("12345", new BankAccount());
        } catch (IllegalArgumentException e) {
            // Goal of test is check that the original account is still present, so we can ignore the exception.
        }

        assertSame(originalAccount, bank.getAccount("12345"));
    }
}
