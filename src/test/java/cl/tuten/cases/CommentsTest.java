package cl.tuten.cases;

import cl.tuten.TutenTestCaseBase;
import cl.tuten.core.bo.TutenAdministrator;
import cl.tuten.core.rest.CommentsREST;
import cl.tuten.core.rest.LoginREST;
import cl.tuten.core.session.APITerceroFacade;
import cl.tuten.utils.builders.UserBuilder;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import javax.ws.rs.core.Response;


/**
 * Casos de pruebas para validar la obtencion de comentarios de API externa
 * @author Carlos Cercado
 */
public class CommentsTest extends TutenTestCaseBase {


    /**
     * Prueba Registrar un recurso y validar logica con la respuesta
     * Flujo normal de eventos
     * */
    @Test
    public void testPostResource() throws Exception{


    }


}