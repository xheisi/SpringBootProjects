package org.internship.repository;

import jakarta.persistence.EntityManager;
import org.internship.entity.User;

public class UserRepository {

    private EntityManager em;

    public UserRepository(EntityManager em) {
        this.em = em;
    }

    public void save(User user) {
        em.persist(user);
    }

    public User findById(Long id) {
        return em.find(User.class, id);
    }

    public void update(User user) {
        em.merge(user);
    }

    public void delete(Long id) {
        User user = em.find(User.class, id);
        if (user != null) em.remove(user);
    }
}