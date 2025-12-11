package org.example.app.controller;

import org.example.app.ServicioUrgencias;
import org.example.app.controller.dto.IngresoDTO;
import org.example.app.controller.dto.IngresoFinalizadoDTO;
import org.example.app.controller.dto.IngresoPendienteDTO;
import org.example.app.controller.dto.AutoridadDTO;
import org.example.domain.Autoridad;
import org.example.domain.Ingreso;
import org.example.domain.Medico;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/urgencias")
public class IngresoController {

    private final ServicioUrgencias servicioUrgencias;

    public IngresoController(ServicioUrgencias servicioUrgencias) {
        this.servicioUrgencias = servicioUrgencias;
    }

    @PostMapping("/ingresos")
    public ResponseEntity<?> registrarIngreso(@RequestBody IngresoDTO dto) {

        if (dto.autoridad != Autoridad.ENFERMERA) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", "No tiene permiso para registrar urgencias"));
        }

        servicioUrgencias.registrarUrgencia(
                dto.cuilPaciente,
                dto.getEnfermera(),
                dto.informe,
                dto.getNivel(),
                dto.temperatura,
                dto.frecuenciaCardiaca,
                dto.frecuenciaRespiratoria,
                dto.presionSistolica,
                dto.presionDiastolica
        );

        return ResponseEntity.ok(Map.of("message", "Ingreso registrado con éxito"));
    }


    @GetMapping("/pendientes")
    public List<IngresoPendienteDTO> obtenerPendientes() {
        var lista = servicioUrgencias.obtenerIngresosPendientes();
        return java.util.stream.IntStream.range(0, lista.size())
                .mapToObj(i -> IngresoPendienteDTO.from(lista.get(i), i + 1))
                .toList();
    }

    @GetMapping("/en-proceso")
    public List<IngresoPendienteDTO> obtenerEnProceso() {
        var lista = servicioUrgencias.obtenerIngresosEnProceso();
        return java.util.stream.IntStream.range(0, lista.size())
                .mapToObj(i -> IngresoPendienteDTO.from(lista.get(i), i + 1))
                .toList();
    }

    @GetMapping("/finalizados")
    public List<IngresoFinalizadoDTO> obtenerFinalizados() {
        List<Ingreso> lista = servicioUrgencias.obtenerIngresosFinalizados();
        return lista.stream().map(IngresoFinalizadoDTO::from).toList();
    }

    @PostMapping("/atender")
    public ResponseEntity<Map<String, Object>> atenderProximo(@RequestBody AutoridadDTO dto) {

        if (dto.autoridad() != Autoridad.MEDICO) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", "No tiene permiso para reclamar pacientes"));
        }

        Ingreso ingreso = servicioUrgencias.atenderProximoPaciente();

        Map<String, Object> body = new HashMap<>();
        body.put("id", ingreso.getId());
        body.put("mensaje", "Ingreso puesto EN_PROCESO");

        return ResponseEntity.ok(body);
    }


    record FinalizarIngresoRequest(
            long idIngreso,
            String diagnostico,
            String nombreMedico,
            String apellidoMedico,
            String emailMedico,
            String matriculaMedico,
            Autoridad autoridad
    ) {}


    @PostMapping("/finalizar")
    public ResponseEntity<Map<String, Object>> finalizar(@RequestBody FinalizarIngresoRequest request) {

        if (request.autoridad() != Autoridad.MEDICO) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", "No tiene permiso para finalizar ingresos"));
        }

        Medico medico = new Medico(
                null,
                request.nombreMedico(),
                request.apellidoMedico(),
                request.emailMedico(),
                request.matriculaMedico()
        );

        Ingreso ingreso = servicioUrgencias.finalizarIngreso(
                request.idIngreso(),
                request.diagnostico(),
                medico
        );

        Map<String, Object> body = new HashMap<>();
        body.put("id", ingreso.getId());
        body.put("mensaje", "Ingreso FINALIZADO");

        return ResponseEntity.ok(body);
    }



    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, String>> handleIllegalState(IllegalStateException ex) {
        return ResponseEntity
                .badRequest()
                .body(Map.of("message", ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity
                .badRequest()
                .body(Map.of("message", ex.getMessage()));
    }
}


