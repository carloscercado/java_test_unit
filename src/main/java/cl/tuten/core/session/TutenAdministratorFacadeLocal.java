
package cl.tuten.core.session;

import cl.tuten.commons.exception.TutenRESTException;
import cl.tuten.core.bo.TutenAdministrator;
import cl.tuten.commons.exception.TutenException;

import javax.ejb.Local;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.Set;

/**
 *
 * @author miguel@tuten.cl
 */
@Local
public interface TutenAdministratorFacadeLocal {
    
    EntityManager getEntityManager();

    void create(TutenAdministrator tutenAdministrator);

    void edit(TutenAdministrator tutenAdministrator);

    void remove(TutenAdministrator tutenAdministrator);

    TutenAdministrator find(Object id);

    List<TutenAdministrator> findAll();

    List<TutenAdministrator> findRange(int[] range);
    
    int count();
    
    TutenAdministrator login(String emailOrUsername, String password) throws TutenRESTException;

    TutenAdministrator loginThree(String emailOrUsername, String password) throws TutenRESTException;

    TutenAdministrator checkUserOrEmail(String emailOrUsername) throws TutenRESTException;

}
