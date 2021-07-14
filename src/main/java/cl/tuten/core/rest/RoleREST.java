package cl.tuten.core.rest;

import cl.tuten.commons.exception.TutenRESTException;
import cl.tuten.core.bo.TutenAdministrator;
import cl.tuten.core.bo.TutenUserRole;
import cl.tuten.core.rest.dto.RoleDTO;
import cl.tuten.core.session.TutenAdministratorFacadeLocal;
import cl.tuten.core.session.TutenUserRoleFacadeLocal;
import cl.tuten.core.utils.ResponseHelper;
import cl.tuten.core.utils.TutenUtils;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import io.jsonwebtoken.Claims;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;


@Stateless
@Path("/role")
@Api(value = "/role", description = "Role API Api Rest")
public class RoleREST extends AbstractREST {

    @EJB
    private TutenUserRoleFacadeLocal userRoleFacade;



    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Crea un rol")
    public Response post(@HeaderParam("token") String token, RoleDTO dto) {

        try {

            //validar dto

            if(dto.getName().length() > 15 ){
                final JsonObject json = Json.createObjectBuilder()
                        .add("code", "BAD_DATA")
                        .add("message", "Usuario esta inactivo")
                        .build();
                throw new TutenRESTException(Response.Status.BAD_REQUEST, json.toString());
            }

            final TutenUserRole role = new TutenUserRole();
            role.setRoleId(dto.getId());
            role.setActive(Boolean.TRUE);
            role.setTimeout(0);
            role.setMultipleSessions(Boolean.FALSE);
            role.setName(dto.getName().toUpperCase());
            userRoleFacade.create(role);


            return ResponseHelper.create().
                            add("name", role.getName()).
                            add("id", role.getRoleId()).
                            add("active",role.getActive()).
                            add("timeout", role.getTimeout()).
                            add("multiple", role.getMultipleSessions()).
                            toResponse(Response.Status.CREATED);
        }catch (TutenRESTException ex) {
            //Logger.getLogger(LoginREST.class.getName()).log(Level.SEVERE, "Usuario no autorizado");
            return Response.status(ex.getStatus()).entity(ex.getMessage()).build();
        }
    }


}

