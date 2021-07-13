package cl.tuten.cases;

import cl.tuten.TutenTestCaseBase;
import org.junit.Assert;
import org.junit.Test;

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

    }


    /**
     * Prueba crear un rol con el nombre mayor de 15 caracteres.
     * Validar que el nombre venga en Mayuscula.
     * Validar que devuelva 400 y con el codigo correspondiente
     * */
    @Test
    public void testCreateBadRole() throws Exception{
        //codigo correspondiente = BAD_DATA

    }


    /**
    *  Prueba iniciar sesion con un usuario que tiene el rol inactivo.
     *  Validar que no se pueda iniciar sesion
     *  Validar codigo http 400 y con el codigo correspondiente
    * */
    @Test
    public void testLoginWithRoleDisabled() throws Exception{
        //codigo correspondiente = ROLE_DISABLED

    }

    /**
     * Prueba iniciar sesion de un usuario de rol OPERADORES_VENTAS de forma exitosa.
     * Luego iniciar sesion nuevamente con el mismo usuario.
     * Validar que No debe dejar permitir porque el ROL del usuario solo permite 1 sesion.
     * Validar codigo correspondiente.
     * */
    @Test
    public void testLoginRole() throws Exception{
        //codigo correspondiente = SESSION_DUPLICATED

    }

}
