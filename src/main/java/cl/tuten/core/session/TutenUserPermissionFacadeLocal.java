package cl.tuten.core.session;

import cl.tuten.core.bo.TutenUserPermission;

import javax.ejb.Local;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Gus
 */
@Local
public interface TutenUserPermissionFacadeLocal {

    void create(TutenUserPermission tutenQuotationItem);

    void edit(TutenUserPermission tutenQuotationItem);

    void remove(TutenUserPermission tutenQuotationItem);

    TutenUserPermission find(Object id);

    List<TutenUserPermission> findAll();

    List<TutenUserPermission> findRange(int[] range);

    int count();
    
    Map<String, List<TutenUserPermission>> getAllMap();
    
    List<TutenUserPermission> getAll();
}
