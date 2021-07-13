
/**
 *
 * @author karol
 */
package cl.tuten.core.k;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import ve.zlab.k.KExecutor;
import ve.zlab.k.KSearch;

@Stateless
public class K extends KSearch implements KExecutor {
    
    @PersistenceContext(unitName = "cl.tuten.core_TutenREST_war_0.1-SNAPSHOTPU")
    private EntityManager entityManager;

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }
}