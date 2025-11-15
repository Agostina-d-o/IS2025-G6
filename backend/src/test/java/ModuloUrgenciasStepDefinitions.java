import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import mock.DBPruebaEnMemoria;
import org.example.app.ServicioUrgencias;
import org.example.domain.Enfermera;
import org.example.domain.Ingreso;
import org.example.domain.NivelEmergencia;
import org.example.domain.Paciente;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

public class ModuloUrgenciasStepDefinitions {

    private Enfermera enfermera;
    private DBPruebaEnMemoria dbMockeada;
    private ServicioUrgencias servicioUrgencias;
    private Exception excepcionEsperada;
    private LocalDateTime tMarcaRegistro;

    public ModuloUrgenciasStepDefinitions() {
        this.dbMockeada = new DBPruebaEnMemoria();

        this.servicioUrgencias = new ServicioUrgencias(
                dbMockeada,
                new org.example.app.interfaces.ValidadorObraSocial() {
                    @Override
                    public boolean obraSocialExiste(String codigoObraSocial) {
                        return true; // dummy: todo existe
                    }
                    @Override
                    public boolean estaAfiliado(String cuilPaciente, String codigoObraSocial, String numeroAfiliado) {
                        return true; // dummy: siempre afiliado
                    }
                }
        );
    }


    @Given("que la siguiente enfermera esta registrada:")
    public void queLaSiguienteEnfermeraEstaRegistrada(List<Map<String, String>> tabla) {
        String nombre = tabla.getFirst().get("Nombre");
        String apeliido = tabla.getFirst().get("Apellido");

        enfermera = new Enfermera(nombre, apeliido);
    }

    @Given("que estan registrados los siguientes pacientes:")
    public void dadoQueEstanRegistradosLosSiguientesPacientes(List<Map<String, String>> tabla) {
        for(Map<String, String> fila: tabla) {
            String cuil = fila.get("Cuil");
            String nombre = fila.get("Nombre");
            String apellido = fila.get("Apellido");
            var domicilio = new org.example.domain.valueobject.Domicilio("S/D", 1, "San Miguel de Tucumán");

            org.example.domain.Paciente paciente =
                    new org.example.domain.Paciente(cuil, nombre, apellido, domicilio, null);

            dbMockeada.guardarPaciente(paciente);
        }
    }

    @When("Ingresan a urgencias los siguientes pacientes:")
    public void ingresaAUrgenciasElSiguientePaciente(List<Map<String, String>> tabla) {
        excepcionEsperada = null;
        for(Map<String, String> fila: tabla) {
            String cuil = fila.get("Cuil");

            /*String informe = fila.get("Informe");
            NivelEmergencia nivelEmergencia = Arrays.stream(NivelEmergencia.values())
                    .filter(nivel -> nivel.tieneNombre(fila.get("Nivel de Emergencia")))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Nivel desconocido"));
            Float temperatura = Float.parseFloat(fila.get("Temperatura"));
            Float frecuenciaCardiaca = Float.parseFloat(fila.get("Frecuencia Cardiaca"));
            Float frecuenciaRespiratoria = Float.parseFloat(fila.get("Frecuencia Respiratoria"));
            List<Float> tensionArterial = Arrays.stream(fila.get("Tension Arterial").split("/")).map(Float::parseFloat).toList(); */

            String informe = nullIfBlank(fila.get("Informe"));
            String nivelStr = fila.get("Nivel de Emergencia");
            NivelEmergencia nivelEmergencia =
                    (nivelStr == null || nivelStr.isBlank())
                            ? null // faltante
                            : Arrays.stream(NivelEmergencia.values())
                            .filter(nivel -> nivel.tieneNombre(nivelStr))
                            .findFirst()
                            .orElseThrow(() -> new RuntimeException("Nivel desconocido"));
            Float temperatura = parseNullableFloat(fila.get("Temperatura"));
            Float frecuenciaCardiaca = parseNullableFloat(fila.get("Frecuencia Cardiaca"));
            Float frecuenciaRespiratoria = parseNullableFloat(fila.get("Frecuencia Respiratoria"));
            List<Float> tensionArterial;
            String ta = fila.get("Tension Arterial");
            if (ta == null || ta.isBlank()) {
                tensionArterial = Arrays.asList(null, null);
            } else {
                try {
                    String[] p = ta.split("/", 2);
                    tensionArterial = (p.length == 2)
                            ? Arrays.asList(Float.parseFloat(p[0].trim()), Float.parseFloat(p[1].trim()))
                            : Arrays.asList(null, null);       // formato raro -> tratá como faltante
                } catch (NumberFormatException e) {
                    tensionArterial = Arrays.asList(null, null); // números inválidos -> faltante
                }
            }



            try{
                tMarcaRegistro = LocalDateTime.now();
                servicioUrgencias.registrarUrgencia(cuil,enfermera,informe,nivelEmergencia,temperatura,frecuenciaCardiaca,frecuenciaRespiratoria,tensionArterial.get(0), tensionArterial.get(1));
            }catch (Exception e){
                this.excepcionEsperada = e;
                break;
            }
        }
    }

    @Then("La lista de espera esta ordenada por cuil de la siguiente manera:")
    public void laListaDeEsperaEstaOrdenadaPorCuilDeLaSiguienteManera(List<String> lista) {

        List<String> cuilsPendientes = servicioUrgencias.obtenerIngresosPendientes()
                .stream()
                .map(Ingreso::getCuilPaciente)
                .toList();

        assertThat(cuilsPendientes)
                .hasSize(lista.size())
                .containsExactlyElementsOf(lista);

    }


    @Then("el sistema muestra el siguiente error: {string}")
    public void elSistemaMuestraElSiguienteError(String arg0) {
        assertThat(excepcionEsperada)
                .isNotNull()
                .hasMessage(arg0);
    }

    //**** Agregados por Grupo 6 ****

    @Given("que el paciente con los siguientes datos no existe en el sistema:")
    public void queElPacienteConLosSiguientesDatosNoExisteEnElSistema(List<Map<String, String>> tabla) {
        String cuil = tabla.getFirst().get("Cuil");

        assertThat(dbMockeada.buscarPacientePorCuil(cuil)).isEmpty();

    }


    @Then("el paciente {string} existe en el sistema")
    public void elPacienteExisteEnElSistema(String cuil) {
        assertThat(dbMockeada.buscarPacientePorCuil(cuil))
                .as("El paciente %s debería haberse creado antes de registrar la urgencia", cuil)
                .isPresent();
    }



    @Then("el ingreso del paciente {string} queda registrado en estado {string}")
    public void elIngresoDelPacienteQuedaRegistradoEnEstado(String cuil, String estadoEsperado) {
        List<Ingreso> pendientes = servicioUrgencias.obtenerIngresosPendientes();

        // buscar el ingreso por CUIL
        Ingreso ingreso = pendientes.stream()
                .filter(i -> cuil.equals(i.getCuilPaciente()))
                .findFirst()
                .orElse(null);

        assertThat(ingreso)
                .as("No se encontró ingreso para el paciente %s", cuil)
                .isNotNull();

        assertThat(ingreso.getEstado().name())
                .as("Estado del ingreso de %s", cuil)
                .isEqualTo(estadoEsperado);
    }

    @Then("la fecha y hora del ingreso del paciente {string} fue asignada por el sistema")
    public void laFechaYHoraDelIngresoDelPacienteFueAsignadaPorElSistema(String cuil) {
        List<Ingreso> pendientes = servicioUrgencias.obtenerIngresosPendientes();
        Ingreso ingreso = pendientes.stream()
                .filter(i->cuil.equals(i.getCuilPaciente()))
                .findFirst()
                .orElse(null);

        assertThat(ingreso)
                .as("No se encontró ingreso para el paciente %s", cuil)
                .isNotNull();

        assertThat(ingreso.getFechaIngreso())
                .as("La fecha/hora debe ser generada al momento del registro por el sistema")
                .isAfterOrEqualTo(tMarcaRegistro);
    }

    @Then("el ingreso del paciente {string} fue registrado por la enfermera {string}")
    public void elIngresoDelPacienteFueRegistradoPorLaEnfermera(String cuil, String enfermeraEsperada) {
        Ingreso ingreso = servicioUrgencias.obtenerIngresosPendientes().stream()
                .filter(i -> cuil.equals(i.getCuilPaciente()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("No se encontró ingreso para el paciente " + cuil));

        String actual = ingreso.getEnfermera().getNombre() + " " + ingreso.getEnfermera().getApellido();
        assertThat(actual).isEqualTo(enfermeraEsperada);
    }









    // Helpers
    private static String nullIfBlank(String s) {
        return (s == null || s.isBlank()) ? null : s;
    }

    private static Float parseNullableFloat(String s) {
        return (s == null || s.isBlank()) ? null : Float.parseFloat(s);
    }
}

