package org.example.domain.valueobject;

public class Domicilio {
    private final String calle;
    private final int numero;
    private final String localidad; // Por alcance: string, "San Miguel de Tucumán"

    public Domicilio(String calle, int numero, String localidad) {
        if (calle == null || calle.isBlank()) throw new IllegalArgumentException("Calle es obligatoria");
        if (numero <= 0) throw new IllegalArgumentException("Número nulo o inválido");
        if (localidad == null || localidad.isBlank()) throw new IllegalArgumentException("Localidad es obligatoria");
        this.calle = calle;
        this.numero = numero;
        this.localidad = localidad;
    }

    public String getCalle() { return calle; }
    public int getNumero() { return numero; }
    public String getLocalidad() { return localidad; }
}
