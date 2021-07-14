package cl.tuten.cases;

import cl.tuten.TutenTestCaseBase;
import cl.tuten.core.bo.TutenAdministrator;
import cl.tuten.core.rest.LoginREST;
import cl.tuten.utils.builders.UserBuilder;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import javax.json.Json;
import javax.ws.rs.core.Response;

/**
 * Casos de pruebas para validar el login de usuario
 * @author Carlos Cercado
 */
public class LoginTest extends TutenTestCaseBase {

    @Test
    public void testSum(){
        final float expectativa = 10;

        final float a = 5;
        final float b = 5;

        final float operacion = a + b;

        Assert.assertEquals(expectativa, operacion, 0);
    }

    /**
    * Prueba inicar sesion con datos invalidos
    * */
    @Test
    public void testLoginWithBadData() throws Exception{
        final LoginREST rest = (LoginREST) container.getContext().lookup("java:global/LoginREST/LoginREST");

        final Response response = rest.login("xxxxxxxx", "xxxxxxxxxx");

        Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());

        final JSONObject json = new JSONObject(response.getEntity().toString());

        Assert.assertEquals("LOGIN_FAIL", json.getString("code"));

    }

    /**
     * Prueba iniciar sesion de un usuario registrado
     * Flujo normal de eventos
     * */
    @Test
    public void testLogin() throws Exception{

        final LoginREST rest = (LoginREST) container.getContext().lookup("java:global/LoginREST/LoginREST");

        final String pass = "miclave";
        final TutenAdministrator user = UserBuilder.getBuilder().withPassword(pass).build(entityManager);

        final Response response = rest.login(user.getUsername(), pass);

        Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    /**
     * Prueba iniciar sesion de un usuario registrado pero inactivo
     * */
    @Test
    public void testLoginUserInactive() throws Exception{
        final LoginREST rest = (LoginREST) container.getContext().lookup("java:global/LoginREST/LoginREST");

        final String pass = "miclave";
        final TutenAdministrator user = UserBuilder.getBuilder().withPassword(pass).disabled().build(entityManager);

        final Response response = rest.login(user.getUsername(), pass);

        Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());

        final JSONObject json = new JSONObject(response.getEntity().toString());

        Assert.assertEquals("USER_DISABLED", json.getString("code"));

    }

    /**
     * Prueba deshabilitar un usuario activo con una sesion que no tiene el permiso.
     * */
    @Test
    public void testDisableUserWithoutPermission() throws Exception{

        final LoginREST rest = (LoginREST) container.getContext().lookup("java:global/LoginREST/LoginREST");

        final TutenAdministrator session = UserBuilder.getBuilder().withUsername("benito").withId(100L).build(entityManager);
        final TutenAdministrator user = UserBuilder.getBuilder().withRole(session.getRole()).build(entityManager);

        final Response response = rest.disable(user.getUsername(), session.getSessionToken());

        Assert.assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), response.getStatus());

        final JSONObject json = new JSONObject(response.getEntity().toString());

        Assert.assertEquals("PERMISSION_REQUIRED", json.getString("code"));

    }

    /**
     * Prueba deshabilitar un usuario activo con una sesion que no tiene el permiso.
     * */
    @Test
    public void testDisableUserWithPermission() throws Exception{
        final LoginREST rest = (LoginREST) container.getContext().lookup("java:global/LoginREST/LoginREST");

        final TutenAdministrator session = UserBuilder.getBuilder().withUsername("benito")
                                            .withPermission("DISABLE_USER").withId(100L).build(entityManager);


        final TutenAdministrator user = UserBuilder.getBuilder().withRole(session.getRole()).build(entityManager);

        final Response response = rest.disable(user.getUsername(), session.getSessionToken());

        Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

    }

    /**
     * Prueba deshabilitar un usuario inactivo con una sesion que no tiene el permiso.
     * */
    @Test
    public void testDisableUserDisableWithPermission() throws Exception{
        final LoginREST rest = (LoginREST) container.getContext().lookup("java:global/LoginREST/LoginREST");

        final TutenAdministrator session = UserBuilder.getBuilder().withUsername("benito")
                .withPermission("DISABLE_USER").withId(100L).build(entityManager);

        final TutenAdministrator user = UserBuilder.getBuilder().withRole(session.getRole()).disabled().build(entityManager);

        final Response response = rest.disable(user.getUsername(), session.getSessionToken());

        Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());

        final JSONObject json = new JSONObject(response.getEntity().toString());

        Assert.assertEquals("USER_DISABLED", json.getString("code"));
    }

}
