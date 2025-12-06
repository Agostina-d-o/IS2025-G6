Feature: Modulo de Urgencias
  Esta feature esta relacionada al registro de ingresos de pacientes en la sala de urgencias
  respetando su nivel de prioridad y el horario de llegada.

  Background:
    Given que la siguiente enfermera esta registrada:
    |Nombre|Apellido|
    |Susana|Gimenez |
    And que estan registrados los siguientes pacientes:
      | Cuil        | Apellido     | Nombre    | Obra Social        |
      | 23123456792 | Nunez        | Marcelo   | Subsidio de Salud  |
      | 27456789032 | Dufour       | Alexandra | Swiss Medical      |
      | 23456789922 | Estrella     | Patricio  | Fondo de Bikini SA |
      | 23123456787 | Gomez Rivera | Pablo     | Swiss Medical      |
      | 27111111112 | Fernandez    | Laura     | Osde               |
      | 27455522232 | Lopez        | Daniela   | PAMI               |


  # Ingresar paciente a la cola de atencion
  Scenario: Ingreso del primer paciente a la lista de espera de urgencias

    When Ingresan a urgencias los siguientes pacientes:
      | Cuil         | Informe      | Nivel de Emergencia | Temperatura | Frecuencia Cardiaca | Frecuencia Respiratoria | Tension Arterial |
      | 23123456792 | Tiene dengue | Emergencia          | 38          | 70                  | 15                      | 120/80           |
    Then La lista de espera esta ordenada por cuil de la siguiente manera:
    |23123456792|

  #  Ingresar pacientes ordenados por nivel de emergencia
  Scenario: Ingreso de un paciente de bajo nivel de emergencia y luego otro de alto nivel de emergencia

    When Ingresan a urgencias los siguientes pacientes:
      | Cuil         | Informe         | Nivel de Emergencia | Temperatura | Frecuencia Cardiaca | Frecuencia Respiratoria | Tension Arterial |
      | 23456789922 | Le duele el ojo | Sin Urgencia        | 37          | 70                  | 15                      | 120/80           |
      | 23123456792 | Tiene dengue        | Emergencia          | 38          | 70                  | 15                      | 120/80           |
    Then La lista de espera esta ordenada por cuil de la siguiente manera:
      |23123456792|
      |23456789922|

  #  Ingresar pacientes ordenados por nivel de emergencia y por orden de llegada en niveles iguales
  Scenario: Ingreso un paciente sin urgencia y dos con urgencia

    When Ingresan a urgencias los siguientes pacientes:
      | Cuil         | Informe             | Nivel de Emergencia | Temperatura | Frecuencia Cardiaca | Frecuencia Respiratoria | Tension Arterial |
      | 27456789032 | Le duele la pestana | Sin Urgencia        | 37          | 70                  | 15                      | 120/80           |
      | 23456789922 | Tiene neumonia      | Emergencia          | 37          | 70                  | 15                      | 120/80           |
      | 23123456792 | Tiene dengue        | Emergencia          | 38          | 70                  | 15                      | 120/80           |
    Then La lista de espera esta ordenada por cuil de la siguiente manera:
      |23456789922|
      |23123456792|
      |27456789032|

  # No permitir ingresar FC con valores negativos
  Scenario: Registrar ingreso con valores negativos en Frecuencia Cardíaca

    When Ingresan a urgencias los siguientes pacientes:
      | Cuil        | Informe             | Nivel de Emergencia | Temperatura | Frecuencia Cardiaca | Frecuencia Respiratoria | Tension Arterial |
      | 23123456787 | Le duele la pestana | Sin Urgencia        | 37          | -70                 | 15                      | 120/80           |

    Then el sistema muestra el siguiente error: "La frecuencia cardíaca no puede ser negativa"

  # No permitir ingresar FR con valores negativos
  Scenario: Registrar ingreso con valores negativos en Frecuencia Respiratoria

    When Ingresan a urgencias los siguientes pacientes:
      | Cuil        | Informe             | Nivel de Emergencia | Temperatura | Frecuencia Cardiaca | Frecuencia Respiratoria | Tension Arterial |
      | 23123456787 | Le duele la pestana | Sin Urgencia        | 37          | 70                  | -15                     | 120/80           |

    Then el sistema muestra el siguiente error: "La frecuencia respiratoria no puede ser negativa"

  # Crear paciente si no existe antes de registrar su ingreso
  Scenario: Ingreso de paciente no registrado
    Given que el paciente con los siguientes datos no existe en el sistema:
      | Cuil        | Apellido | Nombre  | Obra Social |
      | 20999999992 | Perez    | Julieta | Prensa      |
    When Ingresan a urgencias los siguientes pacientes:
      | Cuil        | Informe              | Nivel de Emergencia | Temperatura | Frecuencia Cardiaca | Frecuencia Respiratoria | Tension Arterial |
      | 20999999992 | Dolor torácico agudo | Emergencia          | 37          | 77                  | 20                      | 120/80           |
    Then el paciente "20999999992" existe en el sistema
    And La lista de espera esta ordenada por cuil de la siguiente manera:
      |20999999992|


  # Campos obligatorios faltantes (Scenario Outline para cubrir todos)
  Scenario Outline: Rechazar ingreso cuando falta un dato mandatorio

    When Ingresan a urgencias los siguientes pacientes:
      | Cuil         | Informe   | Nivel de Emergencia | Temperatura | Frecuencia Cardiaca | Frecuencia Respiratoria | Tension Arterial |
      | 27111111112 | <Informe> | <Nivel>             | 37          | <FC>                | <FR>                    | <TA>             |
    Then el sistema muestra el siguiente error: "<Mensaje>"
    Examples:
      | Informe                 | Nivel    | FC | FR | TA     | Mensaje                                              |
      |                         | Urgencia | 80 | 18 | 120/80 | Falta el informe del ingreso                         |
      | Dolor abdominal intenso |          | 80 | 18 | 120/80 | Falta el nivel de emergencia                         |
      | Dolor abdominal intenso | Urgencia |    | 18 | 120/80 | Falta la frecuencia cardíaca                         |
      | Dolor abdominal intenso | Urgencia | 80 |    | 120/80 | Falta la frecuencia respiratoria                     |
      | Dolor abdominal intenso | Urgencia | 80 | 18 |        | Falta la tensión arterial o tiene un formato erróneo |
      | Dolor abdominal intenso | Urgencia | 80 | 18 | 120/   | Falta la tensión arterial o tiene un formato erróneo |
      | Dolor abdominal intenso | Urgencia | 80 | 18 | 12080  | Falta la tensión arterial o tiene un formato erróneo |


  # No ingresar tensión arterial con alguna de sus frecuencias negativas
  Scenario Outline: Rechazar TA con valores negativos

    When Ingresan a urgencias los siguientes pacientes:
      | Cuil         | Informe | Nivel de Emergencia | Temperatura | Frecuencia Cardiaca | Frecuencia Respiratoria | Tension Arterial |
      | 23456789922 | Cefalea | Urgencia            | 37          | 75                  | 16                       | <TA>             |
    Then el sistema muestra el siguiente error: "<Mensaje>"

    Examples:
      | TA      | Mensaje                                        |
      | -120/80 | La frecuencia sistólica no puede ser negativa  |
      | 120/-80 | La frecuencia diastólica no puede ser negativa |


  # Paciente ingresado tiene como estado inicial PENDIENTE
  Scenario: Cada nuevo ingreso de un paciente queda con estado PENDIENTE
    When Ingresan a urgencias los siguientes pacientes:
      | Cuil         | Informe                  | Nivel de Emergencia | Temperatura | Frecuencia Cardiaca | Frecuencia Respiratoria | Tension Arterial |
      | 27455522232 | Fiebre hace varias horas | Urgencia            | 38.5        | 85                  | 18                      | 120/80           |
    Then el ingreso del paciente "27455522232" queda registrado en estado "PENDIENTE"


  # Fecha/hora de ingreso la genera el sistema
  Scenario: La fecha y hora del ingreso la define el sistema al registrar
    When Ingresan a urgencias los siguientes pacientes:
      | Cuil | Informe       | Nivel de Emergencia | Temperatura | Frecuencia Cardiaca | Frecuencia Respiratoria | Tension Arterial |
      | 27455522232     | Caída en casa | Urgencia Menor      | 36.9        | 78                  | 15                      | 115/75           |
    Then la fecha y hora del ingreso del paciente "27455522232" fue asignada por el sistema


  Scenario: La enfermera que registra el ingreso queda asociada al mismo
    When Ingresan a urgencias los siguientes pacientes:
      | Cuil         | Informe  | Nivel de Emergencia | Temperatura | Frecuencia Cardiaca | Frecuencia Respiratoria | Tension Arterial |
      | 23123456792 | Control  | Urgencia Menor      | 36.8        | 70                  | 16                      | 110/70           |
    Then el ingreso del paciente "23123456792" fue registrado por la enfermera "Susana Gimenez"


