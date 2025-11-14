package org.example.app.interfaces;

public interface ValidadorObraSocial {
    boolean obraSocialExiste(String codigoObraSocial);
    boolean estaAfiliado(String cuilPaciente, String codigoObraSocial, String numeroAfiliado);
}
