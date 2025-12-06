package org.example.domain.valueobject;

public class ObraSocial {
    private final String codigo;  // o id
    private final String nombre;

    public ObraSocial(String codigo, String nombre) {
        if (codigo == null || codigo.isBlank()) throw new IllegalArgumentException("Código OS obligatorio");
        if (nombre == null || nombre.isBlank()) throw new IllegalArgumentException("Nombre OS obligatorio");
        this.codigo = codigo;
        this.nombre = nombre;
    }

    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
}
