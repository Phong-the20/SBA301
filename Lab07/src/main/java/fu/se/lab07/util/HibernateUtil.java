package fu.se.lab07.util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateUtil {

    private static SessionFactory sessionFactory;

    public static synchronized SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                        .configure("hibernate.cfg.xml")
                        .build();
                Metadata metadata = new MetadataSources(registry)
                        .getMetadataBuilder()
                        .build();
                sessionFactory = metadata.getSessionFactoryBuilder().build();
            } catch (Exception ex) {
                System.err.println("Initial SessionFactory creation failed: " + ex.getMessage());
                throw new ExceptionInInitializerError(ex);
            }
        }
        return sessionFactory;
    }

    public static synchronized SessionFactory buildTestSessionFactory() {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure("hibernate-test.cfg.xml")
                .build();
        Metadata metadata = new MetadataSources(registry)
                .getMetadataBuilder()
                .build();
        return metadata.getSessionFactoryBuilder().build();
    }

    public static synchronized void shutdown() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
            sessionFactory = null;
        }
    }
}
