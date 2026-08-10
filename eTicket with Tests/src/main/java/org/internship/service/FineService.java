package org.internship.service;

import jakarta.persistence.EntityManager;
import org.internship.entity.Fine;
import org.internship.entity.FineStatus;
import org.internship.entity.Payment;
import org.internship.entity.Vehicle;
import org.internship.repository.FineRepository;
import org.internship.repository.PaymentRepository;
import java.time.LocalDate;
import java.util.List;

public class FineService {

    private EntityManager em;
    private FineRepository repo;
    private PaymentRepository paymentRepo;

    public FineService(EntityManager em) {
        this.em = em;
        this.repo = new FineRepository(em);
        this.paymentRepo = new PaymentRepository(em);
    }

    public void issueFine(Fine fine) {
        if (fine.getVehicle() == null) {
            System.out.println("Error: Fine must be linked to a vehicle!");
            return;
        }
        Vehicle vehicle = em.find(Vehicle.class, fine.getVehicle().getId());
        if (vehicle == null) {
            System.out.println("Error: Vehicle does not exist!");
            return;
        }
        if (fine.getAmount() <= 0) {
            System.out.println("Error: Fine amount must be positive!");
            return;
        }
        if (fine.getReason() == null || fine.getReason().isEmpty()) {
            System.out.println("Error: Fine must have a reason!");
            return;
        }
        if (fine.getReason().length() > 100) {
            System.out.println("Error: Reason cannot exceed 100 characters!");
            return;
        }
        em.getTransaction().begin();
        repo.save(fine);
        em.getTransaction().commit();
        System.out.println("Fine issued for plate: " + vehicle.getPlate());
    }

    public Fine findById(Long id) {
        return repo.findById(id);
    }

    public void update(Fine fine) {
        em.getTransaction().begin();
        repo.update(fine);
        em.getTransaction().commit();
    }

    public List<Fine> findAll() {
        return repo.findAll();
    }

    public List<Fine> findByCitizenId(Long citizenId) {
        return repo.findByCitizenId(citizenId);
    }

    public List<Fine> findByPlate(String plate) {
        return repo.findByPlate(plate);
    }

    public void payFine(Long fineId) {
        em.getTransaction().begin();
        Fine fine = repo.findById(fineId);
        if (fine.getStatus() == FineStatus.PAID) {
            System.out.println("Error: Fine " + fineId + " is already paid!");
            em.getTransaction().rollback();
            return;
        }
        if (fine.getStatus() == FineStatus.CANCELLED) {
            System.out.println("Error: Cannot pay a cancelled fine!");
            em.getTransaction().rollback();
            return;
        }
        fine.setStatus(FineStatus.PAID);
        repo.update(fine);

        Payment payment = new Payment();
        payment.setFine(fine);
        payment.setAmount(fine.getAmount());
        payment.setPaymentDate(LocalDate.now());
        paymentRepo.save(payment);

        em.getTransaction().commit();
        System.out.println("Fine " + fineId + " paid successfully!");
    }

    public void cancelFine(Long fineId) {
        em.getTransaction().begin();
        Fine fine = repo.findById(fineId);
        if (fine.getStatus() == FineStatus.PAID) {
            System.out.println("Error: Cannot cancel a paid fine!");
            em.getTransaction().rollback();
            return;
        }
        if (fine.getStatus() == FineStatus.CANCELLED) {
            System.out.println("Error: Fine is already cancelled!");
            em.getTransaction().rollback();
            return;
        }
        fine.setStatus(FineStatus.CANCELLED);
        repo.update(fine);
        em.getTransaction().commit();
        System.out.println("Fine " + fineId + " cancelled successfully!");
    }

    public void updateReason(Long fineId, String newReason) {
        if (newReason == null || newReason.isEmpty()) {
            System.out.println("Error: Reason cannot be empty!");
            return;
        }
        if (newReason.length() > 100) {
            System.out.println("Error: Reason cannot exceed 100 characters!");
            return;
        }
        em.getTransaction().begin();
        Fine fine = repo.findById(fineId);
        fine.setReason(newReason);
        repo.update(fine);
        em.getTransaction().commit();
        System.out.println("Fine " + fineId + " reason updated to: " + newReason);
    }
}