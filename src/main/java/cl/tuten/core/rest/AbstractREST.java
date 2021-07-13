package cl.tuten.core.rest;

import cl.tuten.commons.constants.Platform;
import cl.tuten.commons.exception.TutenRESTException;
import cl.tuten.core.bo.TutenAdministrator;
import cl.tuten.core.bo.TutenUserPermission;
import cl.tuten.core.session.TutenAdministratorFacadeLocal;
import cl.tuten.core.session.TutenUserPermissionFacadeLocal;
import cl.tuten.core.utils.Constants;
import cl.tuten.core.utils.Security;
import cl.tuten.core.utils.TutenUtils;
import cl.tuten.core.utils.ValidatorHelper;
import io.jsonwebtoken.Claims;
import java.util.Arrays;
import javax.ejb.EJB;
import javax.json.Json;
import javax.json.JsonObject;
import org.apache.log4j.Logger;

import javax.ws.rs.core.Response;
import ve.zlab.k.KException;
import ve.zlab.k.KExecutor;
import ve.zlab.k.KRow;


/**
 * @author miguel@tuten.cl
 */
public abstract class AbstractREST {
    
    @EJB
    public KExecutor K;
    
    @EJB
    private TutenAdministratorFacadeLocal administratorFacade;

    static final Logger LOGGER = Logger.getLogger(AbstractREST.class);
    public static final String APP_BCK = "APP_BCK";
    public static final String APP_PRO = "APP_PRO";
    public static final String APP_BCK_PRO = "APP_BCK_PRO";
    public static final String BOWEB_PRO="BOWEB_PRO";
    
    public void log(String log) {
        LOGGER.info(AbstractREST.class.getName().concat(": ").concat(log));
    }

    public void log(Exception log) {
        LOGGER.error(AbstractREST.class.getName().concat(": "), log);
    }
    
    public Security authorizeAdministrator(final String token, final String app, String[] permissions) throws KException {
        
        ValidatorHelper.assertNotNull(token, "El Token es obligatorio");
        
        ValidatorHelper.assertContainedIn(app, new String[] {
            APP_BCK, APP_PRO, APP_BCK_PRO
        }, "App no valida");
        
        final KRow object =
            K.
            table("mss_login.tuten_administrator a").
            select(
                "a.user_id",
                "a.role_id",
                "a.username",
                "(SELECT tbu.tenant_id FROM mss_login.tuten_business_unit tbu INNER JOIN mss_login.tuten_user_business_unit tubu on tubu.business_unit_id = tbu.id where tubu.user_id = a.user_id limit 1) AS tenantId"
            ).
            where("a.session_token", token).
            single().
            assertNotNull(Response.Status.FORBIDDEN, "Usuario no autorizado");
        
        K.
        table("mss_login.tuten_role_permission").
        where("role_id", object.getString("role_id")).
        whereIn("permission_id", permissions).
        assertExists(Response.Status.FORBIDDEN, "Usuario no autorizado");

        return new Security(object.getLong("user_id"), object.getUUID("tenantId"), object.getString("username"));
    }

    public void checkNulls(Object... objs) throws TutenRESTException {
        for (Object o : objs) {
            if (o == null) {
                throw new TutenRESTException(Response.Status.BAD_REQUEST, String.valueOf(TutenErrorResponse.ERROR.HAS_NULLS));
            }
        }
    }

    public void checkNullsOrEmptyString(Object... objs) throws TutenRESTException {
        checkNulls(objs);
        for (Object o : objs) {
            if (o instanceof String && ((String) o).trim().isEmpty())
                throw new TutenRESTException(Response.Status.BAD_REQUEST);
        }
    }
    
    /**
     * Función simple para autenticar un administrador Engie, dado su ID, su token y el ID de un permiso.
     * @param userId ID del usuario
     * @param token El token
     * @param app El mítico campo app
     * @param permissionId Debe ser un permiso válido (existente), de otra forma se obtendrá una excepción o enviar un string vacío si no se desea validar un permiso en particular
     * @param adminFacade Facade: Admin
     * @param permissionFacade Facade: Permission
     * @return El admin, si es que pasó todos los filtros
     * @throws TutenRESTException Depende del caso. Por lo general un status 401 en caso de que: no esté autorizado, no se encuentre el usuario, token enviado inválido, app inválida o permiso no otorgado. 
     * Permiso declarado no existente devuelve status 500.
     */
    public TutenAdministrator authAdministrator(Long userId, String token, String app, String permissionId, TutenAdministratorFacadeLocal adminFacade, TutenUserPermissionFacadeLocal permissionFacade) throws TutenRESTException {
        TutenAdministrator admin = adminFacade.find(userId);
        TutenUserPermission permission = permissionFacade.find(permissionId);
        if (admin == null) {
            throw new TutenRESTException(Response.Status.UNAUTHORIZED, String.valueOf(TutenErrorResponse.ERROR.INVALID_USER));
        } else if (!permissionId.isEmpty() && permission == null) {
            throw new TutenRESTException(Response.Status.INTERNAL_SERVER_ERROR, String.valueOf(TutenErrorResponse.ERROR.INVALID_PERMISSION));
        } else if (!app.equals(APP_BCK) && !app.equals(APP_PRO) && !app.equals(APP_BCK_PRO)) {
            throw new TutenRESTException(Response.Status.UNAUTHORIZED, String.valueOf(TutenErrorResponse.ERROR.INVALID_APP));
        } else if (!token.equals(admin.getSessionToken())) {
            throw new TutenRESTException(Response.Status.UNAUTHORIZED, String.valueOf(TutenErrorResponse.ERROR.INVALID_TOKEN));
        } else if (!permissionId.isEmpty() && !admin.getRole().getPermissions().contains(permission)) {
            throw new TutenRESTException(Response.Status.UNAUTHORIZED, String.valueOf(TutenErrorResponse.ERROR.MISSING_PERMISSION));
        }
        return admin;
    }
    /**
     * Función simple para autenticar un administrador Engie, dado su ID, su token y el ID de un permiso.
     * @param userId ID del usuario
     * @param token El token
     * @param app El mítico campo app
     * @param adminFacade Facade: Admin
     * @return El admin, si es que pasó todos los filtros
     * @throws TutenRESTException Depende del caso. Por lo general un status 401 en caso de que: no esté autorizado, no se encuentre el usuario, token enviado inválido, app inválida o permiso no otorgado. 
     * Permiso declarado no existente devuelve status 500.
     */
    public TutenAdministrator authAdministrator(Long userId, String token, String app, TutenAdministratorFacadeLocal adminFacade) throws TutenRESTException {
        TutenAdministrator admin = adminFacade.find(userId);
        if (admin == null) {
            throw new TutenRESTException(Response.Status.UNAUTHORIZED, String.valueOf(TutenErrorResponse.ERROR.INVALID_USER));
        } else if (!app.equals(APP_BCK) && !app.equals(APP_PRO) && !app.equals(APP_BCK_PRO)) {
            throw new TutenRESTException(Response.Status.UNAUTHORIZED, String.valueOf(TutenErrorResponse.ERROR.INVALID_APP));
        } else if (!token.equals(admin.getSessionToken())) {
            throw new TutenRESTException(Response.Status.UNAUTHORIZED, String.valueOf(TutenErrorResponse.ERROR.INVALID_TOKEN));
        }
        return admin;
    }
    public Response responseWithBody(Response.Status status, String log) {
        if (log == null) {
            log = status.toString();
        }
        LOGGER.info(AbstractREST.class.getName().concat(": ").concat(log));
        return Response.status(status).type("text/plain").entity(log).build();
    }

    /**
     * Función simple para autenticar un administrador Engie, dado su ID, su token y uno o más IDs de permisos.
     * @param userId ID del usuario
     * @param token El token
     * @param app El mítico campo app
     * @param permissionId Debe ser un permiso válido (existente), de otra forma se obtendrá una excepción
     * @param adminFacade Facade: Admin
     * @param permissionFacade Facade: Permission
     * @return El admin, si es que pasó todos los filtros
     * @throws TutenRESTException Depende del caso. Por lo general un status 401 en caso de que: no esté autorizado, no se encuentre el usuario, token enviado inválido, app inválida o permiso no otorgado.
     * Permiso declarado no existente devuelve status 500.
     */
    public TutenAdministrator authAdministrator(Long userId, String token, String app, String[] permissionId, TutenAdministratorFacadeLocal adminFacade, TutenUserPermissionFacadeLocal permissionFacade) throws TutenRESTException {
        TutenAdministrator admin = adminFacade.find(userId);
        TutenUserPermission permission = null;
        boolean hasPermission = false;
        for (String p : permissionId) {
            permission = permissionFacade.find(p);
            if (admin.getRole().getPermissions().contains(permission)) {
                hasPermission = true;
            }
        }
        if (admin == null) {
            throw new TutenRESTException(Response.Status.UNAUTHORIZED, String.valueOf(TutenErrorResponse.ERROR.INVALID_USER));
        } else if (permission == null) {
            throw new TutenRESTException(Response.Status.INTERNAL_SERVER_ERROR, String.valueOf(TutenErrorResponse.ERROR.INVALID_PERMISSION));
        } else if (!app.equals(Platform.APP_BCK)
                && !app.equals(Platform.APP_PRO)
                && !app.equals(Platform.APP_BCK_PRO)) {
            throw new TutenRESTException(Response.Status.UNAUTHORIZED, String.valueOf(TutenErrorResponse.ERROR.INVALID_APP));
        } else if (!token.equals(admin.getSessionToken())) {
            throw new TutenRESTException(Response.Status.UNAUTHORIZED, String.valueOf(TutenErrorResponse.ERROR.INVALID_TOKEN));
        } else if (!hasPermission) {
            throw new TutenRESTException(Response.Status.UNAUTHORIZED, String.valueOf(TutenErrorResponse.ERROR.MISSING_PERMISSION));
        }
        return admin;
    }

    public Response exceptionResponse(TutenRESTException ex) {
        String name = ex.getError() != null ? ex.getError().name() : ex.getErrorBlock() != null ? ex.getErrorBlock().name() : null;
        String error = ex.getError() != null ? ex.getError().getValue() : ex.getErrorBlock() != null ? ex.getErrorBlock().getValue() : null;
        return TutenErrorResponse.build(ex.getStatus(), error, name);
    }

    public Response responseWithBodyAndLog(Response.Status status, String log) {
        if (log == null) {
            log = status.toString();
        }
        LOGGER.info(AbstractREST.class.getName().concat(": ").concat(log));
        return Response.status(status).type("text/plain").entity(log).build();
    }
    
    //Métodos de JWT
    
    private void isValidAPP(final String app) throws TutenRESTException {
        if (app == null) {
            throw new TutenRESTException(Response.Status.BAD_REQUEST, "APP es requerida");
        }
        //Añadir otro APP a la lista si es requerido
        if (!Arrays.asList("APP_BCK").contains(app.toUpperCase())) {
            throw new TutenRESTException(Response.Status.BAD_REQUEST, "APP es invalida");
        }
    }
    
    public String checkJWT(final String jwt) throws TutenRESTException {
        return this.checkUserJWT(jwt);

    }

    private String checkUserJWT(final String jwt) throws TutenRESTException {
        if (jwt == null || jwt.trim().equals("")) {
            final JsonObject json = Json.createObjectBuilder()
                    .add("code", "JWT_REQUIRED")
                    .add("message", "El token es requerido")
                    .build();
            throw new TutenRESTException(Response.Status.UNAUTHORIZED, json.toString());
        }


        final Claims claims = TutenUtils.decodeJWT(jwt);
        final String APP = claims.get("APP").toString();
        final String userId = claims.getId();

        this.isValidAPP(APP);
        
        return userId;

    }
    


}
