package payrollSystem;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

// Passwords are never saved as normal text. We save: SHA-256( salt + password ) written in hex.
// The salt is a random text, so two users with the same password get different hashes.
public class PasswordUtil {

    private PasswordUtil() { }

    // makes a random salt: 16 random bytes written as 32 hex characters
    public static String newSalt() {
        byte[] bytes = new byte[16];
        new SecureRandom().nextBytes(bytes);
        return toHex(bytes);
    }

    // turns salt + password into the hash text that is saved in the database
    public static String hash(String salt, String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            return toHex(md.digest((salt + password).getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("SHA-256 is not available", ex);
        }
    }

    // checks a typed password against the saved salt and hash
    public static boolean matches(String password, String salt, String savedHash) {
        return hash(salt, password).equalsIgnoreCase(savedHash);
    }

    // helper: bytes to hex text, e.g. 255 becomes "ff"
    private static String toHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
