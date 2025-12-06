package org.example.domain.valueobject;

public class FrecuenciaDiastolica extends Frecuencia{

    public FrecuenciaDiastolica(Float value) {
        super(value);
    }

    @Override
    protected RuntimeException notificarError() {
        return new RuntimeException("La frecuencia diastólica no puede ser negativa");
    }

    @Override
    public String getValorFormateado() {
        return String.format("%.0f mmHg", this.value);
    }

    @Override
    protected String mensajeFaltanteErronea() {
        return "Falta la tensión arterial o tiene un formato erróneo";
    }
}
