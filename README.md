# Ing Software 2025: Proyecto Guardia Triage - BDD con Java

Proyecto para registrar ingresos de pacientes a la sala de urgencias y atenderlos, priorizando por nivel de emergencia y, en empate, por fecha/hora de ingreso. 
Incluye tests BDD (Cucumber) que cubren los criterios de aceptación de la US 001 (moduloUrgencias).
* GRUPO N° 6 - Comisión 4k1 - IS2025

## Requisitos
- SDK: Java Amazon Coretto 22.0.1
- Maven: Maven 3.8+
- IntelliJ IDEA / VS Code (Opcional)

## Ejecutar los tests
En IntelliJ: abrir src/test/resources/moduloUrgencias.feature y ejecutar escenarios desde el editor.

## Principales decisiones de diseño

- VO (Value Objects) para signos vitales: encapsulan validaciones (no negativos, faltantes) y formato de salida.
- Cola de prioridad (ColaAtencion) basada en PriorityQueue, ordenada por:
  - NivelEmergencia (más crítico primero),
  - fechaIngreso (FIFO en empates).
- Fecha/hora del ingreso generada por el sistema en el constructor de Ingreso.
- Estado inicial: todo Ingreso arranca en PENDIENTE.
- Repositorio de pacientes inyectable (RepositorioPacientes) para aislar persistencia (tests usan DBPruebaEnMemoria).

## Criterios de aceptación cubiertos (resumen)

- Ingreso con paciente existente → entra a la cola según prioridad.
- Paciente no existente → se crea y luego se registra el ingreso.
- Campos mandatorios faltantes → error específico (informe, nivel, FC, FR, TA).
- FC/FR/TA negativos → error específico.
- Desempate por fecha/hora cuando el nivel es igual.
- Enfermera que registra queda asociada al ingreso.
- Fecha/hora generada por el sistema (no manual).
- Estado inicial PENDIENTE.

## Cómo agregar nuevos escenarios BDD

- Editar src/test/resources/moduloUrgencias.feature.
- Si se necesitan nuevos pasos, implementarlos en ModuloUrgenciasStepDefinitions.java.
- Ejecutar cada escenario o feature completa (src/test/resources/moduloUrgencias.feature)

## Convenciones de Gherkin que usamos

- Background con datos comunes (enfermera + pacientes).
- Tablas con encabezados consistentes (Cuil, Informe, Nivel de Emergencia, etc.).
- Mensajes de error textuales y exactos.
- -