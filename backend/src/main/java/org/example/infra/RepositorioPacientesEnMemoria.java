package org.example.infra;

import org.example.app.interfaces.RepositorioPacientes;
import org.example.domain.Paciente;
import org.example.domain.valueobject.Cuil;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class RepositorioPacientesEnMemoria implements RepositorioPacientes {

    private final Map<Cuil, Paciente> pacientes = new HashMap<>();

    @Override
    public void guardarPaciente(Paciente paciente) {
        pacientes.put(paciente.getCuil(), paciente);
    }

    @Override
    public Optional<Paciente> buscarPacientePorCuil(Cuil cuil) {
        return Optional.ofNullable(pacientes.get(cuil));
    }

    @Override
    public List<Paciente> listarTodos() {
        return List.copyOf(pacientes.values()); // OK
    }
}
