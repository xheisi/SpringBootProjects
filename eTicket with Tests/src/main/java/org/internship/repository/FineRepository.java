package org.internship.repository;

import jakarta.persistence.EntityManager;
import org.internship.entity.Fine;
import java.util.List;

public class FineRepository {

    private EntityManager em;

    public FineRepository(EntityManager em) {
        this.em = em;
    }

    public void save(Fine fine) {
        em.persist(fine);
    }

    public Fine findById(Long id) {
        return em.find(Fine.class, id);
    }

    public void update(Fine fine) {
        em.merge(fine);
    }

    public void delete(Long id) {
        Fine fine = em.find(Fine.class, id);
        if (fine != null) em.remove(fine);
    }

    public List<Fine> findAll() {
        return em.createQuery("SELECT f FROM Fine f", Fine.class).getResultList();
    }

    public List<Fine> findByCitizenId(Long citizenId) {
        return em.createQuery(
                        "SELECT f FROM Fine f WHERE f.vehicle.citizen.id = :citizenId", Fine.class)
                .setParameter("citizenId", citizenId)
                .getResultList();
    }

    public List<Fine> findByPlate(String plate) {
        return em.createQuery(
                        "SELECT f FROM Fine f WHERE f.vehicle.plate = :plate", Fine.class)
                .setParameter("plate", plate)
                .getResultList();
    }
}