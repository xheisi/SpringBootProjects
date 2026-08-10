package org.internship.service;

import jakarta.persistence.EntityManager;
import org.internship.entity.Citizen;
import org.internship.repository.CitizenRepository;

public class CitizenService {

    private EntityManager em;
    private CitizenRepository repo;

    public CitizenService(EntityManager em) {
        this.em = em;
        this.repo = new CitizenRepository(em);
    }

    public void save(Citizen citizen) {
        em.getTransaction().begin();
        repo.save(citizen);
        em.getTransaction().commit();
        System.out.println("Citizen added: " + citizen.getName());
    }

    public Citizen findById(Long id) {
        return repo.findById(id);
    }

    public void update(Citizen citizen) {
        em.getTransaction().begin();
        repo.update(citizen);
        em.getTransaction().commit();
    }

    public void delete(Long id) {
        em.getTransaction().begin();
        repo.delete(id);
        em.getTransaction().commit();
    }
}