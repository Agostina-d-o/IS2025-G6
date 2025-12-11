package org.example.app.controller.dto;

import org.example.domain.Autoridad;
import org.example.domain.Enfermera;
import org.example.domain.NivelEmergencia;

public class IngresoDTO {
    public String cuilPaciente;
    public String informe;
    public String nivelEmergencia;
    public Float temperatura;
    public Float frecuenciaCardiaca;
    public Float frecuenciaRespiratoria;
    public Float presionSistolica;
    public Float presionDiastolica;
    public String nombreEnfermera;
    public String apellidoEnfermera;
    public Autoridad autoridad;

    public NivelEmergencia getNivel() {
        return NivelEmergencia.valueOf(nivelEmergencia.toUpperCase().replace(" ", "_"));
    }

    public Enfermera getEnfermera() {
        return new Enfermera(nombreEnfermera, apellidoEnfermera);
    }
}
