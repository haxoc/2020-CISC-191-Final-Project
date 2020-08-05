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
    public CommandLineRunner demo(ClientRepository repo) {
        return args -> {
            System.out.println("Add Client 20");
            Client client1 = new Client("Client 20", 100.00, "some address");
            for (int i = 1; i <= 10; i++) {
                //Create a project
                Project project = new Project("Project-" + i, 35, client1);
                //System.out.println(project);

                //Create time sheet for this project
                TimeSheet timeSheetNew = new TimeSheet(project, LocalDate.now().withDayOfMonth(1), LocalDate.now().plusMonths(1));
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
                TimeSheet timeSheetNew = new TimeSheet(project, LocalDate.now().withDayOfMonth(1), LocalDate.now().plusMonths(1));
                //System.out.println("timeSheetNew = " + timeSheetNew);

                project.addTimeSheet(timeSheetNew);
                client3.addProject(project);
            }
            repo.save(client3);
            System.out.println(client3);
            System.out.println(client3.getProjects());

            //Loop through client1 to set up time entries
            Integer clientId = client1.getId();
            System.out.println("saved client ID = " + clientId);

            Optional<Client> c = repo.findById(clientId);

            c.ifPresent((client) -> client.getProjects().forEach((name, project) -> {
                System.out.println("Client 20 details below, clientId = " + clientId);
                project.getTimeSheets().forEach((monthYear, timeSheet) -> {
                    for (int i = 5; i <= 20; i++) {
                        TimeSheetEntry timeSheetEntry = new TimeSheetEntry(
                                LocalDate.now().plusDays(i), //date
                                "employee " + i + " name", //employeeName
                                "desc: " + i, //description
                                (double) Math.round(Math.random() * 10), //time
                                timeSheet
                        );
                        timeSheet.addEntry(timeSheetEntry);
                    }
                });
                repo.save(client);
//                repo.deleteById(client.getId());
//                repo.save(client);
            }));
        };
    }
}
