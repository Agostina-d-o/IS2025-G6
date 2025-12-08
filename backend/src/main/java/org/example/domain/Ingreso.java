package org.example.domain;

import org.example.domain.valueobject.FrecuenciaCardiaca;
import org.example.domain.valueobject.FrecuenciaRespiratoria;
import org.example.domain.valueobject.Temperatura;
import org.example.domain.valueobject.TensionArterial;
import org.example.domain.Atencion;


import java.time.LocalDateTime;

public class Ingreso implements Comparable<Ingreso>{
    private static long SEQ = 1L;
    private final long id;
    private String diagnostico;
    Paciente paciente;
    Enfermera enfermera;
    LocalDateTime fechaIngreso;
    String informe;
    NivelEmergencia nivelEmergencia;
    EstadoIngreso estado;
    Temperatura temperatura;
    FrecuenciaCardiaca frecuenciaCardiaca;
    FrecuenciaRespiratoria frecuenciaRespiratoria;
    TensionArterial tensionArterial;

    private Atencion atencion;


    public Ingreso(Paciente paciente,
                   Enfermera enfermera,
                   String informe,
                   NivelEmergencia nivelEmergencia,
                   Float temperaturaParam,
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

        this.id = SEQ++;

        this.paciente = paciente;
        this.enfermera = enfermera;
        this.fechaIngreso = LocalDateTime.now();
        this.informe = informe;
        this.nivelEmergencia = nivelEmergencia;
        this.estado = EstadoIngreso.PENDIENTE;
        this.temperatura = new Temperatura(temperaturaParam);
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
    public void setEstado(EstadoIngreso nuevoEstado) {
        this.estado = nuevoEstado;
    }


    public LocalDateTime getFechaIngreso() {return fechaIngreso;  }


    public Enfermera getEnfermera() {
        return enfermera;
    }

    public long getId() { return id; }
    public Paciente getPaciente() { return paciente; }
    public NivelEmergencia getNivelEmergencia() { return nivelEmergencia; }
    public String getDiagnostico() { return diagnostico; }

    public void marcarEnProceso() { this.estado = EstadoIngreso.EN_PROCESO; }

    public void finalizar(String diagnostico) {
        this.diagnostico = diagnostico;
        this.estado = EstadoIngreso.FINALIZADO;
    }

    public Temperatura getTemperatura() {
        return temperatura;
    }

    public String getInforme() {
        return informe;
    }

    public FrecuenciaCardiaca getFrecuenciaCardiaca() {
        return frecuenciaCardiaca;
    }

    public FrecuenciaRespiratoria getFrecuenciaRespiratoria() {
        return frecuenciaRespiratoria;
    }

    public TensionArterial getTensionArterial() {
        return tensionArterial;
    }


    //Atención médica asociada a este ingreso (puede ser null si todavía no lo atendieron).
    public Atencion getAtencion() {
        return atencion;
    }

    public void setAtencion(Atencion atencion) {
        this.atencion = atencion;
    }
}
