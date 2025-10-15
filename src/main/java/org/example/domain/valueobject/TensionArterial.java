package org.example.domain.valueobject;

public class TensionArterial {
    private FrecuenciaDiastolica frecuenciaDiastolica;
    private FrecuenciaSistolica frecuenciaSistolica;

    public TensionArterial(Float frecuenciaSistolica, Float frecuenciaDiastolica) {
        this.frecuenciaSistolica = new FrecuenciaSistolica(frecuenciaSistolica);
        this.frecuenciaDiastolica = new FrecuenciaDiastolica(frecuenciaDiastolica);
    }


    public String getValorFormateado() {
        // 120/80 mmHg
        return String.format("%.0f/%.0f mmHg", frecuenciaSistolica, frecuenciaDiastolica);
    }

}
