package cl.tuten.cases;

import cl.tuten.TutenTestCaseBase;
import cl.tuten.core.bo.TutenAdministrator;
import cl.tuten.core.rest.LoginREST;
import cl.tuten.utils.builders.UserBuilder;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import javax.ws.rs.core.Response;

/**
 * Casos de pruebas para validar el login de usuario
 * @author Carlos Cercado
 */
public class LoginTest extends TutenTestCaseBase {

    @Test
    public void testSum(){
        final float a = 5;
        final float b = 5;

        final float expected = 10;

        final float operation = a + b;

        Assert.assertEquals(expected, operation, 0);

    }

    /**
    * Prueba inicar sesion con datos invalidos
    * */
    @Test
    public void testLoginWithBadData() throws Exception{
        //final LoginREST rest = (LoginREST) container.getContext().lookup("java:global/LoginREST/LoginREST");


    }

    /**
     * Prueba iniciar sesion de un usuario registrado
     * Flujo normal de eventos
     * */
    @Test
    public void testLogin() throws Exception{

    }

    /**
     * Prueba iniciar sesion de un usuario registrado pero inactivo
     * */
    @Test
    public void testLoginUserInactive() throws Exception{

    }

    /**
     * Prueba deshabilitar un usuario activo con una sesion que no tiene el permiso.
     * */
    @Test
    public void testDisableUserWithoutPermission() throws Exception{

    }

    /**
     * Prueba deshabilitar un usuario activo con una sesion que no tiene el permiso.
     * */
    @Test
    public void testDisableUserWithPermission() throws Exception{

    }

}
