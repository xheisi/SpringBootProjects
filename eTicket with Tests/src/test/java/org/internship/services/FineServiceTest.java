package org.internship.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import org.internship.entity.Fine;
import org.internship.entity.FineStatus;
import org.internship.entity.Payment;
import org.internship.entity.Vehicle;
import org.internship.service.FineService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * FineService builds its own FineRepository/PaymentRepository internally
 * (new FineRepository(em)), so there is nothing to @InjectMocks: instead
 * we mock the EntityManager itself and let the real repositories delegate
 * to it. That way both the service AND repository logic are exercised.
 */
@ExtendWith(MockitoExtension.class)
class FineServiceTest {

    @Mock
    private EntityManager em;

    @Mock
    private EntityTransaction transaction;

    private FineService fineService;

    @BeforeEach
    void setUp() {
        fineService = new FineService(em);
    }

    // ---------- issueFine ----------

    @Test
    void issueFine_nullVehicle_doesNotPersist() {
        Fine fine = new Fine();
        fine.setVehicle(null);

        fineService.issueFine(fine);

        verify(em, never()).getTransaction();
        verify(em, never()).persist(any());
    }

    @Test
    void issueFine_vehicleDoesNotExist_doesNotPersist() {
        Vehicle vehicle = new Vehicle();
        vehicle.setId(1L);
        Fine fine = new Fine();
        fine.setVehicle(vehicle);
        fine.setAmount(50);
        fine.setReason("Speeding");

        when(em.find(Vehicle.class, 1L)).thenReturn(null);

        fineService.issueFine(fine);

        verify(em, never()).persist(any());
    }

    @Test
    void issueFine_nonPositiveAmount_doesNotPersist() {
        Vehicle vehicle = existingVehicle(1L);
        Fine fine = new Fine();
        fine.setVehicle(vehicle);
        fine.setAmount(0);
        fine.setReason("Speeding");

        when(em.find(Vehicle.class, 1L)).thenReturn(vehicle);

        fineService.issueFine(fine);

        verify(em, never()).persist(any());
    }

    @Test
    void issueFine_emptyReason_doesNotPersist() {
        Vehicle vehicle = existingVehicle(1L);
        Fine fine = new Fine();
        fine.setVehicle(vehicle);
        fine.setAmount(50);
        fine.setReason("");

        when(em.find(Vehicle.class, 1L)).thenReturn(vehicle);

        fineService.issueFine(fine);

        verify(em, never()).persist(any());
    }

    @Test
    void issueFine_reasonTooLong_doesNotPersist() {
        Vehicle vehicle = existingVehicle(1L);
        Fine fine = new Fine();
        fine.setVehicle(vehicle);
        fine.setAmount(50);
        fine.setReason("x".repeat(101));

        when(em.find(Vehicle.class, 1L)).thenReturn(vehicle);

        fineService.issueFine(fine);

        verify(em, never()).persist(any());
    }

    @Test
    void issueFine_validFine_persistsWithinTransaction() {
        Vehicle vehicle = existingVehicle(1L);
        Fine fine = new Fine();
        fine.setVehicle(vehicle);
        fine.setAmount(50);
        fine.setReason("Speeding");

        when(em.find(Vehicle.class, 1L)).thenReturn(vehicle);
        when(em.getTransaction()).thenReturn(transaction);

        fineService.issueFine(fine);

        verify(transaction).begin();
        verify(em).persist(fine);
        verify(transaction).commit();
    }

    // ---------- findById / update / findAll / findByCitizenId / findByPlate ----------

    @Test
    void findById_delegatesToEntityManager() {
        Fine fine = new Fine();
        fine.setId(5L);
        when(em.find(Fine.class, 5L)).thenReturn(fine);

        Fine result = fineService.findById(5L);

        assertSame(fine, result);
    }

    @Test
    void update_mergesWithinTransaction() {
        Fine fine = new Fine();
        fine.setId(5L);
        when(em.getTransaction()).thenReturn(transaction);

        fineService.update(fine);

        verify(transaction).begin();
        verify(em).merge(fine);
        verify(transaction).commit();
    }

    @Test
    void findAll_returnsAllFines() {
        Fine f1 = new Fine();
        Fine f2 = new Fine();
        TypedQuery<Fine> query = mock(TypedQuery.class);
        when(em.createQuery("SELECT f FROM Fine f", Fine.class)).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of(f1, f2));

        List<Fine> result = fineService.findAll();

        assertEquals(2, result.size());
    }

    @Test
    void findByCitizenId_filtersOnCitizen() {
        Fine f1 = new Fine();
        TypedQuery<Fine> query = mock(TypedQuery.class);
        when(em.createQuery(anyString(), eq(Fine.class))).thenReturn(query);
        when(query.setParameter(eq("citizenId"), eq(10L))).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of(f1));

        List<Fine> result = fineService.findByCitizenId(10L);

        assertEquals(1, result.size());
        verify(query).setParameter("citizenId", 10L);
    }

    @Test
    void findByPlate_filtersOnPlate() {
        Fine f1 = new Fine();
        TypedQuery<Fine> query = mock(TypedQuery.class);
        when(em.createQuery(anyString(), eq(Fine.class))).thenReturn(query);
        when(query.setParameter(eq("plate"), eq("AB123CD"))).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of(f1));

        List<Fine> result = fineService.findByPlate("AB123CD");

        assertEquals(1, result.size());
        verify(query).setParameter("plate", "AB123CD");
    }

    // ---------- payFine ----------

    @Test
    void payFine_alreadyPaid_rollsBackAndDoesNotCreatePayment() {
        Fine fine = new Fine();
        fine.setId(1L);
        fine.setStatus(FineStatus.PAID);

        when(em.getTransaction()).thenReturn(transaction);
        when(em.find(Fine.class, 1L)).thenReturn(fine);

        fineService.payFine(1L);

        verify(transaction).begin();
        verify(transaction).rollback();
        verify(transaction, never()).commit();
        verify(em, never()).persist(any(Payment.class));
    }

    @Test
    void payFine_cancelled_rollsBackAndDoesNotCreatePayment() {
        Fine fine = new Fine();
        fine.setId(1L);
        fine.setStatus(FineStatus.CANCELLED);

        when(em.getTransaction()).thenReturn(transaction);
        when(em.find(Fine.class, 1L)).thenReturn(fine);

        fineService.payFine(1L);

        verify(transaction).rollback();
        verify(transaction, never()).commit();
        verify(em, never()).persist(any(Payment.class));
    }

    @Test
    void payFine_unpaid_marksPaidAndCreatesPayment() {
        Fine fine = new Fine();
        fine.setId(1L);
        fine.setStatus(FineStatus.UNPAID);
        fine.setAmount(75);

        when(em.getTransaction()).thenReturn(transaction);
        when(em.find(Fine.class, 1L)).thenReturn(fine);

        fineService.payFine(1L);

        assertEquals(FineStatus.PAID, fine.getStatus());
        verify(em).merge(fine);

        ArgumentCaptor<Payment> paymentCaptor = ArgumentCaptor.forClass(Payment.class);
        verify(em).persist(paymentCaptor.capture());
        Payment savedPayment = paymentCaptor.getValue();
        assertEquals(fine, savedPayment.getFine());
        assertEquals(75, savedPayment.getAmount());
        assertNotNull(savedPayment.getPaymentDate());

        verify(transaction).commit();
        verify(transaction, never()).rollback();
    }

    // ---------- cancelFine ----------

    @Test
    void cancelFine_alreadyPaid_rollsBack() {
        Fine fine = new Fine();
        fine.setId(1L);
        fine.setStatus(FineStatus.PAID);

        when(em.getTransaction()).thenReturn(transaction);
        when(em.find(Fine.class, 1L)).thenReturn(fine);

        fineService.cancelFine(1L);

        verify(transaction).rollback();
        verify(transaction, never()).commit();
        assertEquals(FineStatus.PAID, fine.getStatus());
    }

    @Test
    void cancelFine_alreadyCancelled_rollsBack() {
        Fine fine = new Fine();
        fine.setId(1L);
        fine.setStatus(FineStatus.CANCELLED);

        when(em.getTransaction()).thenReturn(transaction);
        when(em.find(Fine.class, 1L)).thenReturn(fine);

        fineService.cancelFine(1L);

        verify(transaction).rollback();
        verify(transaction, never()).commit();
    }

    @Test
    void cancelFine_unpaid_marksCancelled() {
        Fine fine = new Fine();
        fine.setId(1L);
        fine.setStatus(FineStatus.UNPAID);

        when(em.getTransaction()).thenReturn(transaction);
        when(em.find(Fine.class, 1L)).thenReturn(fine);

        fineService.cancelFine(1L);

        assertEquals(FineStatus.CANCELLED, fine.getStatus());
        verify(em).merge(fine);
        verify(transaction).commit();
        verify(transaction, never()).rollback();
    }

    // ---------- updateReason ----------

    @Test
    void updateReason_emptyReason_doesNotTouchEntityManager() {
        fineService.updateReason(1L, "");

        verify(em, never()).getTransaction();
        verify(em, never()).find(eq(Fine.class), any());
    }

    @Test
    void updateReason_tooLong_doesNotTouchEntityManager() {
        fineService.updateReason(1L, "x".repeat(101));

        verify(em, never()).getTransaction();
    }

    @Test
    void updateReason_valid_updatesReason() {
        Fine fine = new Fine();
        fine.setId(1L);
        fine.setReason("Old reason");

        when(em.getTransaction()).thenReturn(transaction);
        when(em.find(Fine.class, 1L)).thenReturn(fine);

        fineService.updateReason(1L, "New reason");

        assertEquals("New reason", fine.getReason());
        verify(em).merge(fine);
        verify(transaction).commit();
    }

    // ---------- helpers ----------

    private Vehicle existingVehicle(Long id) {
        Vehicle vehicle = new Vehicle();
        vehicle.setId(id);
        vehicle.setPlate("AB123CD");
        return vehicle;
    }
}