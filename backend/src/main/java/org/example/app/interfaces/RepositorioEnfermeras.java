package org.example.app.interfaces;

import org.example.domain.Enfermera;
import java.util.Optional;

public interface RepositorioEnfermeras {
    void guardar(Enfermera enfermera);
    Optional<Enfermera> buscarPorMatricula(String matricula);
}
