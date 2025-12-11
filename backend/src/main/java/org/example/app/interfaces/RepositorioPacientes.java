package org.example.app.interfaces;

import org.example.domain.Paciente;
import org.example.domain.valueobject.Cuil;

import java.util.List;
import java.util.Optional;

public interface RepositorioPacientes {
    void guardarPaciente(Paciente paciente);
    Optional<Paciente> buscarPacientePorCuil(Cuil cuil);
    List<Paciente> listarTodos();
}