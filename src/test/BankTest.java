package test;

import main.BankAccount;
import main.Bank;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

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

    @Test
    public void testCloseAccount() {
        Bank bank = new Bank();
        bank.addAccount("12345", new BankAccount());

        bank.closeAccount("12345");

        try {
            bank.getAccount("12345");
            fail();
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testCloseNonexistentAccount() {
        Bank bank = new Bank();

        try {
            bank.closeAccount("12345");
            fail();
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testCloseAccountWithBalance() {
        Bank bank = new Bank();
        BankAccount account = new BankAccount();

        account.deposit(100);
        bank.addAccount("12345", account);

        try {
            bank.closeAccount("12345");
            fail();
        } catch (IllegalArgumentException e) {
            // Expected exception
        }

        assertEquals(100, bank.getAccount("12345").getBalance(), 0.01);
    }
        @Test
    public void testCreateAccountAddsAccountToBank() {
        Bank bank = new Bank();

        String accountId = bank.createAccount();

        assertTrue(bank.getAccountIds().contains(accountId));
        assertEquals(0, bank.getAccount(accountId).getBalance(), 0.01);
    }

    @Test
    public void testCreateAccountGivesUniqueIds() {
        Bank bank = new Bank();

        String firstId = bank.createAccount();
        String secondId = bank.createAccount();

        assertNotEquals(firstId, secondId);
        assertEquals(2, bank.getAccountIds().size());
    }

    @Test
    public void testCreateAccountSkipsExistingId() {
        Bank bank = new Bank();
        BankAccount existingAccount = new BankAccount();
        bank.addAccount("account1", existingAccount);

        String newId = bank.createAccount();

        assertNotEquals("account1", newId);
        assertSame(existingAccount, bank.getAccount("account1"));
    }

    @Test
    public void testCreateAccountDoesNotReuseClosedId() {
        Bank bank = new Bank();
        String firstId = bank.createAccount();
        bank.closeAccount(firstId);

        String secondId = bank.createAccount();

        assertNotEquals(firstId, secondId);
    }

    @Test
    public void testTransferHistoryIdentifiesTheOtherAccount() {
        Bank bank = new Bank();
        String sourceId = bank.createAccount();
        String destinationId = bank.createAccount();
        BankAccount source = bank.getAccount(sourceId);
        BankAccount destination = bank.getAccount(destinationId);
        source.deposit(100);

        source.transfer(destination, 40);

        assertEquals("Transfer to " + destinationId + ": 40.00 | Balance: 60.00", source.getTransactionHistory().get(1));
        assertEquals("Transfer from " + sourceId + ": 40.00 | Balance: 40.00", destination.getTransactionHistory().get(0));
    }
}
