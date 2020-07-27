package TimeSheetInvoiceManager;

import TimeSheetInvoiceManager.client.Client;
import TimeSheetInvoiceManager.client.ClientRepository;
import TimeSheetInvoiceManager.project.Project;
import TimeSheetInvoiceManager.project.ProjectRepository;
//import TimeSheetInvoiceManager.timesheet.TimeSheetEntry;
//import TimeSheetInvoiceManager.timesheet.TimeSheetEntryRepository;
import TimeSheetInvoiceManager.timesheet.TimeSheetEntry;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
/*

*/

@SpringBootApplication
public class App extends Application {
    private ConfigurableApplicationContext springContext;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() throws Exception {
        springContext = SpringApplication.run(App.class);

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("main.fxml"));

            primaryStage.setTitle("Timesheet Tracker - Invoice Manager");
            primaryStage.setScene(new Scene(root, 940, 680));
            primaryStage.show();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Bean
    public CommandLineRunner demo(ClientRepository repo, ProjectRepository projectRepository) {
        return (args) -> {
            Client client1 = new Client("Client 1", "some address");
            for (int i = 1; i <= 10; i++) {
                Project project = new Project("Project-" + i, 35, client1);
                client1.addProject(project);
            }
            repo.save(client1);
            Integer clientId = client1.getId();

            System.out.println("saved client");
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

            projectRepository.findAll().forEach(System.out::println);
        };
    }

    @Override
    public void stop() throws Exception {
        springContext.stop();
        System.exit(0);
    }
}
