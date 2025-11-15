package org.example.domain;

import org.example.domain.valueobject.FrecuenciaCardiaca;
import org.example.domain.valueobject.FrecuenciaRespiratoria;
import org.example.domain.valueobject.TensionArterial;

import java.time.LocalDateTime;

public class Ingreso implements Comparable<Ingreso>{
    Paciente paciente;
    Enfermera enfermera;
    LocalDateTime fechaIngreso;
    String informe;
    NivelEmergencia nivelEmergencia;
    EstadoIngreso estado;
    Float temperatura;
    FrecuenciaCardiaca frecuenciaCardiaca;
    FrecuenciaRespiratoria frecuenciaRespiratoria;
    TensionArterial tensionArterial;

    public Ingreso(Paciente paciente,
                   Enfermera enfermera,
                   String informe,
                   NivelEmergencia nivelEmergencia,
                   Float temperatura,
                   Float frecuenciaCardiaca,
                   Float frecuenciaRespiratoria,
                   Float frecuenciaSistolica,
                   Float frecuenciaDiastolica){

        // Validar textos obligatorios
        if (informe == null || informe.isBlank()) {
            throw new IllegalArgumentException("Falta el informe del ingreso");
        }
        if (nivelEmergencia == null) {
            throw new IllegalArgumentException("Falta el nivel de emergencia");
        }

        this.paciente = paciente;
        this.enfermera = enfermera;
        this.fechaIngreso = LocalDateTime.now();
        this.informe = informe;
        this.nivelEmergencia = nivelEmergencia;
        this.estado = EstadoIngreso.PENDIENTE;
        this.temperatura = temperatura;
        this.frecuenciaCardiaca = new FrecuenciaCardiaca(frecuenciaCardiaca);
        this.frecuenciaRespiratoria = new FrecuenciaRespiratoria(frecuenciaRespiratoria);
        this.tensionArterial = new TensionArterial(frecuenciaSistolica,frecuenciaDiastolica);
    }


    public String getCuilPaciente(){
        return this.paciente.getCuil();
    }

    @Override
    public int compareTo(Ingreso o) {
        int cmp = this.nivelEmergencia.compararCon(o.nivelEmergencia);
        if (cmp != 0) return cmp;
        // FIFO por fecha/hora
        return this.fechaIngreso.compareTo(o.fechaIngreso);
    }

    public EstadoIngreso getEstado() { return estado;  }

    public LocalDateTime getFechaIngreso() {return fechaIngreso;  }


    public Enfermera getEnfermera() {
        return enfermera;
    }
}
