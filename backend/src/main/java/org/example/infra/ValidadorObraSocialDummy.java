package org.example.infra;

import org.example.app.interfaces.ValidadorObraSocial;
import org.springframework.stereotype.Component;

@Component
public class ValidadorObraSocialDummy implements ValidadorObraSocial {

    @Override
    public boolean obraSocialExiste(String codigoObraSocial) {
        return !codigoObraSocial.isBlank();
    }

    @Override
    public boolean estaAfiliado(String cuilPaciente, String codigoObraSocial, String numeroAfiliado) {
        return cuilPaciente != null && numeroAfiliado != null && !numeroAfiliado.isBlank();
    }
}
