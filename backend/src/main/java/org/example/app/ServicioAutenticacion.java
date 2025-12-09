package org.example.app;

import org.example.app.interfaces.PasswordHasher;
import org.example.app.interfaces.RepositorioUsuarios;
import org.example.app.interfaces.RepositorioMedicos;
import org.example.app.interfaces.RepositorioEnfermeras;
import org.example.domain.Autoridad;
import org.example.domain.Usuario;
import org.example.domain.Medico;
import org.example.domain.Enfermera;
import org.springframework.stereotype.Service;

@Service
public class ServicioAutenticacion {

    private final RepositorioUsuarios repoUsuarios;
    private final PasswordHasher hasher;
    private final RepositorioMedicos repoMedicos;
    private final RepositorioEnfermeras repoEnfermeras;

    public ServicioAutenticacion(
            RepositorioUsuarios repoUsuarios,
            PasswordHasher hasher,
            RepositorioMedicos repoMedicos,
            RepositorioEnfermeras repoEnfermeras
    ) {
        this.repoUsuarios = repoUsuarios;
        this.hasher = hasher;
        this.repoMedicos = repoMedicos;
        this.repoEnfermeras = repoEnfermeras;
    }

    // Registro
    public Usuario registrar(String email, String passwordPlano, Autoridad autoridad,
                             String nombre, String apellido, String matricula) {

        if (passwordPlano == null || passwordPlano.length() < 8)
            throw new IllegalArgumentException("La contraseña debe tener al menos 8 caracteres");

        if (repoUsuarios.buscarPorEmail(email).isPresent())
            throw new IllegalArgumentException("El email ya está registrado");

        // Validaciones nuevas por matrícula duplicada
        if (autoridad == Autoridad.MEDICO &&
                repoMedicos.buscarPorMatricula(matricula).isPresent()) {
            throw new IllegalArgumentException("Ya existe un médico con esa matrícula");
        }

        if (autoridad == Autoridad.ENFERMERA &&
                repoEnfermeras.buscarPorMatricula(matricula).isPresent()) {
            throw new IllegalArgumentException("Ya existe una enfermera con esa matrícula");
        }

        String hash = hasher.hash(passwordPlano);
        Usuario u = new Usuario(email, hash, autoridad, nombre, apellido, matricula);
        repoUsuarios.guardar(u);

        // Crear entidad asociada según rol
        if (autoridad == Autoridad.MEDICO) {
            Medico medico = new Medico(null,nombre, apellido, email, matricula);
            repoMedicos.guardar(medico);
        } else if (autoridad == Autoridad.ENFERMERA) {
            Enfermera enfermera = new Enfermera(null,nombre, apellido, email, matricula);
            repoEnfermeras.guardar(enfermera);
        }

        return u;
    }

    // Login
    public Usuario login(String email, String passwordPlano) {
        final String MSG = "Usuario o contraseña inválidos";
        return repoUsuarios.buscarPorEmail(email)
                .filter(u -> hasher.verify(passwordPlano, u.getPasswordHash()))
                .orElseThrow(() -> new RuntimeException(MSG));
    }
}
