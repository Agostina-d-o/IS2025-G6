package org.example.domain.valueobject;

import java.util.Objects;

public class Cuil {
    private final String valor;

    public Cuil(String valor) {
        if (valor == null || !valor.matches("\\d{11}")) {
            throw new IllegalArgumentException("CUIL inválido. Debe tener 11 dígitos numéricos.");
        }
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }

    @Override
    public String toString() {
        return valor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cuil)) return false;
        Cuil cuil = (Cuil) o;
        return valor.equals(cuil.valor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(valor);
    }
}
