package org.internship.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class EntityManagerUtil {

    private static EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("ticket-fine");

    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public static void shutdown() {
        emf.close();
    }
}