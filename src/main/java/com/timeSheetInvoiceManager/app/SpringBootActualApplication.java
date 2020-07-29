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

import java.util.Optional;
import java.time.LocalDate;

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
    public CommandLineRunner demo(ClientRepository repo, ProjectRepository projectRepository) {
        return (args) -> {
            Client client1 = new Client("Client 2", "some address");
            for (int i = 1; i <= 10; i++) {
                Project project = new Project("Project-" + i, 35, client1);
                client1.addProject(project);
            }
            repo.save(client1);
            Integer clientId = client1.getId();

            System.out.println("saved client ID = " + clientId);
            Optional<Client> c = repo.findById(clientId);

            c.ifPresent((client) -> {
                client.getProjects().forEach((name, project) -> {
                    for (int i = 5; i <= 20; i++) {
                        project.getTimeSheet().addEntry(new TimeSheetEntry(LocalDate.now(), "employee " + i + " name",
                                "desc: " + i, Math.round(Math.random() * 10), project.getTimeSheet()));
                    }
                });
            });

            if (c.isPresent()) {
                client1 = c.get();
            }

            repo.save(client1);
            repo.findAll().forEach(System.out::println);
            projectRepository.findAll().forEach(System.out::println);
        };
    }
}
