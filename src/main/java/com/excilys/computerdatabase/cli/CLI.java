package com.excilys.computerdatabase.cli;

import java.util.Scanner;

import com.excilys.computerdatabase.config.Config;
import com.excilys.computerdatabase.dtos.CompanyDTO;
import com.excilys.computerdatabase.dtos.ComputerDTO;
import com.excilys.computerdatabase.exceptions.ComputerNotFoundException;
import com.excilys.computerdatabase.exceptions.IntroducedAfterDiscontinuedException;
import com.excilys.computerdatabase.exceptions.NameEmptyException;
import com.excilys.computerdatabase.mappers.ComputerMapper;
import com.excilys.computerdatabase.models.Computer;
import com.excilys.computerdatabase.services.CompanyServiceImpl;
import com.excilys.computerdatabase.services.ComputerServiceImpl;
import com.excilys.computerdatabase.validators.ComputerValidator;

public class CLI {

    private static Scanner scanner;
    private static CompanyServiceImpl companyService = new CompanyServiceImpl();
    private static ComputerServiceImpl computerService = new ComputerServiceImpl();

    private static final String DATE_FORMAT = Config.getProperties().getProperty("date_format");

    /**
     * Print the differents choices of the menu.
     */
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

    /**
     * Print the list of the companies.
     */
    public static void printListCompanies() {
        for (CompanyDTO c : companyService.get()) {
            System.out.println(c);
        }
    }

    /**
     * Print the list of the computers.
     */
    public static void printListComputers() {
        for (ComputerDTO c : computerService.get()) {
            System.out.println(c);
        }
    }

    /**
     * Print the computer id.
     * @param id the id of the computer
     */
    public static void printComputer(long id) {
        try {
            ComputerDTO c = computerService.getById(id);
            System.out.println(c);
        } catch (ComputerNotFoundException e) {
            System.out.println("Pas de pc trouvé");
        }
    }

    /**
     * Command-Line Interface.
     * @param args no option
     */
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

                System.out.println("Date d'ajout ? (" + DATE_FORMAT + ")");
                introducedComputer = scanner.nextLine();

                System.out.println("Date de retrait ? (" + DATE_FORMAT + ")");
                discontinuedComputer = scanner.nextLine();

                System.out.println("Marque ?");
                idCompany = scanner.next();

                try {
                    ComputerDTO computerDTO = new ComputerDTO();
                    CompanyDTO companyDTO = new CompanyDTO();

                    companyDTO.setId(Long.parseLong(idCompany));

                    computerDTO.setName(nameComputer);
                    computerDTO.setId(Long.parseLong(idComputer));
                    computerDTO.setIntroduced(introducedComputer);
                    computerDTO.setDiscontinued(discontinuedComputer);
                    computerDTO.setCompany(companyDTO);

                    Computer computer = ComputerMapper.createBean(computerDTO);

                    ComputerValidator.validate(computer);
                    computerService.add(computer);

                    System.out.println("Computer ajouté");

                } catch (NumberFormatException e) {
                    System.out.println("L'id doit être un nombre");
                } catch (IntroducedAfterDiscontinuedException e) {
                    System.out.println("La date d'ajout doit être antérieure à la date de retrait");
                } catch (NameEmptyException e) {
                    System.out.println("Le nom doit être non nul");
                }
                break;

            case "5":
                System.out.println("Modification d'un computer");

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
                    ComputerDTO computerDTO = new ComputerDTO();
                    CompanyDTO companyDTO = new CompanyDTO();

                    companyDTO.setId(Long.parseLong(idCompany));

                    computerDTO.setName(nameComputer);
                    computerDTO.setId(Long.parseLong(idComputer));
                    computerDTO.setIntroduced(introducedComputer);
                    computerDTO.setDiscontinued(discontinuedComputer);
                    computerDTO.setCompany(companyDTO);

                    Computer computer = ComputerMapper.createBean(computerDTO);

                    ComputerValidator.validate(computer);
                    computerService.update(computer);

                } catch (NumberFormatException e) {
                    System.out.println("L'id doit être un nombre");
                } catch (ComputerNotFoundException e) {
                    System.out.println("Le computer n'existe pas");
                } catch (IntroducedAfterDiscontinuedException e) {
                    System.out.println("La date d'ajout doit être antérieure à la date de retrait");
                } catch (NameEmptyException e) {
                    System.out.println("Le nom doit être non nul");
                }
                break;

            case "6":
                System.out.println("Id du pc ?");
                idComputer = scanner.next();

                try {
                    computerService.delete(Integer.parseInt(idComputer));
                    System.out.println("Computer supprimé");
                } catch (NumberFormatException e) {
                    System.out.println("L'id doit être un nombre");
                } catch (ComputerNotFoundException e) {
                    System.out.println("Le computer n'existe pas");
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
