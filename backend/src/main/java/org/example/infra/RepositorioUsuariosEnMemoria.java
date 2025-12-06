package org.example.infra;

import org.example.app.interfaces.RepositorioUsuarios;
import org.example.domain.Usuario;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class RepositorioUsuariosEnMemoria implements RepositorioUsuarios {

    private final Map<String, Usuario> usuarios = new HashMap<>();

    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        return Optional.ofNullable(usuarios.get(email));
    }

    @Override
    public void guardar(Usuario usuario) {
        usuarios.put(usuario.getEmail(), usuario);
    }
}
