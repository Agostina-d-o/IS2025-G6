package org.example.infra;

import org.example.app.interfaces.RepositorioPacientes;
import org.example.domain.Paciente;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class RepositorioPacientesEnMemoria implements RepositorioPacientes {

    private final Map<String, Paciente> pacientes = new HashMap<>();

    @Override
    public void guardarPaciente(Paciente paciente) {
        pacientes.put(paciente.getCuil(), paciente);
    }

    @Override
    public Optional<Paciente> buscarPacientePorCuil(String cuil) {
        return Optional.ofNullable(pacientes.get(cuil));
    }
}
