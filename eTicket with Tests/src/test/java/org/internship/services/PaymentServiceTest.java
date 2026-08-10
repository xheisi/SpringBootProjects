package org.internship.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.internship.entity.Payment;
import org.internship.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private EntityManager em;

    @Mock
    private EntityTransaction transaction;

    private PaymentService paymentService;

    @BeforeEach
    void setUp() {
        paymentService = new PaymentService(em);
    }

    @Test
    void save_persistsWithinTransaction() {
        Payment payment = new Payment();
        payment.setAmount(100);
        payment.setPaymentDate(LocalDate.now());

        when(em.getTransaction()).thenReturn(transaction);

        paymentService.save(payment);

        verify(transaction).begin();
        verify(em).persist(payment);
        verify(transaction).commit();
    }

    @Test
    void findById_delegatesToEntityManager() {
        Payment payment = new Payment();
        payment.setId(3L);
        when(em.find(Payment.class, 3L)).thenReturn(payment);

        Payment result = paymentService.findById(3L);

        assertSame(payment, result);
    }
}