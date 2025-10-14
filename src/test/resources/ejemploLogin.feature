#language:es

Característica: Login de usuarios

Antecedentes:
    Dado que existen los siguientes usuarios en el sistema:
      |email|password|
      |ejemplo@ejemplo.com|1234|
      |ejemplo2@ejemplo.com|5678|

  Escenario: Login correcto
    Cuando El usuario inicia sesion con las siguientes credenciales:
      |      email        |password|
      |ejemplo2@ejemplo.com|1234    |
    Entonces inicio sesion exitosamente

  Escenario: Login Incorrecto:
    Cuando El usuario inicia sesion con las siguientes credenciales:
      |      email        |password|
      |ejemplo2@ejemplo.com|1234    |
    Entonces El usuario no puede iniciar sesion

