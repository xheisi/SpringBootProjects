package org.internship.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import org.internship.entity.Vehicle;
import org.internship.service.VehicleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VehicleServiceTest {

    @Mock
    private EntityManager em;

    @Mock
    private EntityTransaction transaction;

    private VehicleService vehicleService;

    @BeforeEach
    void setUp() {
        vehicleService = new VehicleService(em);
    }

    @Test
    void save_persistsWithinTransaction() {
        Vehicle vehicle = new Vehicle();
        vehicle.setPlate("AB123CD");

        when(em.getTransaction()).thenReturn(transaction);

        vehicleService.save(vehicle);

        verify(transaction).begin();
        verify(em).persist(vehicle);
        verify(transaction).commit();
    }

    @Test
    void findById_delegatesToEntityManager() {
        Vehicle vehicle = new Vehicle();
        vehicle.setId(1L);
        when(em.find(Vehicle.class, 1L)).thenReturn(vehicle);

        Vehicle result = vehicleService.findById(1L);

        assertSame(vehicle, result);
    }

    @Test
    void findById_notFound_returnsNull() {
        when(em.find(Vehicle.class, 99L)).thenReturn(null);

        assertNull(vehicleService.findById(99L));
    }

    @Test
    void update_mergesWithinTransaction() {
        Vehicle vehicle = new Vehicle();
        vehicle.setId(1L);
        vehicle.setModel("Civic");

        when(em.getTransaction()).thenReturn(transaction);

        vehicleService.update(vehicle);

        verify(transaction).begin();
        verify(em).merge(vehicle);
        verify(transaction).commit();
    }

    @Test
    void delete_existingVehicle_removesWithinTransaction() {
        Vehicle vehicle = new Vehicle();
        vehicle.setId(1L);

        when(em.getTransaction()).thenReturn(transaction);
        when(em.find(Vehicle.class, 1L)).thenReturn(vehicle);

        vehicleService.delete(1L);

        verify(transaction).begin();
        verify(em).remove(vehicle);
        verify(transaction).commit();
    }

    @Test
    void delete_nonExistentVehicle_doesNotRemove() {
        when(em.getTransaction()).thenReturn(transaction);
        when(em.find(Vehicle.class, 99L)).thenReturn(null);

        vehicleService.delete(99L);

        verify(transaction).begin();
        verify(em, never()).remove(any());
        verify(transaction).commit();
    }

    @Test
    void findByPlate_filtersOnPlate() {
        Vehicle vehicle = new Vehicle();
        vehicle.setPlate("AB123CD");
        TypedQuery<Vehicle> query = mock(TypedQuery.class);

        when(em.createQuery(anyString(), eq(Vehicle.class))).thenReturn(query);
        when(query.setParameter(eq("plate"), eq("AB123CD"))).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of(vehicle));

        List<Vehicle> result = vehicleService.findByPlate("AB123CD");

        assertEquals(1, result.size());
        verify(query).setParameter("plate", "AB123CD");
    }
}