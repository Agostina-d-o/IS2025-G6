package org.example.domain;

//Representa la atención médica de un ingreso ya reclamado por un médico.

public class Atencion {

    private final Doctor doctor;
    private final String informe;

    public Atencion(Doctor doctor, String informe) {
        if (doctor == null) {
            throw new IllegalArgumentException("El doctor es obligatorio para registrar una atención");
        }
        if (informe == null || informe.isBlank()) {
            throw new IllegalArgumentException("El informe/diagnóstico es obligatorio");
        }
        this.doctor = doctor;
        this.informe = informe;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public String getInforme() {
        return informe;
    }
}
