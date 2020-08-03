/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.timeSheetInvoiceManager.app;

import javafx.application.Application;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.spring.SpringFxWeaver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.CommandLineRunner;

import com.timeSheetInvoiceManager.app.client.Client;
import com.timeSheetInvoiceManager.app.client.ClientRepository;
import com.timeSheetInvoiceManager.app.project.Project;
import com.timeSheetInvoiceManager.app.project.ProjectRepository;
import com.timeSheetInvoiceManager.app.timesheet.TimeSheetEntry;
import com.timeSheetInvoiceManager.app.timesheet.TimeSheetEntryRepository;
import com.timeSheetInvoiceManager.app.timesheet.TimeSheet;
import com.timeSheetInvoiceManager.app.timesheet.TimeSheetRepository;

import java.util.Optional;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author xaboo
 */
@SpringBootApplication
public class SpringBootActualApplication {

    public static void main(String[] args) {
        // This is how normal Spring Boot app would be launched
        //SpringApplication.run(JavafxWeaverExampleApplication.class, args);

        Application.launch(AppApplication.class, args);
    }

    @Bean
    public CommandLineRunner demo(ClientRepository repo, ProjectRepository projectRepository, TimeSheetRepository timeSheetRepository, TimeSheetEntryRepository timeSheetEntryRepository) {
        return new CommandLineRunner() {
            @Override
            public void run(String[] args) throws Exception {
                Client client1 = new Client("Client 20", 100.00, "some address");
                
                for (int i = 1; i <= 10; i++) {
                    Project project = new Project("Project-" + i, 35, client1);
                    client1.addProject(project);
                    System.out.println(project);
                    
                }
                repo.save(client1);
                Client client3 = new Client("Client 30", 103.00, "some address 03");
                for (int i = 1; i <= 10; i++) {
                    Project project = new Project("Project-" + i, 35, client1);
                    client1.addProject(project);
                    System.out.println(project);
                }
                repo.save(client3);
                
                Integer clientId = client1.getId();
                
                System.out.println("saved client ID = " + clientId);
                Optional<Client> c = repo.findById(clientId);
                
                c.ifPresent((client) -> {
                    client.getProjects().forEach((name, project) -> {
                        //Create time sheet for this project
                        TimeSheet timeSheetNew = new TimeSheet(project);
                        System.out.println("timeSheetNew = " + timeSheetNew);
                        timeSheetRepository.save(timeSheetNew);
                        for (int i = 5; i <= 20; i++) {
                            System.out.println("i want to add time sheet entries");
                            TimeSheetEntry timeSheetEntry = new TimeSheetEntry(LocalDate.now(), "employee " + i + " name", "desc: " + i, Math.round(Math.random() * 10), project.getTimeSheet(), project.getId());
                            timeSheetNew.addEntry(timeSheetEntry);
                            timeSheetEntryRepository.save(timeSheetEntry);
                            
                            System.out.println("timeSheetEntry = " + timeSheetEntry);
                        }
                        
                    });
                });
                
                if (c.isPresent()) {
                    client1 = c.get();
                }
                
                repo.save(client1);
                
                System.out.println("repo.findAll()");
                repo.findAll().forEach(System.out::println);
                
                System.out.println("projectRepository.findAll()");
                projectRepository.findAll().forEach(System.out::println);
                
                System.out.println("timeSheetRepository.findById(14)");
                Optional<TimeSheet> timeSheet = timeSheetRepository.findById(3);
                timeSheet.ifPresent((sheet) -> {
                    System.out.println(sheet);
                });
                
                System.out.println("timeSheetEntryRepository.findById(19)");
                Optional<TimeSheetEntry> timeSheetEntry = timeSheetEntryRepository.findById(19);
                System.out.println(timeSheetEntry);
          }
        };
    }
}
