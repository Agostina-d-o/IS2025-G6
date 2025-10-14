# Todos los escenarios que defina abajo van a ser relacionados a esta funcionalidad
Feature: Suma de dos numeros

  Scenario: Suma de dos numeros positivos
    #Dado -> Todo lo que se debe preparar antes del escenario
    Given dos numeros enteros positivos
    And continua el given
    #Cuando -> Ejecucion del escenario
    When se suman los numeros
    And continuacion del when
    #Entonces -> Lo que se espera que pase luego del cuando
    Then el resultado es un numero entero positivo
    And continuacion del then

  Scenario: Suma de dos numeros negativos
    Given dos numeros enteros negativos
    When se suman los numeros
    Then el resultado es un numero entero negativo

  Scenario: Suma de un numero entero positivo y uno negativo
    Given un numero positivo
    And un numero negativo
    When se suman los numeros
    Then el resultado es una diferencia
    And se mantiene el signo del numero de mayor valor.
