package org.example.domain;

public class Usuario {
    private final String email;
    private final String passwordHash; // nunca password en claro
    private final Autoridad autoridad;

    public Usuario(String email, String passwordHash, Autoridad autoridad) {
        if (email == null || email.isBlank() || !email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$"))
            throw new IllegalArgumentException("Email inválido");
        if (passwordHash == null || passwordHash.isBlank())
            throw new IllegalArgumentException("Hash de contraseña obligatorio");
        if (autoridad == null)
            throw new IllegalArgumentException("Autoridad obligatoria");

        this.email = email;
        this.passwordHash = passwordHash;
        this.autoridad = autoridad;
    }

    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public Autoridad getAutoridad() { return autoridad; }
}
