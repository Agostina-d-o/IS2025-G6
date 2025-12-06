package org.example.domain.valueobject;

public class TensionArterial {
    private FrecuenciaDiastolica frecuenciaDiastolica;
    private FrecuenciaSistolica frecuenciaSistolica;

    public TensionArterial(Float frecuenciaSistolica, Float frecuenciaDiastolica) {
        this.frecuenciaSistolica = new FrecuenciaSistolica(frecuenciaSistolica);
        this.frecuenciaDiastolica = new FrecuenciaDiastolica(frecuenciaDiastolica);
    }


    public String getValorFormateado() {
        String s = (frecuenciaSistolica != null)  ? frecuenciaSistolica.getValorFormateado()  : null;
        String d = (frecuenciaDiastolica != null) ? frecuenciaDiastolica.getValorFormateado() : null;
        if (s == null || d == null) return null;
        return s + "/" + d;   // evita %f y el error
    }
}
