package com.excilys.computerdatabase.ui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import com.excilys.computerdatabase.config.Config;
import com.excilys.computerdatabase.models.Company;
import com.excilys.computerdatabase.models.Computer;
import com.excilys.computerdatabase.services.CompanyService;
import com.excilys.computerdatabase.services.ComputerService;

public class CLI {

	private static Scanner scanner;
	private static CompanyService companyService = new CompanyService();
	private static ComputerService computerService = new ComputerService();

	private static String dateFormat = Config.DATE_FORMAT;
	private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);

	public static void printMenu() {
		// prompt for the menu
		System.out.println("\nMenu :\n");
		System.out.println("1 : Liste des compagnies");
		System.out.println("2 : Liste des pcs");
		System.out.println("3 : Details d'un pc");
		System.out.println("4 : Créer un pc");
		System.out.println("5 : MAJ un pc");
		System.out.println("6 : Supprimer un pc");
		System.out.println("7 : Quitter");
	}

	public static void printListCompanies() {
		for (Company c : companyService.get()) {
			System.out.println(c);
		}
	}

	public static void printListComputers() {
		for (Computer c : computerService.get()) {
			System.out.println(c);
		}
	}

	public static void printComputer(int id) {
		Computer c = computerService.get(id);
		System.out.println(c != null ? c : "Pas de pc trouvé");
	}

	public static void main(String[] args) {

		scanner = new Scanner(System.in);

		loop: while (true) {
			printMenu();
			String idComputer;
			String nameComputer;
			String introducedComputer;
			String discontinuedComputer;
			String idCompany;

			// get input as a String
			String choice = scanner.next();

			switch (choice) {

			case "1":
				printListCompanies();
				break;

			case "2":
				printListComputers();
				break;

			case "3":
				System.out.println("Id du pc ?");
				idComputer = scanner.next();

				try {
					int id = Integer.parseInt(idComputer);
					printComputer(id);
				} catch (NumberFormatException e) {
					System.out.println("L'id doit être un nombre");
				}

				break;

			case "4":
				System.out.println("Ajout d'un pc");

				System.out.println("Id ?");
				idComputer = scanner.nextLine();

				System.out.println("Nom ?");
				nameComputer = scanner.nextLine();

				System.out.println("Date d'ajout ? (" + dateFormat + ")");
				introducedComputer = scanner.nextLine();

				System.out.println("Date de retrait ? (" + dateFormat + ")");
				discontinuedComputer = scanner.nextLine();

				System.out.println("Marque ?");
				idCompany = scanner.next();

				try {
					Computer.Builder c = new Computer.Builder(Integer.parseInt(idComputer), nameComputer);

					if (!introducedComputer.equals("null")) {
						Date parsedIntroduced = simpleDateFormat.parse(introducedComputer);
						c.introduced(parsedIntroduced);
					}

					if (!discontinuedComputer.equals("null")) {
						Date parsedDiscontinued = simpleDateFormat.parse(discontinuedComputer);
						c.discontinued(parsedDiscontinued);
					}

					c.company(companyService.get(Integer.parseInt(idCompany)));

					boolean add = computerService.add(c.build());
					System.out.println(add ? "Pc ajouté" : "Erreur dans l'ajout du pc");

				} catch (NumberFormatException e) {
					System.out.println("L'id doit être un nombre");
				} catch (ParseException e) {
					System.out.println("Mauvais format de date");
				}
				break;

			case "5":
				System.out.println("Modification d'un pc");

				System.out.println("Id ?");
				idComputer = scanner.nextLine();

				System.out.println("Nouveau nom ?");
				nameComputer = scanner.nextLine();

				System.out.println("Nouvelle date d'ajout ?");
				introducedComputer = scanner.nextLine();

				System.out.println("Nouvelle date de retrait ?");
				discontinuedComputer = scanner.nextLine();

				System.out.println("Nouvelle marque ?");
				idCompany = scanner.nextLine();

				try {
					Computer.Builder c = new Computer.Builder(Integer.parseInt(idComputer), nameComputer);

					if (!introducedComputer.equals("null")) {
						Date parsedIntroduced = simpleDateFormat.parse(introducedComputer);
						c.introduced(parsedIntroduced);
					}

					if (!discontinuedComputer.equals("null")) {
						Date parsedDiscontinued = simpleDateFormat.parse(discontinuedComputer);
						c.discontinued(parsedDiscontinued);
					}

					c.company(companyService.get(Integer.parseInt(idCompany)));

					boolean update = computerService.update(c.build());

					System.out.println(update ? "Pc modifié" : "Erreur dans la modification");

				} catch (NumberFormatException e) {
					System.out.println("L'id doit être un nombre");
				} catch (ParseException e) {
					System.out.println("Mauvais format de date");
				}
				break;

			case "6":
				System.out.println("Id du pc ?");
				idComputer = scanner.next();

				try {
					boolean res = computerService.delete(Integer.parseInt(idComputer));
					System.out.println((res ? "Pc supprimé" : "Erreur dans la supression"));
				} catch (NumberFormatException e) {
					System.out.println("L'id doit être un nombre");
				}
				break;

			case "7":
				break loop;

			default:
				break;

			}
		}

		scanner.close();
	}
}
