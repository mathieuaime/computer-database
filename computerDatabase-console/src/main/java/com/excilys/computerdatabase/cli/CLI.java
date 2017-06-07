package com.excilys.computerdatabase.cli;

import java.util.Scanner;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.HttpStatus;

import com.excilys.computerdatabase.config.Config;
import com.excilys.computerdatabase.dtos.CompanyDTO;
import com.excilys.computerdatabase.dtos.ComputerDTO;
import com.excilys.computerdatabase.models.Company;
import com.excilys.computerdatabase.models.Page;
import com.excilys.computerdatabase.validators.ComputerDTOValidator;

public class CLI {

    private static Scanner scanner;

    private static final String DATE_FORMAT = Config.getProperties().getProperty("date_format");

    private static WebTarget target;

    /**
     * Constructor.
     */
    public CLI() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.scan("com.excilys.computerdatabase");
        context.refresh();

        context.close();
    }

    private static void initializeService(String username, String password) {
        // HttpAuthenticationFeature feature =
        // HttpAuthenticationFeature.digest(username, password);

        HttpAuthenticationFeature feature = HttpAuthenticationFeature.universalBuilder()
                .credentialsForBasic(username, password).credentialsForDigest(username, password)
                .credentials(username, password).build();

        Client client = ClientBuilder.newClient();
        client.register(feature);
        target = client.target("http://localhost:8080").path("/computerDatabase-webapp").path("api");
    }

    /**
     * Print the differents choices of the menu.
     */
    public static void printMenu() {
        System.out.println("\nMenu :\n");
        System.out.println("1 : Liste des compagnies");
        System.out.println("2 : Liste des pcs");
        System.out.println("3 : Details d'un pc");
        System.out.println("4 : Créer un pc");
        System.out.println("5 : MAJ un pc");
        System.out.println("6 : Supprimer un pc");
        System.out.println("7 : Supprimer une companie");
        System.out.println("8 : Quitter");
    }

    /**
     * Print the list of the companies.
     */
    public static void printListCompanies() {
        Builder builder = target.path("company").request(MediaType.APPLICATION_JSON_TYPE);

        GenericType<Page<Company>> genericType = new GenericType<Page<Company>>() {
        };

        Response response = builder.get();

        Page<Company> companies = response.readEntity(genericType);
        companies.getObjects().forEach(System.out::println);
    }

    /**
     * Print the list of the computers.
     */
    public static void printListComputers() {
        Builder builder = target.path("computer").request(MediaType.APPLICATION_JSON_TYPE);

        GenericType<Page<ComputerDTO>> genericType= new GenericType<Page<ComputerDTO>>() {
        };

        Response response = builder.get();

        Page<ComputerDTO> computers = response.readEntity(genericType);
        computers.getObjects().forEach(System.out::println);
    }

    /**
     * Print the computer id.
     * @param id the id of the computer
     */
    public static void printComputer(long id) {
        Builder builder = target.path("computer").path(String.valueOf(id)).request(MediaType.APPLICATION_JSON_TYPE);

        try {
            ComputerDTO response = builder.get(ComputerDTO.class);
            System.out.println(response);
        } catch (javax.ws.rs.NotFoundException e) {
            System.out.println("Computer " + id + " Not Found");
        }
    }

    /**
     * Command-Line Interface.
     * @param args no option
     */
    public static void main(String[] args) {
        scanner = new Scanner(System.in);

        // login

        System.out.println("Username ?");
        String username = scanner.next();
        System.out.println("Password ?");
        String password = scanner.next();

        initializeService(username, password);

        loop: while (true) {
            printMenu();
            String idComputer;
            String nameComputer;
            String introducedComputer;
            String discontinuedComputer;
            String idCompany;
            
            Builder builder;
            Response response;

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

                System.out.println("Nom ?");
                nameComputer = scanner.next();

                introducedComputer = "";
                System.out.println("Date d'ajout ? (" + DATE_FORMAT + ")");
                // introducedComputer = scanner.nextLine();

                discontinuedComputer = "";
                System.out.println("Date de retrait ? (" + DATE_FORMAT + ")");
                // discontinuedComputer = scanner.nextLine();

                System.out.println("Marque ?");
                // idCompany = scanner.next();
                idCompany = "1";

                try {
                    ComputerDTO computerDTO = new ComputerDTO();
                    builder = target.path("company").path(String.valueOf(idCompany)).request();

                    CompanyDTO companyDTO = new CompanyDTO();

                    try {
                        companyDTO = builder.get(CompanyDTO.class);
                    } catch (javax.ws.rs.NotFoundException e) {
                        System.out.println("Company " + idCompany + " Not Found");
                    }

                    computerDTO.setName(nameComputer);
                    computerDTO.setIntroduced(introducedComputer);
                    computerDTO.setDiscontinued(discontinuedComputer);
                    computerDTO.setCompany(companyDTO);
                    boolean verification = new ComputerDTOValidator().isValid(computerDTO, null);
                    if (verification) {
                        builder = target.path("computer").request();
                        builder.post(Entity.json(computerDTO));

                        System.out.println("Computer ajouté");
                    } else {
                        System.out.println("Les données de l'ordinateur sont incorrectes");
                    }

                } catch (NumberFormatException e) {
                    System.out.println("L'id doit être un nombre");

                }
                break;

            case "5":
                System.out.println("Modification d'un ordinateur");

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

                    try {
                        builder = target.path("company").path(String.valueOf(idCompany)).request();
                        companyDTO = builder.get(CompanyDTO.class);
                    } catch (javax.ws.rs.NotFoundException e) {
                        System.out.println("Company " + idCompany + " Not Found");
                    }

                    companyDTO.setId(Long.parseLong(idCompany));

                    computerDTO.setName(nameComputer);
                    computerDTO.setId(Long.parseLong(idComputer));
                    computerDTO.setIntroduced(introducedComputer);
                    computerDTO.setDiscontinued(discontinuedComputer);
                    computerDTO.setCompany(companyDTO);

                    boolean verification = new ComputerDTOValidator().isValid(computerDTO, null);
                    if (verification) {
                        builder = target.path("computer").request();
                        builder.put(Entity.json(computerDTO));

                        System.out.println("Computer ajouté");
                    } else {
                        System.out.println("Les données de l'ordinateur sont incorrectes");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("L'id doit être un nombre");
                }
                 
                break;

            case "6":
                System.out.println("Id du pc ?");
                idComputer = scanner.next();

                builder = target.path("computer").path(idComputer).request(MediaType.APPLICATION_JSON_TYPE);

                response = builder.delete();
                if (response.getStatus() == HttpStatus.NO_CONTENT.value()) {
                    System.out.println("Computer supprimé");
                } else {
                    System.out.println("Computer inconnu");
                }

                break;

            case "7":
                System.out.println("Id de la companie ?");
                idCompany = scanner.next();

                builder = target.path("company").path(idCompany).request(MediaType.APPLICATION_JSON_TYPE);

                response = builder.delete();
                if (response.getStatus() == HttpStatus.NO_CONTENT.value()) {
                    System.out.println("Compagnie supprimée");
                } else {
                    System.out.println("Compagnie inconnue");
                }

                break;

            case "8":
                break loop;

            default:
                break;

            }
        }

        scanner.close();
    }
}
