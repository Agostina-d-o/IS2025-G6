package org.example.app.interfaces;

import org.example.domain.Usuario;
import java.util.Optional;

public interface RepositorioUsuarios {
    Optional<Usuario> buscarPorEmail(String email);
    void guardar(Usuario usuario);
}
