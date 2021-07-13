package cl.tuten.core.session;

import cl.tuten.commons.exception.TutenRESTException;
import cl.tuten.core.bo.TutenUserPermission;
import cl.tuten.core.bo.TutenUserRole;

import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonObject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Gus
 */
@Stateless
public class TutenUserRoleFacade extends AbstractFacade<TutenUserRole> implements TutenUserRoleFacadeLocal {

    @PersistenceContext(unitName = "cl.tuten.core_TutenREST_war_0.1-SNAPSHOTPU")
    private EntityManager em;

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public TutenUserRoleFacade() {
        super(TutenUserRole.class);
    }

    static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(TutenUserRoleFacade.class);

    @Override
    public Map<String, List<String>> getAllSimpleMap() {
        List<TutenUserRole> roles;
        try {
            roles = em.createQuery("SELECT r FROM TutenUserRole r WHERE r.active = TRUE ORDER BY r.name ASC")
                    .getResultList();
            Map<String, List<String>> map = new HashMap<>();
            for (TutenUserRole r : roles) {
                if (map.get(r.getRoleId()) == null) {
                    map.put(r.getRoleId(), new ArrayList<>());
                }
                for (TutenUserPermission p : r.getPermissions()) {
                    map.get(r.getRoleId()).add(p.getPermissionId());
                }
            }
            return map;
        } catch (Exception e) {
            LOGGER.error("Ocurrió un error al obtener los roles de usuario");
            return null;
        }
    }

    @Override
    public List<TutenUserRole> getAll() {
        List<TutenUserRole> permissions;
        try {
            permissions = em.createQuery("SELECT r FROM TutenUserRole r WHERE r.active = TRUE ORDER BY r.roleId, r.name ASC")
                    .getResultList();
            return permissions;
        } catch (Exception e) {
            LOGGER.error("Ocurrió un error al obtener todos los roles de usuario");
            return null;
        }
    }

    @Override
    public Long getUserCount(TutenUserRole role) {
        try {
            Long count = (Long) em.createQuery("SELECT COUNT(a) FROM TutenAdministrator a WHERE a.active = TRUE AND a.role = :role")
                    .setParameter("role", role)
                    .getSingleResult();
            return count;
        } catch (Exception e) {
            LOGGER.error("Error al obtener conteo de usuarios" + e);
            return null;
        }
    }

    @Override
    public TutenUserRole getByName(String name, String id) {
        try {
            TutenUserRole role = (TutenUserRole) em.createQuery("SELECT r FROM TutenUserRole r WHERE LOWER(r.name) = LOWER(:name) AND r.roleId <> :id")
                    .setParameter("name", name)
                    .setParameter("id", id)
                    .getSingleResult();
            return role;
        } catch (NoResultException e) {
            LOGGER.error("No se obtuve ningun resultado");
            return null;
        } catch (Exception e) {
            LOGGER.error("Error al obtener rol por nombre", e);
            return null;
        }
    }

    @Override
    public void hasPermission(final String role, final String permission) throws TutenRESTException {
        try {
            em.createQuery("SELECT r FROM TutenUserRole r JOIN r.permissions perms " +
                    "                       WHERE UPPER(perms.permissionId) = UPPER(:permission) AND UPPER(r.roleId) = UPPER(:role)")
                    .setParameter("permission", permission)
                    .setParameter("role", role)
                    .getSingleResult();
        } catch (NoResultException e) {
            final JsonObject json = Json.createObjectBuilder()
                    .add("code", "PERMISSION_REQUIRED")
                    .add("message", "no tiene el permiso necesario para esta operacion")
                    .build();
            throw new TutenRESTException(Response.Status.UNAUTHORIZED, json.toString());
        } catch (Exception e) {
            LOGGER.error("Error al obtener rol por nombre", e);
            final JsonObject json = Json.createObjectBuilder()
                    .add("code", "INTERNAL_ERROR")
                    .add("message", "Error interno, contactar administrador")
                    .build();
            throw new TutenRESTException(Response.Status.INTERNAL_SERVER_ERROR, json.toString());
        }
    }



}
