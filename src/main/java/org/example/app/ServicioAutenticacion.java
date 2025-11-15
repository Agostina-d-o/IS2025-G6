package org.example.app;

import org.example.app.interfaces.PasswordHasher;
import org.example.app.interfaces.RepositorioUsuarios;
import org.example.domain.Autoridad;
import org.example.domain.Usuario;

public class ServicioAutenticacion {

    private final RepositorioUsuarios repoUsuarios;
    private final PasswordHasher hasher;

    public ServicioAutenticacion(RepositorioUsuarios repoUsuarios, PasswordHasher hasher) {
        this.repoUsuarios = repoUsuarios;
        this.hasher = hasher;
    }

    // Registro
    public Usuario registrar(String email, String passwordPlano, Autoridad autoridad) {
        if (passwordPlano == null || passwordPlano.length() < 8)
            throw new IllegalArgumentException("La contraseña debe tener al menos 8 caracteres");

        if (repoUsuarios.buscarPorEmail(email).isPresent())
            throw new IllegalArgumentException("El email ya está registrado");

        String hash = hasher.hash(passwordPlano);
        Usuario u = new Usuario(email, hash, autoridad);
        repoUsuarios.guardar(u);
        return u;
    }

    // Login (mensaje único por seguridad)
    public Usuario login(String email, String passwordPlano) {
        final String MSG = "Usuario o contraseña inválidos";
        return repoUsuarios.buscarPorEmail(email)
                .filter(u -> hasher.verify(passwordPlano, u.getPasswordHash()))
                .orElseThrow(() -> new RuntimeException(MSG));
    }
}
