package org.example.infra;

import org.example.app.interfaces.RepositorioMedicos;
import org.example.domain.Medico;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class RepositorioMedicosEnMemoria implements RepositorioMedicos {

    private final Map<String, Medico> porMatricula = new HashMap<>();

    @Override
    public void guardar(Medico medico) {
        porMatricula.put(medico.getMatricula(), medico);
    }

    @Override
    public Optional<Medico> buscarPorMatricula(String matricula) {
        return Optional.ofNullable(porMatricula.get(matricula));
    }
}
