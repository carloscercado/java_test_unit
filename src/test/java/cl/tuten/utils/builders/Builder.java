package cl.tuten.utils.builders;

import javax.persistence.EntityManager;

/**
 * Builder base para la creacion de objetos en base de datos
 * @author Carlos Cercado
 */
public class Builder <T> {

    protected T instance;

    /**
     * Persiste el objeto en base de datos.
     * @author Carlos Cercado
     * @param entityManager entity manager inicializado apuntando a la base de datos de pruebas.
     * @return T una instancia del objeto ya persistente.
     */
    public T build(EntityManager entityManager){
        entityManager.getTransaction().begin();
        entityManager.persist(instance);
        entityManager.flush();
        entityManager.getTransaction().commit();
        return instance;
    }

}
