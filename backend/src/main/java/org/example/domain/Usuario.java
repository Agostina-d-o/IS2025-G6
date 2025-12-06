package org.example.domain;

public class Usuario {
    private final String email;
    private final String passwordHash;
    private final Autoridad autoridad;
    private final String nombre;
    private final String apellido;

    public Usuario(String email, String passwordHash, Autoridad autoridad, String nombre, String apellido) {
        if (email == null || email.isBlank() || !email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$"))
            throw new IllegalArgumentException("Email inválido");
        if (passwordHash == null || passwordHash.isBlank())
            throw new IllegalArgumentException("Hash de contraseña obligatorio");
        if (autoridad == null)
            throw new IllegalArgumentException("Autoridad obligatoria");
        if (nombre == null || nombre.isBlank())
            throw new IllegalArgumentException("Nombre obligatorio");
        if (apellido == null || apellido.isBlank())
            throw new IllegalArgumentException("Apellido obligatorio");

        this.email = email;
        this.passwordHash = passwordHash;
        this.autoridad = autoridad;
        this.nombre = nombre;
        this.apellido = apellido;
    }

    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public Autoridad getAutoridad() { return autoridad; }
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
}
