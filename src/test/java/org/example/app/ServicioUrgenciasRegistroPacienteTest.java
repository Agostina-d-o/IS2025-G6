package org.example.app;

import org.example.app.interfaces.RepositorioPacientes;
import org.example.app.interfaces.ValidadorObraSocial;
import org.example.domain.Paciente;
import org.example.domain.valueobject.AfiliacionObraSocial;
import org.example.domain.valueobject.Domicilio;
import org.example.domain.valueobject.ObraSocial;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ServicioUrgenciasRegistroPacienteTest {

    private RepositorioPacientes repoPacientes;
    private ValidadorObraSocial validadorOS;
    private ServicioUrgencias servicio;

    @BeforeEach
    void setUp() {
        repoPacientes = mock(RepositorioPacientes.class);
        validadorOS   = mock(ValidadorObraSocial.class);
        servicio      = new ServicioUrgencias(repoPacientes, validadorOS);
    }

    @Test
    void creaPaciente_conMandatorios_yOSExistente_yAfiliado() {
        // Preparación
        String cuil = "20123456789";
        Domicilio domicilio = new Domicilio("Mitre", 123, "San Miguel de Tucumán");
        ObraSocial os = new ObraSocial("OSDE", "OSDE");
        AfiliacionObraSocial afiliacion = new AfiliacionObraSocial(os, "ABC123");

        when(validadorOS.obraSocialExiste("OSDE")).thenReturn(true);
        when(validadorOS.estaAfiliado(cuil, "OSDE", "ABC123")).thenReturn(true);

        // Ejecución
        Paciente p = servicio.registrarPaciente(cuil, "María", "Pérez", domicilio, afiliacion);

        // Validación
        assertNotNull(p);
        assertEquals(cuil, p.getCuil());
        verify(repoPacientes, times(1)).guardarPaciente(any(Paciente.class));
        verify(validadorOS).obraSocialExiste("OSDE");
        verify(validadorOS).estaAfiliado(cuil, "OSDE", "ABC123");
    }

    @Test
    void creaPaciente_conMandatorios_sinObraSocial_esExitoso() {
        // Preparación
        String cuil = "23123456789";
        Domicilio domicilio = new Domicilio("Rivadavia", 456, "San Miguel de Tucumán");

        // Ejecución
        Paciente p = servicio.registrarPaciente(cuil, "Juan", "Gómez", domicilio, null);

        // Validación
        assertNotNull(p);
        assertNull(p.getAfiliacionObraSocial());
        verify(repoPacientes, times(1)).guardarPaciente(any(Paciente.class));
        verifyNoInteractions(validadorOS); // No debe consultar nada si no hay OS
    }

    @Test
    void fallaCuandoObraSocialNoExiste() {
        String cuil = "27123456789";
        Domicilio domicilio = new Domicilio("Sarmiento", 10, "San Miguel de Tucumán");
        ObraSocial os = new ObraSocial("XYZ", "Inexistente");
        AfiliacionObraSocial afiliacion = new AfiliacionObraSocial(os, "AF001");

        when(validadorOS.obraSocialExiste("XYZ")).thenReturn(false);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> servicio.registrarPaciente(cuil, "Ana", "López", domicilio, afiliacion));
        assertTrue(ex.getMessage().toLowerCase().contains("obra social inexistente"));

        verify(repoPacientes, never()).guardarPaciente(any());
        verify(validadorOS).obraSocialExiste("XYZ");
        verify(validadorOS, never()).estaAfiliado(any(), any(), any());
    }

    @Test
    void fallaCuandoPacienteNoEstaAfiliadoALaObraSocial() {
        String cuil = "20111222333";
        Domicilio domicilio = new Domicilio("Belgrano", 777, "San Miguel de Tucumán");
        ObraSocial os = new ObraSocial("PAMI", "PAMI");
        AfiliacionObraSocial afiliacion = new AfiliacionObraSocial(os, "XYZ999");

        when(validadorOS.obraSocialExiste("PAMI")).thenReturn(true);
        when(validadorOS.estaAfiliado(cuil, "PAMI", "XYZ999")).thenReturn(false);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> servicio.registrarPaciente(cuil, "Nora", "Suárez", domicilio, afiliacion));
        assertTrue(ex.getMessage().toLowerCase().contains("no está afiliado"));

        verify(repoPacientes, never()).guardarPaciente(any());
        verify(validadorOS).obraSocialExiste("PAMI");
        verify(validadorOS).estaAfiliado(cuil, "PAMI", "XYZ999");
    }

    @Test
    void fallaCuandoCUIL_esNuloOVacio() {
        Domicilio domicilio = new Domicilio("25 de Mayo", 100, "San Miguel de Tucumán");

        assertThrows(IllegalArgumentException.class,
                () -> servicio.registrarPaciente(null, "Luca", "Rossi", domicilio, null));
        assertThrows(IllegalArgumentException.class,
                () -> servicio.registrarPaciente("  ", "Luca", "Rossi", domicilio, null));

        verify(repoPacientes, never()).guardarPaciente(any());
        verifyNoInteractions(validadorOS);
    }

    @Test
    void fallaCuandoCUIL_tieneFormatoInvalido() {
        String cuilInvalido = "123"; // no cumple 11 dígitos
        Domicilio domicilio = new Domicilio("25 de Mayo", 100, "San Miguel de Tucumán");

        assertThrows(IllegalArgumentException.class,
                () -> servicio.registrarPaciente(cuilInvalido, "Luca", "Rossi", domicilio, null));

        verify(repoPacientes, never()).guardarPaciente(any());
        verifyNoInteractions(validadorOS);
    }

    @Test
    void fallaCuandoNombreOApellidoSonVacios() {
        String cuil = "20123456789";
        Domicilio domicilio = new Domicilio("Mitre", 123, "San Miguel de Tucumán");

        // Nombre inválido
        assertThrows(IllegalArgumentException.class,
                () -> servicio.registrarPaciente(cuil, null, "Pérez", domicilio, null));
        assertThrows(IllegalArgumentException.class,
                () -> servicio.registrarPaciente(cuil, "", "Pérez", domicilio, null));
        assertThrows(IllegalArgumentException.class,
                () -> servicio.registrarPaciente(cuil, "   ", "Pérez", domicilio, null));

        // Apellido inválido
        assertThrows(IllegalArgumentException.class,
                () -> servicio.registrarPaciente(cuil, "Ana", null, domicilio, null));
        assertThrows(IllegalArgumentException.class,
                () -> servicio.registrarPaciente(cuil, "Ana", "", domicilio, null));
        assertThrows(IllegalArgumentException.class,
                () -> servicio.registrarPaciente(cuil, "Ana", "   ", domicilio, null));

        verify(repoPacientes, never()).guardarPaciente(any());
        verifyNoInteractions(validadorOS);
    }

    @Test
    void fallaCuandoDomicilioEsNull() {
        String cuil = "20987654321";

        assertThrows(IllegalArgumentException.class,
                () -> servicio.registrarPaciente(cuil, "Juan", "Gómez", null, null));

        verify(repoPacientes, never()).guardarPaciente(any());
        verifyNoInteractions(validadorOS);
    }


}