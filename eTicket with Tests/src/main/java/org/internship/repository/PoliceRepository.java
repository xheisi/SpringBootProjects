package org.internship.repository;

import jakarta.persistence.EntityManager;
import org.internship.entity.Police;

public class PoliceRepository {

    private EntityManager em;

    public PoliceRepository(EntityManager em) {
        this.em = em;
    }

    public void save(Police officer) {
        em.persist(officer);
    }

    public Police findById(Long id) {
        return em.find(Police.class, id);
    }

    public void update(Police officer) {
        em.merge(officer);
    }

    public void delete(Long id) {
        Police officer = em.find(Police.class, id);
        if (officer != null) em.remove(officer);
    }
}