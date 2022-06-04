package ots.controllers;

import ots.database.DatabaseController;
import ots.models.User;

import java.security.MessageDigest;
import java.util.Optional;

import static java.nio.charset.StandardCharsets.UTF_8;

public class LoginController {
    private final DatabaseController databaseController;
    private final MessageDigest digest;

    public LoginController(DatabaseController databaseController) {
        this.databaseController = databaseController;

        try {
            this.digest = MessageDigest.getInstance("SHA-256");
        } catch (Exception exception) {
            throw new RuntimeException("Unable to get SHA-256 message digest instance", exception);
        }
    }

    public Optional<User> login(String email, String password) {
        User user = databaseController.getUser(email);
        if (user == null) {
            System.out.println("A user, with the email " + email + ", does not exist");
            return Optional.empty();
        }

        String hashedPassword = hashPassword(password);

        if (!user.getPassword().equals(hashedPassword)) {
            System.out.println("The entered password is incorrect");
            return Optional.empty();
        }

        return Optional.of(user);
    }

    private String hashPassword(String password) {
        byte[] bytes = digest.digest(password.getBytes(UTF_8));
        final StringBuilder stringBuilder = new StringBuilder(bytes.length);

        for (byte hashByte : bytes) {
            stringBuilder.append(Integer.toHexString(255 & hashByte));
        }

        return stringBuilder.toString();
    }
}
