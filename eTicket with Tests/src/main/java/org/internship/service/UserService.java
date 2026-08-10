package org.internship.service;

import jakarta.persistence.EntityManager;
import org.internship.entity.User;
import org.internship.repository.UserRepository;

public class UserService {

    private EntityManager em;
    private UserRepository repo;

    public UserService(EntityManager em) {
        this.em = em;
        this.repo = new UserRepository(em);
    }

    public void save(User user) {
        em.getTransaction().begin();
        repo.save(user);
        em.getTransaction().commit();
    }

    public User findById(Long id) {
        return repo.findById(id);
    }

    public void update(User user) {
        em.getTransaction().begin();
        repo.update(user);
        em.getTransaction().commit();
    }

    public void delete(Long id) {
        em.getTransaction().begin();
        repo.delete(id);
        em.getTransaction().commit();
    }
}