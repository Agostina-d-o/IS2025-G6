package org.example.app.interfaces;

import org.example.domain.Medico;
import java.util.Optional;

public interface RepositorioMedicos {
    void guardar(Medico medico);
    Optional<Medico> buscarPorMatricula(String matricula);
}
