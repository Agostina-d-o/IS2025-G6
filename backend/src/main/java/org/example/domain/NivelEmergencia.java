package org.example.domain;

import java.time.Duration;

public enum NivelEmergencia {

    CRITICA       (new Nivel(0, "CRITICA",         Duration.ofMinutes(0))),
    EMERGENCIA    (new Nivel(1, "EMERGENCIA",      Duration.ofMinutes(10))),
    URGENCIA      (new Nivel(2, "URGENCIA",        Duration.ofMinutes(30))),
    URGENCIA_MENOR(new Nivel(3, "URGENCIA MENOR",  Duration.ofHours(1))),
    SIN_URGENCIA  (new Nivel(4, "SIN URGENCIA",    Duration.ofHours(2)));

    private final Nivel nivel;

    NivelEmergencia(Nivel nivel) {
        this.nivel = nivel;
    }

    public boolean tieneNombre(String nombre) {
        if (nombre == null) return false;
        return this.nivel.getNombre().equalsIgnoreCase(nombre);
    }

    /**
     * Compara prioridades: menor número de nivel = más prioridad.
     */
    public int compararCon(NivelEmergencia otro) {
        return Integer.compare(this.nivel.getNivel(), otro.nivel.getNivel());
    }

    public Nivel getNivel() {
        return nivel;
    }
}
