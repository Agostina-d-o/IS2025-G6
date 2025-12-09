package org.example.domain;

import org.example.domain.valueobject.AfiliacionObraSocial;
import org.example.domain.valueobject.Cuil;
import org.example.domain.valueobject.Domicilio;

public class Paciente extends Persona {

    private Domicilio domicilio;
    private AfiliacionObraSocial afiliacionObraSocial;

    public Paciente(Cuil cuil,
                    String nombre,
                    String apellido,
                    Domicilio domicilio,
                    AfiliacionObraSocial afiliacionObraSocial) {

        super(cuil, nombre, apellido, null); // el cuil ya fue validado al construir el VO

        if (nombre == null || nombre.isBlank()) throw new IllegalArgumentException("Nombre obligatorio");
        if (apellido == null || apellido.isBlank()) throw new IllegalArgumentException("Apellido obligatorio");
        if (domicilio == null) throw new IllegalArgumentException("Domicilio obligatorio");

        this.domicilio = domicilio;
        this.afiliacionObraSocial = afiliacionObraSocial; // puede ser null
    }

    public Domicilio getDomicilio() { return domicilio; }
    public AfiliacionObraSocial getAfiliacionObraSocial() { return afiliacionObraSocial; }
}
