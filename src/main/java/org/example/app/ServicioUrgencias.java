package org.example.app;

import org.example.app.interfaces.RepositorioPacientes;
import org.example.domain.Enfermera;
import org.example.domain.Ingreso;
import org.example.domain.NivelEmergencia;
import org.example.domain.Paciente;
import org.example.domain.queue.ColaAtencion;


import java.util.ArrayList;
import java.util.List;

public class ServicioUrgencias {

    private final ColaAtencion colaAtencion = new ColaAtencion();

    //SEGREGACION DE INTERFAZ
    //PATRON ADAPTADOR
    private RepositorioPacientes dbPacientes;

    private final List<Ingreso> listaEspera;

    //INYECCION DE DEPENDENCIA -> Pruebas
    public ServicioUrgencias(RepositorioPacientes dbPacientes) {
        this.dbPacientes = dbPacientes;
        this.listaEspera = new ArrayList<>(); //ahora se usa ColaAtencion
    }


    public void registrarUrgencia(String cuilPaciente,
                                   Enfermera enfermera,
                                   String informe,
                                  NivelEmergencia emergencia,
                                   Float temperatura,
                                   Float frecuenciaCardiaca,
                                   Float frecuenciaRespiratoria,
                                   Float frecuenciaSistolica,
                                   Float frecuenciaDiastolica){

        /*
        Paciente paciente = dbPacientes.buscarPacientePorCuil(cuilPaciente)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

         */

        // Asegurar paciente (crear si no existe)
        Paciente paciente = dbPacientes.buscarPacientePorCuil(cuilPaciente)
                .orElseGet(() -> {
                    Paciente nuevo = new Paciente(cuilPaciente, "", "", "");
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
                frecuenciaDiastolica);

        colaAtencion.agregar(ingreso);

        /*
        listaEspera.add(ingreso);
        listaEspera.sort(Ingreso::compareTo);
        */
    }

    public List<Ingreso> obtenerIngresosPendientes(){
        //return this.listaEspera;
        return colaAtencion.verComoLista();
    }
}
