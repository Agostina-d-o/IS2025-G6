package org.example.infra;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import org.example.app.interfaces.PasswordHasher;

public class Argon2idPasswordHasher implements PasswordHasher {

    private final Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);

    // Parámetros base
    private static final int MEMORY_KB   = 64 * 1024;
    private static final int ITERATIONS  = 3;
    private static final int PARALLELISM = 1;

    @Override
    public String hash(String rawPassword) {
        if (rawPassword == null) throw new IllegalArgumentException("Password obligatoria");
        return argon2.hash(ITERATIONS, MEMORY_KB, PARALLELISM, rawPassword);
    }

    @Override
    public boolean verify(String rawPassword, String hash) {
        return argon2.verify(hash, rawPassword);
    }
}
