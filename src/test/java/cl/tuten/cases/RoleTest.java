package cl.tuten.cases;

import cl.tuten.TutenTestCaseBase;
import cl.tuten.core.bo.TutenAdministrator;
import cl.tuten.core.bo.TutenUserRole;
import cl.tuten.core.rest.LoginREST;
import cl.tuten.core.rest.RoleREST;
import cl.tuten.core.rest.dto.RoleDTO;
import cl.tuten.utils.builders.RoleBuilder;
import cl.tuten.utils.builders.UserBuilder;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import javax.ws.rs.core.Response;

/**
 * Casos de pruebas para validar el recurso de rol
 * @author Carlos Cercado
 */
public class RoleTest extends TutenTestCaseBase {


    /**
     * REGLAS DE NEGOCIO / CRITERIOS DE ACEPTACION
     * REGLA 1: El nombre del rol siempre debe estar en mayuscula y debe ser menor de 15 caracteres.
     * REGLA 2: Solo pueden iniciar sesion los usuarios cuyo rol esta activo.
     * REGLA 3: EL ROL OPERADORES_VENTAS solo puede tener una sesion abierta.
     * */




    /**
     * Prueba crear un role
     * Flujo normal de eventos
     * validar que devuelva 201 dado los datos correctos.
     * */
    @Test
    public void testCreateRole() throws Exception{
        final RoleREST rest = (RoleREST) container.getContext().lookup("java:global/LoginREST/RoleREST");

        final RoleDTO dto = new RoleDTO();
        dto.setId("ROLE_MEGAMAN");
        dto.setName("SUPERADMIN");
        final Response response = rest.post("", dto);

        Assert.assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());

    }


    /**
     * Prueba crear un rol con el nombre mayor de 15 caracteres.
     * Validar que el nombre venga en Mayuscula.
     * Validar que devuelva 400 y con el codigo correspondiente
     * */
    @Test
    public void testCreateBadRole() throws Exception{
        final RoleREST rest = (RoleREST) container.getContext().lookup("java:global/LoginREST/RoleREST");

        final RoleDTO dto = new RoleDTO();
        dto.setId("ROLE_MEGAMAN");
        dto.setName("SUPERADMIN CON NOMBRE SUPER LARGO MUY MUY LARGO");
        final Response response = rest.post("", dto);

        Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());

        final JSONObject json = new JSONObject(response.getEntity().toString());

        Assert.assertEquals("BAD_DATA", json.getString("code"));

    }


    /**
    *  Prueba iniciar sesion con un usuario que tiene el rol inactivo.
     *  Validar que no se pueda iniciar sesion
     *  Validar codigo http 400 y con el codigo correspondiente
    * */
    @Test
    public void testLoginWithRoleDisabled() throws Exception{
        final LoginREST rest = (LoginREST) container.getContext().lookup("java:global/LoginREST/LoginREST");

        final String pass = "miclave";
        final TutenUserRole role = RoleBuilder.getBuilder().disabled().build(entityManager);
        final TutenAdministrator user = UserBuilder.getBuilder().withPassword(pass).withRole(role).build(entityManager);

        final Response response = rest.login(user.getUsername(), pass);

        Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());

        final JSONObject json = new JSONObject(response.getEntity().toString());

        Assert.assertEquals("ROLE_DISABLED", json.getString("code"));

    }

    /**
     * Prueba iniciar sesion de un usuario de rol OPERADORES_VENTAS de forma exitosa.
     * Luego iniciar sesion nuevamente con el mismo usuario.
     * Validar que No debe dejar permitir porque el ROL del usuario solo permite 1 sesion.
     * Validar codigo correspondiente.
     * */
    @Test
    public void testLoginRole() throws Exception{
        final LoginREST rest = (LoginREST) container.getContext().lookup("java:global/LoginREST/LoginREST");

        final TutenUserRole role = RoleBuilder.getBuilder().withRoleID("OPERADORES_VENTAS").multipleSession(Boolean.FALSE).build(entityManager);

        final String pass = "miclave";
        final TutenAdministrator user = UserBuilder.getBuilder().withPassword(pass).logout().withRole(role).build(entityManager);

        final Response response = rest.login(user.getUsername(), pass);

        Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        final Response response2 = rest.login(user.getUsername(), pass);

        Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response2.getStatus());

        final JSONObject json = new JSONObject(response2.getEntity().toString());

        Assert.assertEquals("SESSION_DUPLICATED", json.getString("code"));

    }

}
