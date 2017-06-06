package com.excilys.computerdatabase.cli;

import java.util.List;
import java.util.Scanner;

import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.excilys.computerdatabase.config.Config;
import com.excilys.computerdatabase.dtos.CompanyDTO;
import com.excilys.computerdatabase.dtos.ComputerDTO;
import com.excilys.computerdatabase.exceptions.IntroducedAfterDiscontinuedException;
import com.excilys.computerdatabase.exceptions.NameEmptyException;
import com.excilys.computerdatabase.exceptions.NotFoundException;
import com.excilys.computerdatabase.mappers.impl.ComputerMapper;
import com.excilys.computerdatabase.models.Computer;
import com.excilys.computerdatabase.models.Page;
import com.excilys.computerdatabase.validators.ComputerValidator;

public class CLI {

    //@Autowired
    private static ComputerMapper computerMapper = new ComputerMapper();

    private static Scanner scanner;

    private static final String DATE_FORMAT = Config.getProperties().getProperty("date_format");

    private static WebTarget target;

    /**
     * Constructor.
     */
    public CLI() {
        AnnotationConfigApplicationContext  context = new AnnotationConfigApplicationContext();
        context.scan("com.excilys.computerdatabase");
        context.refresh();

        computerMapper = (ComputerMapper) context.getBean("computerMapper");

        context.close();
    }

    private static void initializeService(String username, String password) {
        //HttpAuthenticationFeature feature = HttpAuthenticationFeature.digest(username, password);
        
        HttpAuthenticationFeature feature = HttpAuthenticationFeature.universalBuilder()
                      .credentialsForBasic(username, password)
                      .credentialsForDigest(username, password)
                      .credentials(username, password)
                      .build();
        
        Client client = ClientBuilder.newClient();
        client.register(feature);
        target = client.target("http://localhost:8080").path("/webapp").path("api");
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

        GenericType<Page<CompanyDTO>> genericType = new GenericType<Page<CompanyDTO>>() {
        };

        Response response = builder.get();
        
        System.out.println(response);

        Page<CompanyDTO> companies = response.readEntity(genericType);
        companies.getObjects().forEach(System.out::println);
    }

    /**
     * Print the list of the computers.
     */
    public static void printListComputers() {
        Builder builder = target.path("computer").request(MediaType.APPLICATION_JSON_TYPE);

        GenericType<List<ComputerDTO>> genericType = new GenericType<List<ComputerDTO>>() {
        };

        Response response = builder.get();
        
        System.out.println(response);

        List<ComputerDTO> computers = response.readEntity(genericType);
        computers.forEach(System.out::println);
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

                System.out.println("Date d'ajout ? (" + DATE_FORMAT + ")");
                //introducedComputer = scanner.nextLine();
                introducedComputer = "";

                System.out.println("Date de retrait ? (" + DATE_FORMAT + ")");
                //discontinuedComputer = scanner.nextLine();
                discontinuedComputer = "";

                System.out.println("Marque ?");
                //idCompany = scanner.next();
                idCompany = "1";

                try {
                    ComputerDTO computerDTO = new ComputerDTO();
                    CompanyDTO companyDTO = new CompanyDTO();

                    companyDTO.setId(Long.parseLong(idCompany));

                    computerDTO.setName(nameComputer);
                    computerDTO.setIntroduced(!introducedComputer.equals("") ? introducedComputer : null);
                    computerDTO.setDiscontinued(!discontinuedComputer.equals("") ? discontinuedComputer : null);
                    computerDTO.setCompany(companyDTO);

                    Computer computer = computerMapper.bean(computerDTO);

                    ComputerValidator.validate(computer);

                    Builder builder = target.path("computer").request(MediaType.APPLICATION_JSON_TYPE);
                    builder.post(Entity.json(computer));
                    //computerService.save(computer);

                    System.out.println("Computer ajouté");

                } catch (NumberFormatException e) {
                    System.out.println("L'id doit être un nombre");
                } catch (IntroducedAfterDiscontinuedException e) {
                    System.out.println("La date d'ajout doit être antérieure à la date de retrait");
                } catch (NameEmptyException e) {
                    System.out.println("Le nom doit être non nul");
                } catch (NotFoundException e) {
                    System.out.println("La company n'existe pas");
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

                /*try {
                    ComputerDTO computerDTO = new ComputerDTO();
                    CompanyDTO companyDTO = new CompanyDTO();

                    companyDTO.setId(Long.parseLong(idCompany));

                    computerDTO.setName(nameComputer);
                    computerDTO.setId(Long.parseLong(idComputer));
                    computerDTO.setIntroduced(!introducedComputer.equals("") ? introducedComputer : null);
                    computerDTO.setDiscontinued(!discontinuedComputer.equals("") ? discontinuedComputer : null);
                    computerDTO.setCompany(companyDTO);

                    Computer computer = computerMapper.bean(computerDTO);

                    ComputerValidator.validate(computer);
                    computerService.update(computer);

                } catch (NumberFormatException e) {
                    System.out.println("L'id doit être un nombre");
                } catch (IntroducedAfterDiscontinuedException e) {
                    System.out.println("La date d'ajout doit être antérieure à la date de retrait");
                } catch (NameEmptyException e) {
                    System.out.println("Le nom doit être non nul");
                } catch (NotFoundException e) {
                    if (e instanceof ComputerNotFoundException) {
                        System.out.println("Le computer n'existe pas");
                    } else if (e instanceof CompanyNotFoundException) {
                        System.out.println("La company n'exste pas");
                    }
                }*/
                break;

            case "6":
                System.out.println("Id du pc ?");
                idComputer = scanner.next();

                /*try {
                    computerService.delete(Long.parseLong(idComputer));
                    System.out.println("Computer supprimé");
                } catch (NumberFormatException e) {
                    System.out.println("L'id doit être un nombre");
                } catch (NotFoundException e) {
                    System.out.println("Le computer n'existe pas");
                }*/
                break;

            case "7":
                System.out.println("Id de la companie ?");
                idCompany = scanner.next();

                /*try {
                    companyService.delete(Long.parseLong(idCompany));
                    System.out.println("Computer supprimé");
                } catch (NumberFormatException e) {
                    System.out.println("L'id doit être un nombre");
                } catch (NotFoundException e) {
                    System.out.println("Le computer n'existe pas");
                }*/
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
