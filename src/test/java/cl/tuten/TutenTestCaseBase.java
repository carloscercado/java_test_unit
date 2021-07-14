package cl.tuten;

import cl.tuten.core.bo.TutenAdministrator;
import cl.tuten.utils.Server;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;

import javax.ejb.embeddable.EJBContainer;
import javax.persistence.EntityManager;
import java.util.logging.Logger;

/**
 * @author Carlos Cercado
 * Base con logica para reutilizar. Contiene:
 * El contenedor embebido, para desplegar los servicios web
 * EntityManager para acceder a la base de datos de pruebas.
 */
public class TutenTestCaseBase {

    @ClassRule
    public static final Server server = Server.getServer();
    public static EJBContainer container = server.container;
    protected static EntityManager entityManager;
    public static Logger logger = Logger.getLogger(TutenTestCaseBase.class.getName());

    @BeforeClass
    public static void setUpClass() throws Exception{
        logger.info("Se inicia la prueba de una clase");
        entityManager = server.emf.createEntityManager();
    }


    @Before
    public void setUp() {

    }

    /**
     * Al finalizar cada caso de prueba, se elimaran los datos de las siguientes tablas a fin
     * de conseguir que cada caso de prueba este completamente aislado y libre de ruido
     * @author Carlos Cercado
     */
    @After
    public void tearDown() {
        entityManager.getTransaction().begin();

        entityManager.createNativeQuery("DELETE FROM mss_login.tuten_role_permission;").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM mss_login.tuten_user_permission;").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM mss_login.tuten_administrator;").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM mss_login.tuten_user_role;").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM mss_login.tuten_user_role;").executeUpdate();

        entityManager.getTransaction().commit();
    }
}
