package org.example.app.controller;

import org.example.app.ServicioUrgencias;
import org.example.app.controller.dto.AtencionDTO;
import org.example.app.controller.dto.IngresoDTO;
import org.example.app.controller.dto.IngresoFinalizadoDTO;
import org.example.app.controller.dto.IngresoPendienteDTO;
import org.example.domain.Ingreso;
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
    public void registrarIngreso(@RequestBody IngresoDTO dto) {
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
    public ResponseEntity<Map<String, Object>> atenderProximo() {
        Ingreso ingreso = servicioUrgencias.atenderProximoPaciente();

        Map<String, Object> body = new HashMap<>();
        body.put("id", ingreso.getId());
        body.put("mensaje", "Ingreso puesto EN_PROCESO");

        return ResponseEntity.ok(body);
    }


    record FinalizarIngresoRequest(long idIngreso, String diagnostico) {}

    @PostMapping("/finalizar")
    public ResponseEntity<Map<String, Object>> finalizar(@RequestBody FinalizarIngresoRequest request) {
        Ingreso ingreso = servicioUrgencias.finalizarIngreso(
                request.idIngreso(),
                request.diagnostico()
        );

        Map<String, Object> body = new HashMap<>();
        body.put("id", ingreso.getId());
        body.put("mensaje", "Ingreso FINALIZADO");

        return ResponseEntity.ok(body);
    }

    @PostMapping("/ingresos/{id}/atencion")
    public ResponseEntity<String> registrarAtencion(@PathVariable Long id, @RequestBody AtencionDTO dto) {
        try {
            servicioUrgencias.registrarAtencion(id, dto);
            return ResponseEntity.ok("Atención registrada correctamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }



    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, String>> handleIllegalState(IllegalStateException ex) {
        return ResponseEntity
                .badRequest()
                .body(Map.of("message", ex.getMessage()));
    }
}


