package com.hospitalapp.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import com.hospitalapp.dao.AddressDao;
import com.hospitalapp.dao.BranchDao;
import com.hospitalapp.dao.EncounterDao;
import com.hospitalapp.dao.HospitalDao;
import com.hospitalapp.dao.ItemDao;
import com.hospitalapp.dao.MedOrderDao;
import com.hospitalapp.dao.PersonDao;
import com.hospitalapp.entity.Address;
import com.hospitalapp.entity.Branch;
import com.hospitalapp.entity.Encounter;
import com.hospitalapp.entity.Hospital;
import com.hospitalapp.entity.Item;
import com.hospitalapp.entity.MedOrder;
import com.hospitalapp.entity.Person;

public class HospitalApp {

	private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("vikas");
	private static EntityManager em = emf.createEntityManager();
	private static EntityTransaction et = em.getTransaction();

	private static HospitalDao hdao = new HospitalDao(em, et);
	private static EncounterDao edao = new EncounterDao(em, et);
	private static BranchDao bdao = new BranchDao(em, et);
	private static PersonDao pdao = new PersonDao(em, et);
	private static ItemDao idao = new ItemDao(em, et);
	private static MedOrderDao mdao = new MedOrderDao(em, et);
	private static AddressDao adao = new AddressDao(em, et);

	private static Scanner sc = new Scanner(System.in);

	public static void main(String[] args) {

		while (true) {
			System.out.println("=================================");
			System.out.println("1. Add Hospital");
			System.out.println("2. Add Branch to Hospital");
			System.out.println("3. Add Encounter to Branch");
			System.out.println("---------------------------------");
			System.out.println("4. Update Hospital Name / Manager");
			System.out.println("5. Update Branch Address");
			System.out.println("---------------------------------");
			System.out.println("6. Remove Hospital");
			System.out.println("7. Remove Branch");
			System.out.println("8. Remove Person");
			System.out.println("---------------------------------");
			System.out.println("9. Show Person by ID");
			System.out.println("10. Show Branch by ID");
			System.out.println("11. Show Hospital by ID");
			System.out.println("12. Show All Hospitals");
			System.out.println("=================================");
			System.out.println("");

			int choice = sc.nextInt();
			switch (choice) {
			case 1: {
				// Add Hospital
				addHospital();
				break;
			}
			case 2: {
				// Add Branch
				addBranch();
				break;
			}
			case 3: {
				// Add Encounter
				addEncounter();
				break;
			}
			case 4: {
				// update hospital
				updateHospital();
				break;

			}
			case 5: {
				// Update Branch address
				updateAddressOfBranch();
				break;
			}

			case 6: {
				// Remove Hospital
				removeHospital();
				break;
			}
			case 7: {
				// Remove Branch
				removeBranch();
				break;
			}
			case 8: {
				// Remove Person
				removePerson();
				break;
			}
			case 9: {
				// Show Person
				showPersonById();

				break;
			}
			case 10: {
				// Show Branch
				showBranchById();
				break;
			}
			case 11: {
				// Show Hospital
				showHospitalById();
				break;
			}
			case 12: {
				// Show All Hospitals
				displayAll();
				break;
			}
			}
		}
	}

	private static void removeBranch() {

		System.out.print("Hospital ID : ");
		int id = sc.nextInt();

		Hospital hospital = em.find(Hospital.class, id);

		if (hospital == null) {
			System.out.println("Hospital not exist!");
			return;
		}

		List<Branch> brli = hospital.getBranchs();
		System.out.println("------- All Branches --------");
		for (Branch branch : brli) {
			System.out.println(branch.getId() + " : " + branch.getName() + " : " + branch.getAddress());
		}
		System.out.println("-----------------------------");
		System.out.println();
		System.out.print("Branch ID : ");
		int bid = sc.nextInt();

		boolean flag = true;

		for (int i = 0; i < brli.size(); i++) {
			Branch b = brli.get(i);
			if (bid == b.getId()) {
				brli.remove(i);

				hospital.setBranchs(brli);

				// Branch Detaching
				hdao.updateHospital(hospital);

				// Address Detaching
				Address address = b.getAddress();
				b.setAddress(null);
				bdao.updateBranch(b);

				// Removing Address
				adao.removeAddress(address);

				// Removing Branch
				bdao.removeBranch(b);
				System.out.println("Branch Deleted Successfully");
				return;
			}
		}

		if (flag) {
			flag = false;
			System.out.println("Branch not found!");
			return;
		}

	}

	private static void showHospitalById() {

		System.out.print("Hospital ID : ");
		int id = sc.nextInt();

		Hospital hospital = em.find(Hospital.class, id);

		if (hospital == null) {
			System.out.println("Hospital not exist!");
			return;
		}

		List<Branch> brli = hospital.getBranchs();

		if (brli != null)
			for (Branch branch : brli) {

				System.out.println("------- Branch Details -------");
				System.out.println("Branch ID: " + branch.getId());
				System.out.println("Branch Name: " + branch.getName());
				System.out.println("Branch Address: " + branch.getAddress());
				System.out.println("------------------------------");
				System.out.println();

				// Getting all persons
				for (Person p : pdao.getAllPerson()) {

					List<Encounter> currentBranchEncounterByPerson = countEn(branch.getEncounters(), p);

					if (currentBranchEncounterByPerson.isEmpty())
						continue;
					System.out.println("<<<<    ---------    >>>>");
					System.out.println("   Encounters no. (" + currentBranchEncounterByPerson.size() + ")    ");
					System.out.println("Person ID : " + p.getId());
					System.out.println("Person Name : " + p.getName());
					System.out.println("<<<<    ---------    >>>>");
					for (Encounter e : currentBranchEncounterByPerson) {
						System.out.println("..... Encounter Details ......");
						System.out.println("Encounter ID: " + e.getId());
						System.out.println("Cause of En.: " + e.getCauseOfEncountering());
						System.out.println("Encounter Date: " + e.getDate());
						System.out.println("Discharge Date: " + e.getDischargeDate());

						for (MedOrder mo : e.getMedOrders()) {
							System.out.println("MedOrder ID: " + mo.getId());
							System.out.println("Prescription : " + mo.getPrescription());
							System.out.println("   Items   ");
							for (Item i : mo.getItems()) {
								System.out.println("*  *  *  *  *  *  *  *  *");
								System.out.println("Item ID : " + i.getId());
								System.out.println("Item Name : " + i.getName());
								System.out.println("Item Price : " + i.getPrice());
								System.out.println("*  *  *  *  *  *  *  *  *");
							}
						}
						System.out.println("..............................");
					}
					System.out.println("<<<<    ---------    >>>>");
				}
				System.out.println("-----------------------------");
			}

	}

	private static void showPersonById() {

		System.out.print("Person ID : ");
		int pid = sc.nextInt();

		Person person = em.find(Person.class, pid);
		if (person == null) {
			System.out.println("Person does not exist!");
			return;
		}

		System.out.println("<<<<    ---------    >>>>");
		System.out.println("   Encounters no. (" + person.getEnli().size() + ")    ");
		System.out.println("Person ID : " + person.getId());
		System.out.println("Person Name : " + person.getName());
		System.out.println("<<<<    ---------    >>>>");
		for (Encounter e : person.getEnli()) {
			System.out.println("..... Encounter Details ......");
			System.out.println("Encounter ID: " + e.getId());
			System.out.println("Cause of En.: " + e.getCauseOfEncountering());
			System.out.println("Encounter Date: " + e.getDate());
			System.out.println("Discharge Date: " + e.getDischargeDate());

			for (MedOrder mo : e.getMedOrders()) {
				System.out.println("MedOrder ID: " + mo.getId());
				System.out.println("Prescription : " + mo.getPrescription());
				System.out.println("   Items   ");
				for (Item i : mo.getItems()) {
					System.out.println("*  *  *  *  *  *  *  *  *");
					System.out.println("Item ID : " + i.getId());
					System.out.println("Item Name : " + i.getName());
					System.out.println("Item Price : " + i.getPrice());
					System.out.println("*  *  *  *  *  *  *  *  *");
				}
			}
			System.out.println("..............................");
		}
		System.out.println("<<<<    ---------    >>>>");
	}

	private static void showBranchById() {

		System.out.print("Hospital ID : ");
		int id = sc.nextInt();

		Hospital hospital = em.find(Hospital.class, id);

		if (hospital == null) {
			System.out.println("Hospital not exist!");
			return;
		}

		List<Branch> brli = hospital.getBranchs();

		if (brli != null)
			for (Branch branch : brli) {

				System.out.println("------- Branch Details -------");
				System.out.println("Branch ID: " + branch.getId());
				System.out.println("Branch Name: " + branch.getName());
				System.out.println("Branch Address: " + branch.getAddress());
				System.out.println("------------------------------");
				System.out.println();

				// Getting all persons
				for (Person p : pdao.getAllPerson()) {

					List<Encounter> currentBranchEncounterByPerson = countEn(branch.getEncounters(), p);

					if (currentBranchEncounterByPerson.isEmpty())
						continue;
					System.out.println("<<<<    ---------    >>>>");
					System.out.println("   Encounters no. (" + currentBranchEncounterByPerson.size() + ")    ");
					System.out.println("Person ID : " + p.getId());
					System.out.println("Person Name : " + p.getName());
					System.out.println("<<<<    ---------    >>>>");
					for (Encounter e : currentBranchEncounterByPerson) {
						System.out.println("..... Encounter Details ......");
						System.out.println("Encounter ID: " + e.getId());
						System.out.println("Cause of En.: " + e.getCauseOfEncountering());
						System.out.println("Encounter Date: " + e.getDate());
						System.out.println("Discharge Date: " + e.getDischargeDate());

						for (MedOrder mo : e.getMedOrders()) {
							System.out.println("MedOrder ID: " + mo.getId());
							System.out.println("Prescription : " + mo.getPrescription());
							System.out.println("   Items   ");
							for (Item i : mo.getItems()) {
								System.out.println("*  *  *  *  *  *  *  *  *");
								System.out.println("Item ID : " + i.getId());
								System.out.println("Item Name : " + i.getName());
								System.out.println("Item Price : " + i.getPrice());
								System.out.println("*  *  *  *  *  *  *  *  *");
							}
						}
						System.out.println("..............................");
					}
					System.out.println("<<<<    ---------    >>>>");
				}
				System.out.println("-----------------------------");
			}
	}

	private static void removePerson() {

		System.out.print("Hospital ID : ");
		int id = sc.nextInt();
		Hospital hospital = em.find(Hospital.class, id);
		if (hospital == null) {
			System.out.println("Hospital not exist!");
			return;
		}

		System.out.print("Branch ID : ");
		int bid = sc.nextInt();

		Branch branch = null;
		boolean flag = true;

		List<Branch> brli = hospital.getBranchs();
		for (Branch b : brli) {
			if (bid == b.getId()) {
				branch = b;
				flag = false;
				break;
			}
		}

		if (flag) {
			flag = false;
			System.out.println("Branch not found!");
			return;
		}

		List<Person> prli = new ArrayList<>();

		List<Person> allPrli = pdao.getAllPerson();

		for (Person person : allPrli) {
			for (Encounter en : person.getEnli()) {
				if (branch.getEncounters().contains(en)) {
					prli.add(person);
					break;
				}
			}
		}

		System.out.println("------- All Persons --------");
		for (Person b : prli) {
			System.out.println(b.getId() + " : " + b.getName());
		}
		System.out.println("-----------------------------");
		System.out.println();

		System.out.print("Person ID : ");
		int pid = sc.nextInt();

		Person person = em.find(Person.class, pid);
		if (person == null) {
			System.out.println("Person does not exist!");
			return;
		}

		List<Encounter> branchEnLi = branch.getEncounters();
		List<Encounter> perEnLi = person.getEnli();
		person.setEnli(null);
		for (Encounter e : perEnLi) {

			if (branchEnLi.contains(e)) {
				branchEnLi.remove(e);

				branch.setEncounters(branchEnLi);
				edao.removeEncounter(e);
			}
		}

		pdao.removePerson(person);
	}

	private static void addHospital() {
		System.out.print("Hospital Name : ");
		String name = sc.next();

		System.out.print("Manager Name : ");
		String manager_name = sc.next();
		Hospital hospital = new Hospital();

		hospital.setName(name);
		hospital.setManagerName(manager_name);

		hdao.saveHospital(hospital);
		System.out.println("Hospital Added! \nPlease add a branch (2)");

	}

	private static void addBranch() {
		System.out.print("Hospital ID : ");
		int id = sc.nextInt();

		Hospital hospital = hdao.findById(id);
		if (hospital == null) {
			System.out.println("Hospital does not exist!");
			return;
		}

		System.out.print("Branch Name : ");
		String name = sc.next();

		System.out.print("Address : ");
		Address address = new Address();
		sc.nextLine();
		address.setDetails(sc.nextLine());

		// adding address
		adao.addAddress(address);

		Branch branch = new Branch();
		branch.setName(name);
		branch.setAddress(address);

		// Branch adding
		bdao.addBranch(branch);

		List<Branch> brli = hospital.getBranchs();

		if (brli == null)
			brli = new ArrayList<>();

		brli.add(branch);
		hospital.setBranchs(brli);

		// Hospital updating
		hdao.updateHospital(hospital);

		System.out.println("Congrats! your new branch started");

	}

	private static void removeHospital() {

		System.out.print("Hospital ID : ");
		int id = sc.nextInt();

		Hospital hospital = hdao.findById(id);
		if (hospital == null) {
			System.out.println("Hospital does not exist!");
			return;
		}

		List<Branch> hbrli = hospital.getBranchs();

		if (hbrli != null && !hbrli.isEmpty()) {

			// Detaching hospital and branches
			hospital.setBranchs(null);
			for (Branch b : hbrli) {

				// removing encounter reference
				b.setEncounters(null);

				Address addressToBeRemove = b.getAddress();

				// removing Address reference
				b.setAddress(null);

				bdao.updateBranch(b);

				// Remove Address
				if (addressToBeRemove != null)
					adao.removeAddress(addressToBeRemove);

				if (b.getEncounters() != null && !b.getEncounters().isEmpty())
					for (Encounter encounter : b.getEncounters()) {

						for (Person person : pdao.getAllPerson()) {
							if (person.getEnli().contains(encounter)) {
								List<Encounter> perEnLi = person.getEnli();
								perEnLi.remove(encounter);
								person.setEnli(perEnLi);

								// Person Removing by encounter of branch
								pdao.checkAndSavePerson(person);
							}
						}
						// removing encounter reference
						encounter.setMedOrders(null);
						edao.updateEncounter(encounter);

						for (MedOrder med : encounter.getMedOrders()) {

							List<Item> itli = med.getItems();

							// removing medOrder reference
							med.setItems(null);
							mdao.updateMedOrder(med);

							if (itli != null && !itli.isEmpty())
								for (Item item : itli) {
									// removing Items
									idao.removeItem(item);
								}

							mdao.removeMedOrder(med);
						}

						edao.removeEncounter(encounter);
					}

				// Remove Branch
				bdao.removeBranch(b);
			}
		}

		// Removing Hospital
		hdao.removeHospital(hospital);
		System.out.println("Hospital Removed!");

	}

	private static void addEncounter() {

		System.out.print("Hospital ID : ");
		int hid = sc.nextInt();

		Hospital hospital = null;
		hospital = hdao.findById(hid);

		if (hospital == null) {
			System.out.println("Hospital not present!");
			return;
		}
		
		List<Branch> brli2 = hospital.getBranchs();
		System.out.println("------- All Branches --------");
		for (Branch branch : brli2) {
			System.out.println(branch.getId() + " : " + branch.getName() + " : " + branch.getAddress());
		}
		System.out.println("-----------------------------");
		System.out.println();

		System.out.print("Branch ID : ");
		int bid = sc.nextInt();

		Branch branch = null;
		boolean flag = true;

		List<Branch> brli = hospital.getBranchs();
		for (Branch b : brli) {
			if (bid == b.getId()) {
				branch = b;
				flag = false;
				brli.remove(branch);
				break;
			}
		}

		if (flag) {
			flag = false;
			System.out.println("Branch not found!");
			return;
		}

		Person person = new Person();
		System.out.println("Encounter by \n(1.new person 2.existing)");

		if (sc.nextInt() != 1) {
			// Existing person

			System.out.print("Person ID : ");
			int pid = sc.nextInt();

			person = em.find(Person.class, pid);
			if (person == null) {
				System.out.println("Person does not exist!");
				return;
			}
		} else {
			// New Person
			System.out.print("Person Name : ");
			sc.nextLine();
			person.setName(sc.nextLine());
		}

		Encounter encounter = new Encounter();
		MedOrder medOrder = new MedOrder();

		System.out.print("Cause Of Encountering : ");
		sc.nextLine();
		encounter.setCauseOfEncountering(sc.nextLine());

		encounter.setDate(LocalDate.now().toString());

		System.out.print("Discharge date: ");
		encounter.setDischargeDate(sc.next());

		System.out.print("MedOrder (Description) : ");
		sc.nextLine();
		medOrder.setPrescription(sc.nextLine());

		// To add items into medorder
		List<Item> itli = new ArrayList<>();

		while (true) {

			System.out.println("1. Add Item");
			System.out.println("2. Done");
			int option = sc.nextInt();
			if (option == 1) {
				while (true) {
					System.out.println(".................");
					System.out.println("1. New Item \n2. Existing Item \n3. Back");
					System.out.println(".................");
					int itemChoice = sc.nextInt();
					if (itemChoice == 1) {

						Item item = new Item();

						System.out.print("Name : ");
						sc.nextLine();
						item.setName(sc.nextLine());
						System.out.print("Price : ");
						item.setPrice(sc.nextDouble());

						itli.add(item);
					} else if (itemChoice == 2) {

						List<Item> existingItemlist = idao.geAllItems();

						System.out.println("------------------------------------");
						for (Item item : existingItemlist) {
							System.out.println(item.getId() + " : " + item.getName() + " : " + item.getPrice());
						}
						System.out.println("------------------------------------");

						System.out.print("Item ID: ");
						Item item = idao.findById(sc.nextInt());

						if (item == null) {
							System.out.println("Item not found!");
							break;
						}

						itli.add(item);
					} else {
						break;
					}

				}
			} else
				break;
		}

		// Item adding
		for (Item item : itli) {
			if (idao.findById(item.getId()) == null)
				idao.addItems(itli);
		}

		medOrder.setItems(itli);

		// MedOrder adding
		mdao.addMedOrder(medOrder);

		List<MedOrder> meli = new ArrayList<>();
		meli.add(medOrder);

		encounter.setMedOrders(meli);

		// encounter adding
		edao.addEncounter(encounter);

		List<Encounter> eli = new ArrayList<>();
		if (branch.getEncounters() != null)
			eli.addAll(branch.getEncounters());
		eli.add(encounter);

		List<Encounter> perEnLi = new ArrayList<>();
		if (person.getEnli() == null) {
			// new person list add
			perEnLi.add(encounter);
			person.setEnli(perEnLi);
		} else {
			// existing person list update
			perEnLi.addAll(person.getEnli());
			perEnLi.add(encounter);
			person.setEnli(perEnLi);
		}

		// person adding/updating
		pdao.checkAndSavePerson(person);

		branch.setEncounters(eli);

		// Branch update
		bdao.updateBranch(branch);

		brli.add(branch);
		// Hospital update
		hdao.updateHospital(hospital);

		System.out.println("Encounter added!");
	}

	private static void updateAddressOfBranch() {
		System.out.print("Hospital ID : ");
		int hid = sc.nextInt();

		Hospital hospital = hdao.findById(hid);

		if (hospital == null) {
			System.out.println("Hospital not present!");
			return;
		}

		System.out.print("Branch ID : ");
		int bid = sc.nextInt();

		Branch branch = null;
		boolean flag = true;

		List<Branch> brli = hospital.getBranchs();
		for (Branch b : brli) {
			if (bid == b.getId()) {
				branch = b;
				brli.remove(branch);
				flag = false;
				break;
			}
		}

		if (flag) {
			flag = false;
			System.out.println("Branch not found!");
			return;
		}

		System.out.print("Branch Name : ");
		branch.setName(sc.next());

		System.out.print("Address : ");
		Address address = branch.getAddress();

		if (address == null)
			address = new Address();
		sc.nextLine();
		address.setDetails(sc.nextLine());

		// updating address
		adao.updateAddress(address);

		branch.setAddress(address);

		// Branch updating
		bdao.updateBranch(branch);

		if (brli == null)
			brli = new ArrayList<>();

		brli.add(branch);
		hospital.setBranchs(brli);

		// Hospital updating
		hdao.updateHospital(hospital);

		System.out.println("Branch address updated!");

	}

	private static void updateHospital() {
		System.out.print("Hospital ID : ");
		int id = sc.nextInt();
		Hospital hospital = em.find(Hospital.class, id);
		if (hospital == null) {
			System.out.println("Hospital not exist!");
			return;
		}

		System.out.print("Hospital Name : ");
		String name = sc.next();

		System.out.print("Manager Name : ");
		String manager_name = sc.next();

		hospital.setName(name);
		hospital.setManagerName(manager_name);

		hdao.updateHospital(hospital);

		System.out.println("----------------------------------------");
		System.out.println("Hospital name is updated successfully!");
		System.out.println("Would you like to update branch details?");
		System.out.println("Please choose option 5");
		System.out.println("----------------------------------------");
	}

	public static List<Encounter> countEn(List<Encounter> enli, Person person) {
		List<Encounter> penli = new ArrayList<>();
		int count = 0;

		if (enli != null)
			for (Encounter e : enli) {
				if (person.getEnli().contains(e)) {
					count++;
					penli.add(e);
				}
			}
		return penli;
	}

	private static void displayAll() {
		List<Hospital> hli = hdao.getAllHospitals();

		if (hli.isEmpty()) {
			System.out.println("No hospital added yet!");
			return;
		}

		for (Hospital h : hli) {
			System.out.println("==============================");
			System.out.println("Hospital ID: " + h.getId());
			System.out.println("Hospital Name: " + h.getName());
			System.out.println("Manager Name : " + h.getManagerName());
			System.out.println();
			System.out.println("------- Branch Details -------");

			if (h.getBranchs() != null && !h.getBranchs().isEmpty())
				for (Branch b : h.getBranchs()) {
					System.out.println("Branch ID: " + b.getId());
					System.out.println("Branch Name: " + b.getName());
					System.out.println("Branch Address: " + b.getAddress());
					System.out.println();

					// Getting all persons
					for (Person p : pdao.getAllPerson()) {

						List<Encounter> currentBranchEncounterByPerson = countEn(b.getEncounters(), p);

						if (currentBranchEncounterByPerson.isEmpty())
							continue;
						System.out.println("<<<<    ---------    >>>>");
						System.out.println("   Encounters no. (" + currentBranchEncounterByPerson.size() + ")    ");
						System.out.println("Person ID : " + p.getId());
						System.out.println("Person Name : " + p.getName());
						System.out.println("<<<<    ---------    >>>>");
						for (Encounter e : currentBranchEncounterByPerson) {
							System.out.println("..... Encounter Details ......");
							System.out.println("Encounter ID: " + e.getId());
							System.out.println("Cause of En.: " + e.getCauseOfEncountering());
							System.out.println("Encounter Date: " + e.getDate());
							System.out.println("Discharge Date: " + e.getDischargeDate());

							for (MedOrder mo : e.getMedOrders()) {
								System.out.println("MedOrder ID: " + mo.getId());
								System.out.println("Prescription : " + mo.getPrescription());
								System.out.println("   Items   ");
								for (Item i : mo.getItems()) {
									System.out.println("*  *  *  *  *  *  *  *  *");
									System.out.println("Item ID : " + i.getId());
									System.out.println("Item Name : " + i.getName());
									System.out.println("Item Price : " + i.getPrice());
									System.out.println("*  *  *  *  *  *  *  *  *");
								}
							}

							System.out.println("..............................");
						}
						System.out.println("<<<<    ---------    >>>>");
					}

					System.out.println("-----------------------------");

				}
			else
				System.out.println("No branch added!");
			System.out.println();

			System.out.println("==============================");
			System.out.println();
		}
	}
}
