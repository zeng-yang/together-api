package com.zhlzzz.together.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityManager;

@UtilityClass
@Slf4j
public class EntityUtils {

    public static void batchPersist(EntityManager em, Iterable<?> entities) {
        Integer batchSize = null;
        try {
            batchSize = Integer.parseInt((String)em.getEntityManagerFactory().getProperties().get("hibernate.jdbc.batch_size"));
        } catch (NumberFormatException e) {
            // do nothing.
        }

        if (batchSize == null) {
            batchPersist(em, entities, 50);
        } else {
            batchPersist(em, entities, batchSize);
        }
    }

    public static void batchPersist(EntityManager em, Iterable<?> entities, Integer batchSize) {
        int i = 0;
        for (Object entity: entities) {
            em.persist(entity);
            i += 1;
            if (i % batchSize == 0) {
                em.flush();
                em.clear();
            }
        }
    }

    public static <T> boolean isEntitiesEmpty(EntityManager em, Class<T> entityClass) {
        String name = em.getMetamodel().entity(entityClass).getName();
        return em.createQuery("SELECT e FROM " + name + " e", entityClass)
                .setMaxResults(1)
                .getResultList().size() == 0;
    }
}
