/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.tuten.core.session;

import cl.tuten.commons.exception.TutenRESTException;
import cl.tuten.core.bo.TutenUserRole;

import javax.ejb.Local;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Gus
 */
@Local
public interface TutenUserRoleFacadeLocal {
    
    EntityManager getEntityManager();

    void create(TutenUserRole tutenQuotationItem);

    void edit(TutenUserRole tutenQuotationItem);

    void remove(TutenUserRole tutenQuotationItem);

    TutenUserRole find(Object id);

    List<TutenUserRole> findAll();

    List<TutenUserRole> findRange(int[] range);

    int count();
    
    List<TutenUserRole> getAll();
    
    Map<String, List<String>> getAllSimpleMap();

    Long getUserCount(TutenUserRole role);
    
    TutenUserRole getByName(String name, String id);

    void hasPermission(final String role, final String permission) throws TutenRESTException;
}
