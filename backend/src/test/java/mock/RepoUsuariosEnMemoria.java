package mock;

import org.example.app.interfaces.RepositorioUsuarios;
import org.example.domain.Usuario;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class RepoUsuariosEnMemoria implements RepositorioUsuarios {
    private final Map<String, Usuario> data = new HashMap<>();

    @Override public Optional<Usuario> buscarPorEmail(String email) {
        return Optional.ofNullable(data.get(email));
    }

    @Override public void guardar(Usuario usuario) {
        data.put(usuario.getEmail(), usuario);
    }
}
