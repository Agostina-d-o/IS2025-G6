import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;

import java.util.List;
import java.util.Map;

public class LoginDeUsuariosStepDef {
    @Dado("que existen los siguientes usuarios en el sistema:")
    public void queExistenLosSiguientesUsuariosEnElSistema(List<Map<String,String>> credenciales) {
        
    }

    @Cuando("inicio sesion con el usuario {string}")
    public void inicioSesionConElUsuario(String arg0) {
        
    }

    @Cuando("El usuario inicia sesion con las siguientes credenciales:")
    public void elUsuarioIniciaSesionConLasSiguientesCredenciales(List<Map<String,String>> credenciales) {
        
    }

    @Entonces("inicio sesion exitosamente")
    public void inicioSesionExitosamente() {
        
    }

    @Entonces("El usuario no puede iniciar sesion")
    public void elUsuarioNoPuedeIniciarSesion() {
    }
}
