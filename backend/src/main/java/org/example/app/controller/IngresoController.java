package org.example.app.controller;

import org.example.app.ServicioUrgencias;
import org.example.app.controller.dto.IngresoDTO;
import org.example.app.controller.dto.IngresoFinalizadoDTO;
import org.example.app.controller.dto.IngresoPendienteDTO;
import org.example.domain.Ingreso;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}


