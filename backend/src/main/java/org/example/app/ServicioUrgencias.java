package org.example.app;

import org.example.app.interfaces.RepositorioPacientes;
import org.example.app.interfaces.ValidadorObraSocial;
import org.example.domain.Enfermera;
import org.example.domain.Ingreso;
import org.example.domain.NivelEmergencia;
import org.example.domain.Paciente;
import org.example.domain.queue.ColaAtencion;
import org.example.domain.valueobject.AfiliacionObraSocial;
import org.example.domain.valueobject.Domicilio;
import org.springframework.stereotype.Service;


//import java.util.ArrayList;
import java.util.List;

@Service
public class ServicioUrgencias {

    private final ColaAtencion colaAtencion = new ColaAtencion();
    private ValidadorObraSocial validadorObraSocial;

    //SEGREGACION DE INTERFAZ
    //PATRON ADAPTADOR
    private RepositorioPacientes dbPacientes;


    //private List<Ingreso> listaEspera;

    //INYECCION DE DEPENDENCIA -> Pruebas
    public ServicioUrgencias(RepositorioPacientes dbPacientes, ValidadorObraSocial validadorObraSocial) {
        this.dbPacientes = dbPacientes;
        this.validadorObraSocial = validadorObraSocial;
        //this.listaEspera = new ArrayList<>(); //ahora se usa ColaAtencion
    }

    // HU-002
    public Paciente registrarPaciente(String cuil,
                                      String nombre,
                                      String apellido,
                                      Domicilio domicilio,
                                      AfiliacionObraSocial afiliacionOpcional) {
        if (afiliacionOpcional != null) {
            String codOS = afiliacionOpcional.getObraSocial().getCodigo();
            String nroAf = afiliacionOpcional.getNumeroAfiliado();
            if (!validadorObraSocial.obraSocialExiste(codOS))
                throw new IllegalArgumentException("No se puede registrar: obra social inexistente");
            if (!validadorObraSocial.estaAfiliado(cuil, codOS, nroAf))
                throw new IllegalArgumentException("No se puede registrar: el paciente no está afiliado a la obra social");
        }
        Paciente paciente = new Paciente(cuil, nombre, apellido, domicilio, afiliacionOpcional);
        dbPacientes.guardarPaciente(paciente);
        return paciente;
    }

    // Urgencia: permite auto-alta “mínima” si no existe
    public void registrarUrgencia(String cuilPaciente,
                                  Enfermera enfermera,
                                  String informe,
                                  NivelEmergencia emergencia,
                                  Float temperatura,
                                  Float frecuenciaCardiaca,
                                  Float frecuenciaRespiratoria,
                                  Float frecuenciaSistolica,
                                  Float frecuenciaDiastolica) {

        Paciente paciente = dbPacientes.buscarPacientePorCuil(cuilPaciente)
                .orElseGet(() -> {
                    // Alta “provisional” mínima válida (sin OS)
                    Domicilio dummy = new Domicilio("S/D", 1, "San Miguel de Tucumán");
                    Paciente nuevo = new Paciente(cuilPaciente, "N/D", "N/D", dummy, null);
                    dbPacientes.guardarPaciente(nuevo);
                    return nuevo;
                });

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

    public List<Ingreso> obtenerIngresosPendientes(){
        //return this.listaEspera;
        return colaAtencion.verComoLista();
    }
}
