package org.internship.service;

import jakarta.persistence.EntityManager;
import org.internship.entity.Vehicle;
import org.internship.repository.VehicleRepository;
import java.util.List;

public class VehicleService {

    private EntityManager em;
    private VehicleRepository repo;

    public VehicleService(EntityManager em) {
        this.em = em;
        this.repo = new VehicleRepository(em);
    }

    public void save(Vehicle vehicle) {
        em.getTransaction().begin();
        repo.save(vehicle);
        em.getTransaction().commit();
        System.out.println("Vehicle registered: " + vehicle.getPlate());
    }

    public Vehicle findById(Long id) {
        return repo.findById(id);
    }

    public void update(Vehicle vehicle) {
        em.getTransaction().begin();
        repo.update(vehicle);
        em.getTransaction().commit();
    }

    public void delete(Long id) {
        em.getTransaction().begin();
        repo.delete(id);
        em.getTransaction().commit();
    }

    public List<Vehicle> findByPlate(String plate) {
        return repo.findByPlate(plate);
    }
}