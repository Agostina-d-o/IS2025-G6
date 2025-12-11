package org.example.infra;

import org.example.app.interfaces.ValidadorObraSocial;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

@Component
public class ValidadorObraSocialDummy implements ValidadorObraSocial {

    private static final Set<String> OBRAS_VALIDAS = Set.of(
            "OSDE", "PAMI", "SUBSIDIO", "SWISS"
    );

    private static final Map<String, Set<String>> AFILIADOS_VALIDOS = Map.of(
            "OSDE", Set.of("O100", "O101"),
            "PAMI", Set.of("P100", "P101"),
            "SUBSIDIO", Set.of("SS100", "SS101"),
            "SWISS", Set.of("S100", "S101")
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

        if (!obraSocialExiste(codigoObraSocial)) return false;

        if (cuilPaciente == null || cuilPaciente.isBlank()) return false;
        if (numeroAfiliado == null || numeroAfiliado.isBlank()) return false;

        Set<String> numerosValidos = AFILIADOS_VALIDOS.get(codigoObraSocial.toUpperCase());

        if (numerosValidos == null) return false;

        return numerosValidos.contains(numeroAfiliado.trim());
    }
}
