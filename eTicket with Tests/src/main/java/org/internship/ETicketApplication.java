package org.internship;

import org.internship.entity.Citizen;
import org.internship.entity.Fine;
import org.internship.entity.Police;
import org.internship.entity.Vehicle;
import org.internship.service.CitizenService;
import org.internship.service.FineService;
import org.internship.service.PoliceService;
import org.internship.service.VehicleService;
import org.internship.util.EntityManagerUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;
import java.util.List;

@SpringBootApplication
public class ETicketApplication {

	public static void main(String[] args) {

				EntityManager em = EntityManagerUtil.getEntityManager();

				PoliceService policeService = new PoliceService(em);
				CitizenService citizenService = new CitizenService(em);
				VehicleService vehicleService = new VehicleService(em);
				FineService fineService = new FineService(em);

				// 1. Create two officers
				System.out.println("=== 1. Creating officers ===");
				Police officer1 = new Police();
				officer1.setName("Mark");
				officer1.setSurname("Smith");
				officer1.setBadgeNumber("B001");
				policeService.save(officer1);

				Police officer2 = new Police();
				officer2.setName("John");
				officer2.setSurname("Doe");
				officer2.setBadgeNumber("B002");
				policeService.save(officer2);

				// 2. Create three citizens
				System.out.println("=== 2. Creating citizens ===");
				Citizen citizen1 = new Citizen();
				citizen1.setName("Alice");
				citizen1.setSurname("Brown");
				citizenService.save(citizen1);

				Citizen citizen2 = new Citizen();
				citizen2.setName("Bob");
				citizen2.setSurname("Green");
				citizenService.save(citizen2);

				Citizen citizen3 = new Citizen();
				citizen3.setName("Carol");
				citizen3.setSurname("White");
				citizenService.save(citizen3);

				// 3. Register vehicles for citizens
				System.out.println("=== 3. Registering vehicles ===");
				Vehicle vehicle1 = new Vehicle();
				vehicle1.setPlate("AA123BB");
				vehicle1.setBrand("Toyota");
				vehicle1.setModel("Corolla");
				vehicle1.setCitizen(citizen1);
				vehicleService.save(vehicle1);

				Vehicle vehicle2 = new Vehicle();
				vehicle2.setPlate("CC456DD");
				vehicle2.setBrand("BMW");
				vehicle2.setModel("X5");
				vehicle2.setCitizen(citizen2);
				vehicleService.save(vehicle2);

				Vehicle vehicle3 = new Vehicle();
				vehicle3.setPlate("EE789FF");
				vehicle3.setBrand("Ford");
				vehicle3.setModel("Focus");
				vehicle3.setCitizen(citizen3);
				vehicleService.save(vehicle3);

				// 4. Create multiple fines
				System.out.println("=== 4. Creating fines ===");
				Fine fine1 = new Fine();
				fine1.setReason("Speeding");
				fine1.setAmount(150.0);
				fine1.setDate(LocalDate.now());
				fine1.setVehicle(vehicle1);
				fine1.setOfficer(officer1);
				fineService.issueFine(fine1);

				Fine fine2 = new Fine();
				fine2.setReason("Parking violation");
				fine2.setAmount(50.0);
				fine2.setDate(LocalDate.now());
				fine2.setVehicle(vehicle2);
				fine2.setOfficer(officer1);
				fineService.issueFine(fine2);

				Fine fine3 = new Fine();
				fine3.setReason("Red light");
				fine3.setAmount(200.0);
				fine3.setDate(LocalDate.now());
				fine3.setVehicle(vehicle1);
				fine3.setOfficer(officer2);
				fineService.issueFine(fine3);

				Fine fine4 = new Fine();
				fine4.setReason("No seatbelt");
				fine4.setAmount(75.0);
				fine4.setDate(LocalDate.now());
				fine4.setVehicle(vehicle3);
				fine4.setOfficer(officer2);
				fineService.issueFine(fine4);

				// 5. Print all fines
				System.out.println("=== 5. All fines ===");
				List<Fine> allFines = fineService.findAll();
				for (Fine f : allFines) {
					System.out.println("Fine ID: " + f.getId()
							+ " | Reason: " + f.getReason()
							+ " | Amount: " + f.getAmount()
							+ " | Status: " + f.getStatus()
							+ " | Plate: " + f.getVehicle().getPlate());
				}

				// 6. Search fines by citizen
				System.out.println("=== 6. Fines for citizen Alice (ID: " + citizen1.getId() + ") ===");
				List<Fine> finesByCitizen = fineService.findByCitizenId(citizen1.getId());
				for (Fine f : finesByCitizen) {
					System.out.println("Fine ID: " + f.getId() + " | Reason: " + f.getReason());
				}

				// 7. Search fines by plate number
				System.out.println("=== 7. Fines for plate AA123BB ===");
				List<Fine> finesByPlate = fineService.findByPlate("AA123BB");
				for (Fine f : finesByPlate) {
					System.out.println("Fine ID: " + f.getId() + " | Reason: " + f.getReason());
				}

				// 8. Update one fine reason
				System.out.println("=== 8. Updating reason of fine ID: " + fine1.getId() + " ===");
				fineService.updateReason(fine1.getId(), "Excessive speeding");
				System.out.println("Updated reason to: Drunk driving");

				// 9. Pay one fine
				System.out.println("=== 9. Paying fine ID: " + fine2.getId() + " ===");
				fineService.payFine(fine2.getId());

				// 10. Try to pay the same fine again
				System.out.println("=== 10. Trying to pay fine ID: " + fine2.getId() + " again ===");
				fineService.payFine(fine2.getId());

				// 11. Cancel one unpaid fine
				System.out.println("=== 11. Cancelling fine ID: " + fine3.getId() + " ===");
				fineService.cancelFine(fine3.getId());

				// 12. Try to pay the cancelled fine
				System.out.println("=== 12. Trying to pay cancelled fine ID: " + fine3.getId() + " ===");
				fineService.payFine(fine3.getId());

				// 13. Print final fine statuses
				System.out.println("=== 13. Final fine statuses ===");
				List<Fine> finalFines = fineService.findAll();
				for (Fine f : finalFines) {
					System.out.println("Fine ID: " + f.getId()
							+ " | Reason: " + f.getReason()
							+ " | Status: " + f.getStatus());
				}

				// 14. Shutdown Hibernate
				System.out.println("=== 14. Shutting down ===");
				em.close();
				EntityManagerUtil.shutdown();
				System.out.println("Done!");
	}

}
