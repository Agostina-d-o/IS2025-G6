package org.example.infra;

import org.example.app.interfaces.RepositorioEnfermeras;
import org.example.domain.Enfermera;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class RepositorioEnfermerasEnMemoria implements RepositorioEnfermeras {

    private final Map<String, Enfermera> porMatricula = new HashMap<>();

    @Override
    public void guardar(Enfermera enfermera) {
        porMatricula.put(enfermera.getMatricula(), enfermera);
    }

    @Override
    public Optional<Enfermera> buscarPorMatricula(String matricula) {
        return Optional.ofNullable(porMatricula.get(matricula));
    }
}
