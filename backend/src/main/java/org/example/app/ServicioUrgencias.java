package org.example.app;

import jakarta.annotation.PostConstruct;
import org.example.app.interfaces.RepositorioPacientes;
import org.example.app.interfaces.ValidadorObraSocial;
import org.example.domain.*;
import org.example.domain.queue.ColaAtencion;
import org.example.domain.valueobject.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ServicioUrgencias {

    private final ColaAtencion colaAtencion = new ColaAtencion();
    private final List<Ingreso> enProceso = new ArrayList<>();
    private final List<Ingreso> finalizados = new ArrayList<>();
    private ValidadorObraSocial validadorObraSocial;

    private RepositorioPacientes dbPacientes;

    public ServicioUrgencias(RepositorioPacientes dbPacientes, ValidadorObraSocial validadorObraSocial) {
        this.dbPacientes = dbPacientes;
        this.validadorObraSocial = validadorObraSocial;
    }

    // HU-002
    public Paciente registrarPaciente(String cuil,
                                      String nombre,
                                      String apellido,
                                      Domicilio domicilio,
                                      AfiliacionObraSocial afiliacionOpcional) {

        if (nombre == null || nombre.isBlank())
            throw new IllegalArgumentException("El nombre es obligatorio");

        if (apellido == null || apellido.isBlank())
            throw new IllegalArgumentException("El apellido es obligatorio");


        Cuil cuilVO = new Cuil(cuil);

        if (dbPacientes.buscarPacientePorCuil(cuilVO).isPresent()) {
            throw new IllegalArgumentException("Ya existe un paciente registrado con ese CUIL");
        }

        if (afiliacionOpcional != null) {
            String codOS = afiliacionOpcional.getObraSocial().getCodigo();
            String nroAf = afiliacionOpcional.getNumeroAfiliado();
            if (!validadorObraSocial.obraSocialExiste(codOS))
                throw new IllegalArgumentException("Obra social inexistente");
            if (!validadorObraSocial.estaAfiliado(cuil, codOS, nroAf))
                throw new IllegalArgumentException("El paciente no está afiliado a la obra social");
        }

        Paciente paciente = new Paciente(cuilVO, nombre, apellido, domicilio, afiliacionOpcional);
        dbPacientes.guardarPaciente(paciente);
        return paciente;
    }

    // HU-001 registrar urgencia
    public void registrarUrgencia(String cuilPaciente,
                                  Enfermera enfermera,
                                  String informe,
                                  NivelEmergencia emergencia,
                                  Float temperatura,
                                  Float frecuenciaCardiaca,
                                  Float frecuenciaRespiratoria,
                                  Float frecuenciaSistolica,
                                  Float frecuenciaDiastolica) {

        Cuil cuilVO = new Cuil(cuilPaciente);

        if (enfermera == null) {
            throw new IllegalArgumentException("La enfermera es obligatoria para registrar una urgencia");
        }


        Paciente paciente = dbPacientes.buscarPacientePorCuil(cuilVO)
                .orElseGet(() -> {
                    // Alta mínima si no existe
                    Domicilio dummy = new Domicilio("S/D", 1, "San Miguel de Tucumán");
                    Paciente nuevo = new Paciente(cuilVO, "N/D", "N/D", dummy, null);
                    dbPacientes.guardarPaciente(nuevo);
                    return nuevo;
                });

        // evitar duplicado si ya hay ingreso activo
        boolean yaTieneIngresoActivo =
                colaAtencion.verComoLista().stream()
                        .anyMatch(i -> i.getPaciente().getCuil().equals(cuilVO)
                                && i.getEstado() == EstadoIngreso.PENDIENTE)
                        ||
                        enProceso.stream()
                                .anyMatch(i -> i.getPaciente().getCuil().equals(cuilVO));

        if (yaTieneIngresoActivo) {
            throw new IllegalStateException("Ya existe un ingreso pendiente o en proceso para este paciente.");
        }

        Ingreso ingreso = new Ingreso(
                paciente,
                enfermera,
                informe,
                emergencia,
                temperatura,
                frecuenciaCardiaca,
                frecuenciaRespiratoria,
                frecuenciaSistolica,
                frecuenciaDiastolica
        );

        colaAtencion.agregar(ingreso);
    }

    public List<Ingreso> obtenerIngresosPendientes() {
        return colaAtencion.verComoLista().stream()
                .filter(i -> i.getEstado() == EstadoIngreso.PENDIENTE)
                .toList();
    }

    public Ingreso atenderProximoPaciente() {
        if (colaAtencion.estaVacia()) {
            throw new IllegalStateException("No hay ingresos pendientes en la lista de espera.");
        }
        Ingreso siguiente = colaAtencion.atender();
        siguiente.marcarEnProceso();
        enProceso.add(siguiente);
        return siguiente;
    }

    public Ingreso finalizarIngreso(long idIngreso, String diagnostico, Medico medico) {
        Ingreso ingreso = enProceso.stream()
                .filter(i -> i.getId() == idIngreso)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No se encontró un ingreso EN_PROCESO con el id " + idIngreso));

        if (ingreso.getAtencion() != null) {
            throw new IllegalStateException("Este ingreso ya fue finalizado anteriormente.");
        }

        Atencion atencion = new Atencion(medico, diagnostico);
        ingreso.setAtencion(atencion);
        ingreso.setEstado(EstadoIngreso.FINALIZADO);

        enProceso.remove(ingreso);
        finalizados.add(ingreso);

        return ingreso;
    }


    public List<Ingreso> obtenerIngresosEnProceso() {
        return new ArrayList<>(enProceso);
    }

    public List<Ingreso> obtenerIngresosFinalizados() {
        return new ArrayList<>(finalizados);
    }

    public List<Paciente> listarPacientesRegistrados() {
        return dbPacientes.listarTodos();
    }

    @PostConstruct
    public void resetearIds() {
        Ingreso.resetSecuencia();
    }

}
