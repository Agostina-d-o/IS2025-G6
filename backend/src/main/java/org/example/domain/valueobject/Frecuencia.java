package org.example.domain.valueobject;

public abstract class Frecuencia {
    protected Float value;

    public Frecuencia(Float value) {
        validarFaltante(value);
        validarFrecuenciaNoNegativa(value);
        this.value = value;
    }

    private void validarFrecuenciaNoNegativa(Float value){
        if (value < 0){
            throw this.notificarError();
        }
    }

    private void validarFaltante(Float value){
        if (value == null){
            throw new IllegalArgumentException(mensajeFaltanteErronea());
        }
    }

    protected abstract RuntimeException notificarError();
    public abstract String getValorFormateado();
    protected abstract String mensajeFaltanteErronea();


}
