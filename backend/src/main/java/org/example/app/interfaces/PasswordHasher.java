package org.example.app.interfaces;

public interface PasswordHasher {
    String hash(String rawPassword);
    boolean verify(String rawPassword, String hash);
}
