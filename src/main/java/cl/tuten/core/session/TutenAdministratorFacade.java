package cl.tuten.core.session;

import cl.tuten.commons.exception.TutenRESTException;
import cl.tuten.core.bo.TutenAdministrator;
import cl.tuten.commons.exception.TutenException;
import cl.tuten.core.utils.Token;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonObject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.core.Response;
import java.util.*;
import java.util.logging.Level;

/**
 * @author miguel@tuten.cl
 */
@Stateless
public class TutenAdministratorFacade extends AbstractFacade<TutenAdministrator> implements TutenAdministratorFacadeLocal {


    static final Logger LOGGER = Logger.getLogger(TutenAdministratorFacade.class);
    @PersistenceContext(unitName = "cl.tuten.core_TutenREST_war_0.1-SNAPSHOTPU")
    private EntityManager em;

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public TutenAdministratorFacade() {
        super(TutenAdministrator.class);
    }

    @Override
    public TutenAdministrator login(String emailOrUsername, String password) throws TutenRESTException {
        String hashPassword = Token.getStringHash(password);
        try {
            final TutenAdministrator user = em.createQuery("SELECT o FROM TutenAdministrator o WHERE (o.username = :emailOrUsername OR o.email = :emailOrUsername) AND o.passwordHash = :pass", TutenAdministrator.class)
                    .setParameter("emailOrUsername", emailOrUsername)
                    .setParameter("pass", hashPassword)
                    .getSingleResult();

            if(!user.getActive()){
                LOGGER.error("Usuario no encontrado");
                final JsonObject json = Json.createObjectBuilder()
                        .add("code", "USER_DISABLED")
                        .add("message", "Usuario esta inactivo")
                        .build();
                throw new TutenRESTException(Response.Status.BAD_REQUEST, json.toString());
            }

            return user;
        } catch (NoResultException e) {
            LOGGER.error("Usuario no encontrado");
            final JsonObject json = Json.createObjectBuilder()
                    .add("code", "LOGIN_FAIL")
                    .add("message", "Datos incorrectos")
                    .build();
            throw new TutenRESTException(Response.Status.BAD_REQUEST, json.toString());
        } catch(TutenRESTException e){
            throw e;
        }catch (Exception e) {
            LOGGER.error("Error al logera el usuario");
            final JsonObject json = Json.createObjectBuilder()
                    .add("code", "INTERNAL_ERROR")
                    .add("message", "Error interno, contactar administrador")
                    .build();
            throw new TutenRESTException(Response.Status.INTERNAL_SERVER_ERROR, json.toString());
        }
    }

    @Override
    public TutenAdministrator checkUserOrEmail(String emailOrUsername) throws TutenRESTException {
        TutenAdministrator a;
        try {
            a = em.createQuery("SELECT o FROM TutenAdministrator o WHERE o.username = :emailOrUsername OR o.email = :emailOrUsername", TutenAdministrator.class)
                    .setParameter("emailOrUsername", emailOrUsername).getSingleResult();
        } catch (NoResultException e) {
            final JsonObject json = Json.createObjectBuilder()
                    .add("code", "DATA_INVALID")
                    .add("message", "El usuario no existe, por favor comuníquese con su supervisor o administrador.")
                    .build();
            throw new TutenRESTException(Response.Status.BAD_REQUEST, json.toString());
        }
        if (a.getActive() != null && !a.getActive()) {
            final JsonObject json = Json.createObjectBuilder()
                    .add("code", "USER_DISABLED")
                    .add("message", "El usuario esta inactivo.")
                    .build();
            throw new TutenRESTException(Response.Status.BAD_REQUEST, json.toString());
        }
        return a;
    }

    @Override
    public TutenAdministrator loginThree(String emailOrUsername, String password) throws TutenRESTException {
        String hashPassword = Token.getStringHash(password);
        try {
            final TutenAdministrator user = em.createQuery("SELECT o FROM TutenAdministrator o WHERE (o.username = :emailOrUsername OR o.email = :emailOrUsername) AND o.passwordHash = :pass AND o.role.roleId = :roleId", TutenAdministrator.class)
                    .setParameter("emailOrUsername", emailOrUsername)
                    .setParameter("pass", hashPassword)
                    .setParameter("roleId", 3L)
                    .getSingleResult();
            return user;
        } catch (NoResultException e) {
            LOGGER.error("Usuario no encontrado");
            final JsonObject json = Json.createObjectBuilder()
                    .add("code", "LOGIN_FAIL")
                    .add("message", "Datos incorrectos")
                    .build();
            throw new TutenRESTException(Response.Status.BAD_REQUEST, json.toString());
        }catch (Exception e) {
            LOGGER.error("Error al logera el usuario");
            final JsonObject json = Json.createObjectBuilder()
                    .add("code", "INTERNAL_ERROR")
                    .add("message", "Error interno, contactar administrador")
                    .build();
            throw new TutenRESTException(Response.Status.INTERNAL_SERVER_ERROR, json.toString());
        }
    }
}
