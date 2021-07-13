package cl.tuten.core.rest;

import cl.tuten.commons.exception.TutenRESTException;

import cl.tuten.core.session.APITerceroFacadeLocal;
import cl.tuten.core.session.TutenUserRoleFacadeLocal;
import cl.tuten.core.utils.TutenUtils;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import io.jsonwebtoken.Claims;
import org.json.JSONArray;
import org.json.JSONObject;
import ve.zlab.k.KException;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
@Path("/comments")
@Api(value = "/comments", description = "Comments API")
public class CommentsREST extends AbstractREST{

    public static Logger logger = Logger.getLogger(CommentsREST.class.getName());

    @EJB
    private TutenUserRoleFacadeLocal userRoleFacade;

    @EJB
    public APITerceroFacadeLocal apiTerceroFacade;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Guardar un recurso en el API de tercero, y valida los datos")
    public Response post(@HeaderParam("token") String token, @HeaderParam("title") String title,
                         @HeaderParam("body") String body) throws KException {

        try {

            final String PERMISSION = "POST_RESOURCE";

            final Claims claims = TutenUtils.decodeJWT(token);

            final String ROLE = claims.get("ROLE").toString();

            userRoleFacade.hasPermission(ROLE, PERMISSION);

            final JSONObject json = apiTerceroFacade.createResource(title, body);

            // logica de negocio
            final Integer idUser = json.getInt("id");

            logger.log(Level.INFO, "SE GUARDO EN EL API DE TERCERO UN POST CON ID="+idUser);

            // guardar en base de datos con este objeto idUser
            // validar informacion con este objeto idUser
            return Response.ok(json).build();
        }catch (TutenRESTException ex) {
            return Response.status(ex.getStatus()).entity(ex.getMessage()).build();
        }
    }

}
