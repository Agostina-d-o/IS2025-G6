package org.example.app;

import org.example.app.interfaces.PasswordHasher;
import org.example.app.interfaces.RepositorioUsuarios;
import org.example.app.interfaces.RepositorioMedicos;
import org.example.app.interfaces.RepositorioEnfermeras;
import org.example.domain.Autoridad;
import org.example.domain.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ServicioAutenticacionTest {

    private RepositorioUsuarios repo;
    private PasswordHasher hasher;
    private RepositorioMedicos repoMedicos;
    private RepositorioEnfermeras repoEnfermeras;
    private ServicioAutenticacion auth;

    @BeforeEach
    void setUp() {
        repo = mock(RepositorioUsuarios.class);
        hasher = mock(PasswordHasher.class);
        repoMedicos = mock(RepositorioMedicos.class);
        repoEnfermeras = mock(RepositorioEnfermeras.class);

        auth  = new ServicioAutenticacion(repo, hasher, repoMedicos, repoEnfermeras);
    }

    @Test
    void registra_ok_hashea_y_persiste() {
        String email = "ana@hospital.tuc";
        String raw   = "secreto123";
        String nombre = "Ana";
        String apellido = "Pérez";
        String matricula = "MAT123";

        when(repo.buscarPorEmail(email)).thenReturn(Optional.empty());
        when(hasher.hash(raw)).thenReturn("HASH-ABC");

        Usuario u = auth.registrar(email, raw, Autoridad.ENFERMERA, nombre, apellido, matricula);

        assertEquals(email, u.getEmail());
        assertEquals("HASH-ABC", u.getPasswordHash());
        assertEquals(Autoridad.ENFERMERA, u.getAutoridad());
        assertEquals(nombre, u.getNombre());
        assertEquals(apellido, u.getApellido());
        assertEquals(matricula, u.getMatricula());

        verify(hasher).hash(raw);
        verify(repo).guardar(any(Usuario.class));
    }

    @Test
    void registra_falla_por_password_corta() {
        assertThrows(IllegalArgumentException.class,
                () -> auth.registrar("doc@hospi.ar", "1234567", Autoridad.MEDICO, "Ana", "Perez", "MAT1"));
        verifyNoInteractions(repo, hasher);
    }

    @Test
    void registra_falla_por_email_duplicado() {
        when(repo.buscarPorEmail("doc@hospi.ar")).thenReturn(Optional.of(mock(Usuario.class)));

        assertThrows(IllegalArgumentException.class,
                () -> auth.registrar("doc@hospi.ar", "passwordOK", Autoridad.MEDICO, "Ana", "Perez", "MAT2"));

        verify(repo, never()).guardar(any());
        verify(hasher, never()).hash(any());
    }

    @Test
    void registra_falla_por_email_invalido() {
        when(repo.buscarPorEmail("mal")).thenReturn(Optional.empty());
        when(hasher.hash("passwordOK")).thenReturn("H");

        assertThrows(IllegalArgumentException.class,
                () -> auth.registrar("mal", "passwordOK", Autoridad.ENFERMERA, "Ana", "Perez", "MAT3"));

        verify(repo, never()).guardar(any());
    }

    @Test
    void login_ok() {
        String email = "ana@hospi.ar";
        Usuario u = new Usuario(email, "HASHED", Autoridad.ENFERMERA, "Ana", "Perez", "MAT100");

        when(repo.buscarPorEmail(email)).thenReturn(Optional.of(u));
        when(hasher.verify("secreto123", "HASHED")).thenReturn(true);

        Usuario logueado = auth.login(email, "secreto123");

        assertSame(u, logueado);
        verify(hasher).verify("secreto123", "HASHED");
    }

    @Test
    void login_falla_usuario_inexistente_mensaje_unico() {
        when(repo.buscarPorEmail("x@x.com")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> auth.login("x@x.com", "algo1234"));

        assertEquals("Usuario o contraseña inválidos", ex.getMessage());
    }

    @Test
    void login_falla_password_incorrecta_mensaje_unico() {
        String email = "ana@hospi.ar";
        Usuario u = new Usuario(email, "HASHED", Autoridad.ENFERMERA, "Ana", "Perez", "MAT222");

        when(repo.buscarPorEmail(email)).thenReturn(Optional.of(u));
        when(hasher.verify("malpass", "HASHED")).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> auth.login(email, "malpass"));

        assertEquals("Usuario o contraseña inválidos", ex.getMessage());
    }
}
