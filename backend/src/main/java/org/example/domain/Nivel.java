package org.example.domain;

import java.time.Duration;

public class Nivel {

    private final int nivel;
    private final String nombre;
    private final Duration duracionMaxEspera;

    public Nivel(int nivel, String nombre, Duration duracionMaxEspera) {
        if (nivel < 0) {
            throw new IllegalArgumentException("El número de nivel debe ser >= 0");
        }
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre de nivel es obligatorio");
        }
        if (duracionMaxEspera == null || duracionMaxEspera.isNegative()) {
            throw new IllegalArgumentException("La duración máxima de espera es obligatoria y no puede ser negativa");
        }
        this.nivel = nivel;
        this.nombre = nombre;
        this.duracionMaxEspera = duracionMaxEspera;
    }

    public int getNivel() {
        return nivel;
    }

    public String getNombre() {
        return nombre;
    }

    public Duration getDuracionMaxEspera() {
        return duracionMaxEspera;
    }
}
