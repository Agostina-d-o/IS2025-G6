package org.example.domain.valueobject;

public class Temperatura {

    private final Float valor;

    public Temperatura(Float valor) {
        if (valor == null) {
            throw new IllegalArgumentException("La temperatura es obligatoria");
        }
        this.valor = valor;
    }

    public Float getValor() {
        return valor;
    }

    public String getValorFormateado() {
        return String.format("%.1f °C", valor);
    }
}
