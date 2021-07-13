package cl.tuten.core.session;

import cl.tuten.core.bo.TutenUserPermission;
import org.apache.log4j.Logger;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Gus
 */
@Stateless
public class TutenUserPermissionFacade extends AbstractFacade<TutenUserPermission> implements TutenUserPermissionFacadeLocal {

    @PersistenceContext(unitName = "cl.tuten.core_TutenREST_war_0.1-SNAPSHOTPU")
    private EntityManager em;
    static final Logger LOGGER = Logger.getLogger(TutenAdministratorFacade.class);

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public TutenUserPermissionFacade() {
        super(TutenUserPermission.class);
    }
    
    @Override
    public Map<String, List<TutenUserPermission>> getAllMap() {
        List<TutenUserPermission> permissions;
        try {
            permissions = em.createQuery("SELECT p FROM TutenUserPermission p ORDER BY p.description ASC")
                    .getResultList();
            Map<String, List<TutenUserPermission>> map = new HashMap<>();
            for (TutenUserPermission p : permissions) {
                if (map.get(p.getDescription()) == null) {
                    map.put(p.getDescription(), new ArrayList<>());
                }
                map.get(p.getDescription()).add(p);
            }
            return map;
        } catch (Exception e) {
            LOGGER.error("Ocurrió un error al obtener el mapa de permisos de usuario");
            return null;
        }
    }
    
    @Override
    public List<TutenUserPermission> getAll() {
        List<TutenUserPermission> permissions;
        try {
            permissions = em.createQuery("SELECT p FROM TutenUserPermission p ORDER BY p.permissionId, p.description ASC")
                    .getResultList();
            return permissions;
        } catch (Exception e) {
            LOGGER.error("Ocurrió un error al obtener los permisos de usuario");
            return null;
        }
    }
    
    
}
