package org.example.infra;

import org.example.app.interfaces.ValidadorObraSocial;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class ValidadorObraSocialDummy implements ValidadorObraSocial {

    // Lista de cod de obras sociales “válidas”
    private static final Set<String> OBRAS_VALIDAS = Set.of(
            "OSDE",
            "PAMI",
            "SUBSIDIO",
            "SWISS"
    );

    @Override
    public boolean obraSocialExiste(String codigoObraSocial) {
        if (codigoObraSocial == null) return false;
        return OBRAS_VALIDAS.stream()
                .anyMatch(v -> v.equalsIgnoreCase(codigoObraSocial.trim()));
    }

    @Override
    public boolean estaAfiliado(String cuilPaciente,
                                String codigoObraSocial,
                                String numeroAfiliado) {

        // que la OS exista
        if (!obraSocialExiste(codigoObraSocial)) return false;

        // reglas mínimas de afiliación
        return cuilPaciente != null && !cuilPaciente.isBlank()
                && numeroAfiliado != null && !numeroAfiliado.isBlank();
    }
}
