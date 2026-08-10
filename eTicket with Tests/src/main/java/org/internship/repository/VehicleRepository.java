package org.internship.repository;

import jakarta.persistence.EntityManager;
import org.internship.entity.Vehicle;
import java.util.List;

public class VehicleRepository {

    private EntityManager em;

    public VehicleRepository(EntityManager em) {
        this.em = em;
    }

    public void save(Vehicle vehicle) {
        em.persist(vehicle);
    }

    public Vehicle findById(Long id) {
        return em.find(Vehicle.class, id);
    }

    public void update(Vehicle vehicle) {
        em.merge(vehicle);
    }

    public void delete(Long id) {
        Vehicle vehicle = em.find(Vehicle.class, id);
        if (vehicle != null) em.remove(vehicle);
    }

    public List<Vehicle> findByPlate(String plate) {
        return em.createQuery("SELECT v FROM Vehicle v WHERE v.plate = :plate", Vehicle.class)
                .setParameter("plate", plate)
                .getResultList();
    }
}