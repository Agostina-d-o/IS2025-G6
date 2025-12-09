package org.example.domain;

//Representa la atención médica de un ingreso ya reclamado por un médico.

public class Atencion {

    private final Medico medico;
    private final String diagnostico;

    public Atencion(Medico medico, String diagnostico) {
        if (medico == null) {
            throw new IllegalArgumentException("El medico es obligatorio para registrar una atención");
        }
        if (diagnostico == null || diagnostico.isBlank()) {
            throw new IllegalArgumentException("El diagnóstico es obligatorio");
        }
        this.medico = medico;
        this.diagnostico = diagnostico;
    }

    public Medico getMedico() {
        return medico;
    }

    public String getDiagnostico() {
        return diagnostico;
    }
}
