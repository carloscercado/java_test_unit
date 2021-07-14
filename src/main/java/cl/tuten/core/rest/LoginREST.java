package cl.tuten.core.rest;

import cl.tuten.commons.exception.TutenRESTException;
import cl.tuten.core.bo.*;
import cl.tuten.core.session.*;
import cl.tuten.core.utils.*;
import com.wordnik.swagger.annotations.*;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;

import io.jsonwebtoken.Claims;


@Stateless
@Path("/login")
@Api(value = "/login", description = "Login API Api Rest")
public class LoginREST extends AbstractREST {

    @EJB
    private TutenAdministratorFacadeLocal administratorFacade;

    @EJB
    private TutenUserRoleFacadeLocal userRoleFacade;

    @POST
    @Path("/{emailOrUsername}")
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "login: validates las credenciales y asigna token de acceso")
    public Response login(
            @PathParam("emailOrUsername") String emailOrUsername,
            @HeaderParam("password") String password) {

        try {
            final TutenAdministrator admin = administratorFacade.login(emailOrUsername.toLowerCase(), password);

            if(!admin.getRole().getActive()){
                final JsonObject json = Json.createObjectBuilder()
                        .add("code", "ROLE_DISABLED")
                        .add("message", "Rol esta inactivo")
                        .build();
                throw new TutenRESTException(Response.Status.BAD_REQUEST, json.toString());
            }

            if(admin.getRole().getRoleId().equals("OPERADORES_VENTAS") && admin.getSessionStatus().equals("ACTIVA")){
                final JsonObject json = Json.createObjectBuilder()
                        .add("code", "SESSION_DUPLICATED")
                        .add("message", "Rol esta inactivo")
                        .build();
                throw new TutenRESTException(Response.Status.BAD_REQUEST, json.toString());
            }
            admin.setSessionToken(TutenUtils.createJWT(admin, 15L));

            admin.setLastestActivityDate(new Date());
            admin.setSessionStatus("ACTIVA"); //la sesion del usuario es activa

            administratorFacade.edit(admin);

            return ResponseHelper.create().
                            add("username", admin.getUsername()).
                            add("userId", admin.getUserId()).
                            add("email", admin.getEmail()).
                            add("firstName", admin.getFirstName()).
                            add("lastName", admin.getLastName()).
                            add("sessionToken", admin.getSessionToken()).
                            add("active", admin.getActive()).
                            toResponse(Response.Status.OK);
        }catch (TutenRESTException ex) {
            //Logger.getLogger(LoginREST.class.getName()).log(Level.SEVERE, "Usuario no autorizado");
            return Response.status(ex.getStatus()).entity(ex.getMessage()).build();
        }
    }

    @PUT
    @Path("/user/{username}/disable")
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Deshabilita a un usuario")
    public Response disable(@PathParam("username") String username,
            @HeaderParam("token") String token) {
        try {
            final String PERMISSION = "DISABLE_USER";

            final Claims claims = TutenUtils.decodeJWT(token);

            final String ROLE = claims.get("ROLE").toString();

            userRoleFacade.hasPermission(ROLE, PERMISSION);

            final TutenAdministrator data = administratorFacade.checkUserOrEmail(username.toLowerCase());

            if(!data.getActive()){
                final JsonObject json = Json.createObjectBuilder()
                        .add("code", "USER_DISABLED")
                        .add("message", "Usuario esta inactivo")
                        .build();
                throw new TutenRESTException(Response.Status.BAD_REQUEST, json.toString());
            }

            data.setActive(Boolean.FALSE);
            administratorFacade.edit(data);
            administratorFacade.getEntityManager().flush();

            return Response.ok().build();

        } catch (TutenRESTException ex) {
            //Logger.getLogger(LoginREST.class.getName()).log(Level.SEVERE, null, ex);

            return Response.status(ex.getStatus()).entity(ex.getMessage()).build();
        }
    }


}
