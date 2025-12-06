package org.example.domain;

import org.example.domain.valueobject.AfiliacionObraSocial;
import org.example.domain.valueobject.Domicilio;

public class Paciente {
    private String cuil;
    private String nombre;
    private String apellido;
    private Domicilio domicilio;
    private AfiliacionObraSocial afiliacionObraSocial;


    public Paciente(String cuil,
                    String nombre,
                    String apellido,
                    Domicilio domicilio,
                    AfiliacionObraSocial afiliacionObraSocial) {

        if (!cuilValido(cuil)) throw new IllegalArgumentException("CUIL inválido (11 dígitos)");
        if (nombre == null || nombre.isBlank()) throw new IllegalArgumentException("Nombre obligatorio");
        if (apellido == null || apellido.isBlank()) throw new IllegalArgumentException("Apellido obligatorio");
        if (domicilio == null) throw new IllegalArgumentException("Domicilio obligatorio");

        this.cuil = cuil;
        this.nombre = nombre;
        this.apellido = apellido;
        this.domicilio = domicilio;
        this.afiliacionObraSocial = afiliacionObraSocial; // puede ser null
    }

    private boolean cuilValido(String cuil) {
        return cuil != null && cuil.matches("\\d{11}");
    }


    public String getCuil() { return cuil; }
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public Domicilio getDomicilio() { return domicilio; }
    public AfiliacionObraSocial getAfiliacionObraSocial() { return afiliacionObraSocial; }
}
