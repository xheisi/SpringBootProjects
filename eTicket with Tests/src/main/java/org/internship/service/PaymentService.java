package org.internship.service;

import jakarta.persistence.EntityManager;
import org.internship.entity.Payment;
import org.internship.repository.PaymentRepository;

public class PaymentService {

    private EntityManager em;
    private PaymentRepository repo;

    public PaymentService(EntityManager em) {
        this.em = em;
        this.repo = new PaymentRepository(em);
    }

    public void save(Payment payment) {
        em.getTransaction().begin();
        repo.save(payment);
        em.getTransaction().commit();
    }

    public Payment findById(Long id) {
        return repo.findById(id);
    }
}