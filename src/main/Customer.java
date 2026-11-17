
package main;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class Customer {

    private final String username;
    private byte[] passwordHash;
    private byte[] salt;

    public Customer(String username, String password) {
        if (username == null || username.trim().isEmpty() || username.contains(" ")) {
            throw new IllegalArgumentException("Username cannot be empty or contain spaces.");
        }

        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }

        this.username = username;
        this.salt = new byte[16];
        new SecureRandom().nextBytes(salt);
        this.passwordHash = hashPassword(password);
    }

    public String getUsername() {
        return username;
    }

    public boolean checkPassword(String password) {
        if (password == null) {
            return false;
        }

        byte[] enteredHash = hashPassword(password);
        return MessageDigest.isEqual(passwordHash, enteredHash);
    }

    private byte[] hashPassword(String password) {
        char[] characters = password.toCharArray();

        PBEKeySpec spec = new PBEKeySpec(characters, salt, 120000, 256);

        try {
            SecretKeyFactory factory =
                    SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

            return factory.generateSecret(spec).getEncoded();

        } catch (Exception e) {
            throw new IllegalStateException("Could not hash password.", e);

        } finally {
            spec.clearPassword();
            Arrays.fill(characters, '\0');
        }
    }
}
