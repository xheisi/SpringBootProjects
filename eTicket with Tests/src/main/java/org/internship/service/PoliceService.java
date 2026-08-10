package org.internship.service;

import jakarta.persistence.EntityManager;
import org.internship.entity.Police;
import org.internship.repository.PoliceRepository;

public class PoliceService {

    private EntityManager em;
    private PoliceRepository repo;

    public PoliceService(EntityManager em) {
        this.em = em;
        this.repo = new PoliceRepository(em);
    }

    public void save(Police officer) {
        em.getTransaction().begin();
        repo.save(officer);
        em.getTransaction().commit();
        System.out.println("Officer added: " + officer.getName() + " | Badge: " + officer.getBadgeNumber());
    }

    public Police findById(Long id) {
        return repo.findById(id);
    }

    public void update(Police officer) {
        em.getTransaction().begin();
        repo.update(officer);
        em.getTransaction().commit();
    }

    public void delete(Long id) {
        em.getTransaction().begin();
        repo.delete(id);
        em.getTransaction().commit();
    }
}