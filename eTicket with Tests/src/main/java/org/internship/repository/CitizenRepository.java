package org.internship.repository;

import jakarta.persistence.EntityManager;
import org.internship.entity.Citizen;

public class CitizenRepository {

    private EntityManager em;

    public CitizenRepository(EntityManager em) {
        this.em = em;
    }

    public void save(Citizen citizen) {
        em.persist(citizen);
    }

    public Citizen findById(Long id) {
        return em.find(Citizen.class, id);
    }

    public void update(Citizen citizen) {
        em.merge(citizen);
    }

    public void delete(Long id) {
        Citizen citizen = em.find(Citizen.class, id);
        if (citizen != null) em.remove(citizen);
    }
}