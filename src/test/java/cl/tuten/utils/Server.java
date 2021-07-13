package cl.tuten.utils;

import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.junit.rules.ExternalResource;

import javax.ejb.embeddable.EJBContainer;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Servidor embebido para desplegar los web services durante las pruebas.
 * Se le establece una ruta de configuracion.
 * @author Carlos Cercado
 */
public class Server extends ExternalResource{

    public static Logger logger = Logger.getLogger(Server.class.getName());
    public static EJBContainer container;
    private static Server currentInstance;
    public static EntityManagerFactory emf;

    public static Server getServer () {
        if (currentInstance == null) {
            currentInstance = new Server();
        }
        return currentInstance;
    }

    public Server(){
        logger.info("Iniciando servidor embebido.");
        final Map<String, Object> properties = new HashMap<>();

        properties.put(EJBContainer.MODULES, new File("target/LoginREST"));
        properties.put("org.glassfish.ejb.embedded.glassfish.installation.root", "./src/test/glassfish");
        properties.put("org.glassfish.ejb.embedded.glassfish.configuration.file", "./src/test/glassfish/domains/domain1/config/domain.xml");
        container = EJBContainer.createEJBContainer(properties);
        emf = getEntityManagerFactory();

        logger.info("Servidor embebido iniciado.");
    }

    public EntityManagerFactory getEntityManagerFactory(){
        final Map<String, String> props = new HashMap<>();
        props.put(PersistenceUnitProperties.TRANSACTION_TYPE, "RESOURCE_LOCAL");
        props.put(PersistenceUnitProperties.JDBC_USER, "tuten_user");
        props.put(PersistenceUnitProperties.JDBC_PASSWORD, "holatuten123.");
        props.put(PersistenceUnitProperties.JDBC_URL, "jdbc:postgresql://localhost:5432/macarena_test");
        props.put(PersistenceUnitProperties.JDBC_DRIVER, "org.postgresql.Driver");
        props.put(PersistenceUnitProperties.TRANSACTION_TYPE, "RESOURCE_LOCAL");
        return Persistence.createEntityManagerFactory("cl.tuten.core_TutenREST_war_0.1-SNAPSHOTPU", props);
    }

}