package mock;

import org.example.app.interfaces.RepositorioPacientes;
import org.example.domain.Paciente;
import org.example.domain.valueobject.Cuil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class DBPruebaEnMemoria implements RepositorioPacientes {

    private final Map<Cuil, Paciente> pacientes = new HashMap<>();

    @Override
    public void guardarPaciente(Paciente paciente) {
        pacientes.put(paciente.getCuil(), paciente);
    }

    @Override
    public Optional<Paciente> buscarPacientePorCuil(Cuil cuil) {
        return Optional.ofNullable(pacientes.get(cuil));
    }

    //PARA TESTS
    public Optional<Paciente> buscarPacientePorCuil(String cuil) {
        return buscarPacientePorCuil(new Cuil(cuil));
    }



    @Override
    public List<Paciente> listarTodos() {
        return List.copyOf(pacientes.values());
    }
}
