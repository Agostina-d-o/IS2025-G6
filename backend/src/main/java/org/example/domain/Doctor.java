package org.example.domain;

public class Doctor extends Persona {

    private final String matricula;

    public Doctor(String cuil,
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

    public String getMatricula() {
        return matricula;
    }
}
