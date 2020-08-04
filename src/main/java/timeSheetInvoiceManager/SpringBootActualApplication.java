/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timeSheetInvoiceManager;

import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.CommandLineRunner;

import timeSheetInvoiceManager.client.Client;
import timeSheetInvoiceManager.client.ClientRepository;
import timeSheetInvoiceManager.project.Project;
import timeSheetInvoiceManager.project.ProjectRepository;
import timeSheetInvoiceManager.timesheet.TimeSheetEntry;
import timeSheetInvoiceManager.timesheet.TimeSheetEntryRepository;
import timeSheetInvoiceManager.timesheet.TimeSheet;
import timeSheetInvoiceManager.timesheet.TimeSheetRepository;

import java.util.Optional;
import java.time.LocalDate;

/**
 * @author xaboo
 */
@SpringBootApplication
public class SpringBootActualApplication {

    public static void main(String[] args) {
        // This is how normal Spring Boot app would be launched
        //SpringApplication.run(JavafxWeaverExampleApplication.class, args);

        Application.launch(App.class, args);
    }

    @Bean
    public CommandLineRunner demo(ClientRepository repo, ProjectRepository projectRepository, TimeSheetRepository timeSheetRepository, TimeSheetEntryRepository timeSheetEntryRepository) {
        return args -> {
            System.out.println("Add Client 20");
            Client client1 = new Client("Client 20", 100.00, "some address");
            for (int i = 1; i <= 10; i++) {
                //Create a project
                Project project = new Project("Project-" + i, 35, client1);
                //System.out.println(project);

                //Create time sheet for this project
                TimeSheet timeSheetNew = new TimeSheet(project, LocalDate.now(), LocalDate.now().plusMonths(1));
                //System.out.println("timeSheetNew = " + timeSheetNew);

                project.addTimeSheet(timeSheetNew);
                client1.addProject(project);
            }
            repo.save(client1);
            System.out.println(client1);
            System.out.println(client1.getProjects());
            System.out.println(client1.getProjects());

            System.out.println("Add Client 30");
            Client client3 = new Client("Client 30", 103.00, "some address 03");
            for (int i = 1; i <= 10; i++) {
                //Create a project
                Project project = new Project("Project-" + i, 35, client1);
                //System.out.println(project);

                //Create time sheet for this project
                TimeSheet timeSheetNew = new TimeSheet(project, LocalDate.now(), LocalDate.now().plusMonths(1));
                //System.out.println("timeSheetNew = " + timeSheetNew);

                project.addTimeSheet(timeSheetNew);
                client3.addProject(project);
            }
            repo.save(client3);
            System.out.println(client3);
            System.out.println(client3.getProjects());

            //Loop through client1 to set up time entries
            String clientId = client1.getId();
            System.out.println("saved client ID = " + clientId);

            Optional<Client> c = repo.findById(clientId);

            c.ifPresent((client) -> client.getProjects().forEach((name, project) -> {
                System.out.println("Client 20 details below, clientId = " + clientId);
                //System.out.println(client);
                //System.out.println(name);
                //System.out.println(project);
                //System.out.println(project.getTimeSheet(LocalDate.ofYearDay(LocalDate.now().getYear(), LocalDate.now().getMonthValue()))); //this returns a null
                //System.out.println(project.getTimeSheets());

                Optional<TimeSheet> timeSheet_linked = timeSheetRepository.findById(project.getId());
                //System.out.println(timeSheet_linked);

                for (int i = 5; i <= 20; i++) {
                    //System.out.println("i want to add time sheet entries");
                    //TimeSheet timeSheet_linked = project.getTimeSheet(LocalDate.ofYearDay(LocalDate.now().getYear(), LocalDate.now().getMonthValue()));
                    //System.out.println("timeSheet_linked = " + timeSheet_linked);
                    if (timeSheet_linked.isPresent()) {
                        TimeSheetEntry timeSheetEntry = new TimeSheetEntry(
                                                                LocalDate.now(), //date
                                                                "employee " + i + " name", //employeeName
                                                                "desc: " + i, //description
                                                                Math.round(Math.random() * 10), //time
                                                                timeSheet_linked.get(), //timesheet object
                                                                project.getId() //projectid
                                                            );
                        timeSheet_linked.get().addEntry(timeSheetEntry);
                        timeSheetEntryRepository.save(timeSheetEntry);
                        //System.out.println("timeSheetEntry = " + timeSheetEntry);
                    }
                }
            }));

            if (c.isPresent()) {
                client1 = c.get();
            }

            repo.save(client1);

            System.out.println("repo.findAll()");
            repo.findAll().forEach(System.out::println);

            System.out.println("projectRepository.findAll()");
            projectRepository.findAll().forEach(System.out::println);

            System.out.println("timeSheetRepository.findById(3)");
            Optional<TimeSheet> timeSheet = timeSheetRepository.findById(3);
            timeSheet.ifPresent(System.out::println);

            System.out.println("timeSheetRepository.findByAll()");
            timeSheetEntryRepository.findAll().forEach(System.out::println);
            //timeSheet.ifPresent(System.out::println);
            //System.out.println("timeSheetEntryRepository.findById(19)");
            //Optional<TimeSheetEntry> timeSheetEntry = timeSheetEntryRepository.findById(19);
            //System.out.println(timeSheetEntry);
        };
    }
}
