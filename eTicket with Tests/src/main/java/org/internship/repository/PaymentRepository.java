package org.internship.repository;

import jakarta.persistence.EntityManager;
import org.internship.entity.Payment;

public class PaymentRepository {

    private EntityManager em;

    public PaymentRepository(EntityManager em) {
        this.em = em;
    }

    public void save(Payment payment) {
        em.persist(payment);
    }

    public Payment findById(Long id) {
        return em.find(Payment.class, id);
    }

    public void update(Payment payment) {
        em.merge(payment);
    }

    public void delete(Long id) {
        Payment payment = em.find(Payment.class, id);
        if (payment != null) em.remove(payment);
    }
}