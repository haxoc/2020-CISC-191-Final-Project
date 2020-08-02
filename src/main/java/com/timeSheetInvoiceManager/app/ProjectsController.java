/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.timeSheetInvoiceManager.app;

import com.timeSheetInvoiceManager.app.client.Client;
import com.timeSheetInvoiceManager.app.client.ClientRepository;
import com.timeSheetInvoiceManager.app.project.Project;
import com.timeSheetInvoiceManager.app.project.ProjectRepository;
import com.timeSheetInvoiceManager.app.timesheet.TimeSheet;
import com.timeSheetInvoiceManager.app.timesheet.TimeSheetRepository;
import com.timeSheetInvoiceManager.app.timesheet.TimeSheetEntry;
import com.timeSheetInvoiceManager.app.timesheet.TimeSheetEntryRepository;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javax.persistence.TypedQuery;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.util.Optional;

/**
 * FXML Controller class
 *
 * @author xaboo
 */
@Component
@FxmlView("projects.fxml")

public class ProjectsController implements Initializable {

    private ClientRepository clientRepository;
    private Client client;
    private ProjectRepository projectRepository;
    private Project project;
    private TimeSheetRepository timeSheetRepository;
    private TimeSheet timeSheet;
    private TimeSheetEntryRepository timeSheetEntryRepository;
    private TimeSheetEntry timeSheetEntry;

    @FXML
    private Button btnSave;
    @FXML
    private TextField txtName;
    @FXML
    private TextArea txtAddress;
    @FXML
    private TextField txtRate;
    @FXML
    private ListView<String> listClients;
    @FXML
    private ListView<String> listProjects;
    @FXML
    private TableView<TimeSheetEntry> tableEntries;
 
    @FXML
    TableColumn entryIdCol;
    @FXML
    TableColumn entryDateCol;
    @FXML
    TableColumn entryDescCol;
    @FXML
    TableColumn entryNameCol;
    @FXML
    TableColumn entryRateCol;
 
    // The table's data
    private ObservableList<TimeSheetEntry> entryList;
    private ObservableList<String> clientList = FXCollections.observableArrayList();
    private ObservableList<String> projectList = FXCollections.observableArrayList();

    /**
     * Initializes the controller class.
     */

    @Autowired
    public ProjectsController(ClientRepository clientRepository, ProjectRepository projectRepository, TimeSheetRepository timeSheetRepository, TimeSheetEntryRepository timeSheetEntryRepository) {
        this.clientRepository = clientRepository;
        this.projectRepository = projectRepository;
        this.timeSheetRepository = timeSheetRepository;
        this.timeSheetEntryRepository = timeSheetEntryRepository;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("Projects Controller initialized");
        listClients.setItems(clientList);

        ObservableList<String> projectList = FXCollections.observableArrayList("No projects");
        listProjects.setItems(projectList);

        clientRepository.findAll().forEach((client) -> {
            clientList.add(client.getName());
        });
        //System.out.println(projectList.toString());
    }    
    
    @FXML
    public void clientListClicked(MouseEvent mouseEvent) {
        System.out.println("Client list Clicked inside projects");

        var selectedClient = listClients.getSelectionModel().getSelectedItem();
        listProjects.setItems(projectList);

        System.out.println(selectedClient);

        List<Client> c = clientRepository.findByName(selectedClient);
        System.out.println("List<Client> c = " + c.toString());

        listProjects.getItems().clear();
        //projectList = FXCollections.observableArrayList("No projects");
        listProjects.setItems(projectList);

        c.forEach((client) -> {
            Map<String, Project> projects = client.getProjects();

            client.getProjects().forEach((name, project) -> {
                projectList.add(project.getName());
            });
        });
    }

    @FXML
    public void projectListClicked(MouseEvent mouseEvent) {
        System.out.println("Client list Clicked inside projects");

        var selectedProject = listProjects.getSelectionModel().getSelectedItem();
        System.out.println(selectedProject);

        List<Project> p = projectRepository.findByName(selectedProject);
        System.out.println("List<Project> p = " + p.toString());

        // Set up the table data
        entryIdCol.setCellValueFactory(
            new PropertyValueFactory<TimeSheetEntry,Integer>("id")
        );
        entryDateCol.setCellValueFactory(
            new PropertyValueFactory<TimeSheetEntry,LocalDate>("date")
        );
        entryDescCol.setCellValueFactory(
            new PropertyValueFactory<TimeSheetEntry,String>("description")
        );
        entryNameCol.setCellValueFactory(
            new PropertyValueFactory<TimeSheetEntry,String>("employeeName")
        );
        /*
        entryRateCol.setCellValueFactory(
            new PropertyValueFactory<TimeSheetEntry,String>("rate")
        );
        */
 
        
        //entryList = FXCollections.observableArrayList();
        Optional<TimeSheetEntry> tse = timeSheetEntryRepository.findById(19);
        System.out.println(tse);
        
        /*
        timeSheetRepository.findAll().forEach((entry) -> {
            System.out.println(entry);
            //entryList.add(entry);
        });
        */
        //Iterable<TimeSheetEntry> te = timeSheetEntryRepository.findAll();
        //System.out.println(te);

        //listEntries.getItems().clear();
        //timeEntriesList = FXCollections.observableArrayList("No Time Entries");
        //listEntries.setItems(timeEntriesList);
/*
        p.forEach((project) -> {
            Map<String, Project> projects = client.getProjects();

            client.getProjects().forEach((name, project) -> {
                projectList.add(project.getName());
            });
        });
*/
        /*
        listProjects.setItems(projectList);

        System.out.println(selectedClient);

        List<Client> c = clientRepository.findByName(selectedClient);
        System.out.println("List<Client> c = " + c.toString());

        listProjects.getItems().clear();
        projectList = FXCollections.observableArrayList("No projects");
        listProjects.setItems(projectList);

        c.forEach((client) -> {
            Map<String, Project> projects = client.getProjects();

            client.getProjects().forEach((name, project) -> {
                projectList.add(project.getName());
            });
        });
        */
}

    public void saveButtonClicked() {
        System.out.println("Save clicked");
    }
    public void timeEntriesClicked() {
        System.out.println("Save clicked");
    }
}
