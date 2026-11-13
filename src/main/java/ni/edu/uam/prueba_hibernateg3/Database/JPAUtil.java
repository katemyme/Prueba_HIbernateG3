package ni.edu.uam.prueba_hibernateg3.Database;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.HashMap;
import java.util.Map;


public class JPAUtil {
    // Debe coincidir con <persistence-unit name="..."> de META-INF/persistence.xml
    private static final String PERSISTENCE_UNIT = "bdlaboratorioorm";
    private static EntityManagerFactory entityManagerFactory;

    public static synchronized void iniciar() {
        if (entityManagerFactory == null) {
            String password = System.getenv("DB_PASSWORD");
            if (password == null || password.isBlank()) {
                throw new RuntimeException("La variable de entorno DB_PASSWORD está vacía");
            }
            Map<String, Object> propiedades = new HashMap<>();
            propiedades.put("jakarta.persistence.jdbc.password", password);
            entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT, propiedades);
        }
    }

    public static EntityManager getEntityManager() {
        iniciar();
        return entityManagerFactory.createEntityManager();
    }

    public static synchronized void cerrar() {
        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
            entityManagerFactory = null;
        }
    }
}
