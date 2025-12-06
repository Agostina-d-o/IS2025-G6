package org.example.domain.valueobject;

public class AfiliacionObraSocial {
    private final ObraSocial obraSocial;
    private final String numeroAfiliado;

    public AfiliacionObraSocial(ObraSocial obraSocial, String numeroAfiliado) {
        if (obraSocial == null) throw new IllegalArgumentException("Obra social es obligatoria");
        if (numeroAfiliado == null || numeroAfiliado.isBlank()) throw new IllegalArgumentException("Número de afiliado obligatorio");
        this.obraSocial = obraSocial;
        this.numeroAfiliado = numeroAfiliado;
    }

    public ObraSocial getObraSocial() { return obraSocial; }
    public String getNumeroAfiliado() { return numeroAfiliado; }
}
