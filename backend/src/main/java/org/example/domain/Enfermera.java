package org.example.domain;

import org.example.domain.valueobject.Cuil;

public class Enfermera extends Persona {

    private final String matricula;

    public Enfermera(Cuil cuil,
                     String nombre,
                     String apellido,
                     String email,
                     String matricula) {
        super(cuil, nombre, apellido, email);
        if (matricula == null || matricula.isBlank()) {
            throw new IllegalArgumentException("Matrícula obligatoria");
        }
        this.matricula = matricula;
    }

    // Constructor auxiliar para tests/step definitions existentes
    public Enfermera(String nombre, String apellido) {
        this(null, nombre, apellido, null, "MATRICULA-DUMMY");
    }

    public String getMatricula() {
        return matricula;
    }
}
