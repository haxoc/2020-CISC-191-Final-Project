package TimeSheetInvoiceManager;

import TimeSheetInvoiceManager.client.Client;
import TimeSheetInvoiceManager.client.ClientRepository;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;


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
        TabPane tabPane = new TabPane();

        String javaVersion = System.getProperty("java.version");
        String javafxVersion = System.getProperty("javafx.version");
        Label l = new Label("JavaFX " + javafxVersion + ", running on Java " + javaVersion + ".");

        Tab tab1 = new Tab("Invoices", new Label("Show all Invoices \n" + l));
        Tab tab2 = new Tab("Clients", new Label("Show all Clients \n" + l));
        Tab tab3 = new Tab("Projects", new Label("Show all Projects \n" + l));

        tabPane.getTabs().add(tab1);
        tabPane.getTabs().add(tab2);
        tabPane.getTabs().add(tab3);

        Scene scene = new Scene(new StackPane(tabPane), 640, 480);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Timesheet Tracker - Invoice Manager");

        primaryStage.show();
    }

    @Bean
    public CommandLineRunner demo(ClientRepository repo) {
        return (args) -> {
            Iterable<Client> clientsFromRepo = repo.findAll();
            for (Client c : clientsFromRepo) {
                System.out.printf("Client id %d, name %s\n", c.getId(), c.getName());
            }

            repo.save(new Client("Guy", "San Diego"));
            repo.save(new Client("Jerry", "Lost Angeles"));
            repo.save(new Client("Mark", "New York"));
            System.out.println("Saved Customers");

            clientsFromRepo = repo.findAll();
            for (Client c : clientsFromRepo) {
                System.out.printf("Client id %d, name %s\n", c.getId(), c.getName());
            }
        };
    }

    @Override
    public void stop() throws Exception {
        springContext.stop();
    }
}
