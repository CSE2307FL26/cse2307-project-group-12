
package test;

import main.Customer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;

public class CustomerTest {

    @Test
    public void testGetUsername() {
        Customer customer = new Customer("owen", "password123");

        assertEquals("owen", customer.getUsername());
    }

    @Test
    public void testCorrectPassword() {
        Customer customer = new Customer("owen", "password123");

        assertTrue(customer.checkPassword("password123"));
    }

    @Test
    public void testIncorrectPassword() {
        Customer customer = new Customer("owen", "password123");

        assertFalse(customer.checkPassword("wrongPassword"));
    }

    @Test
    public void testNullPasswordDoesNotMatch() {
        Customer customer = new Customer("owen", "password123");

        assertFalse(customer.checkPassword(null));
    }
}
