/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timeSheetInvoiceManager.controller;

import javafx.event.ActionEvent;
import timeSheetInvoiceManager.client.Client;
import timeSheetInvoiceManager.client.ClientRepository;
import timeSheetInvoiceManager.project.Project;
import timeSheetInvoiceManager.project.ProjectRepository;
import timeSheetInvoiceManager.services.MainServiceCoordinator;
import timeSheetInvoiceManager.timesheet.TimeSheet;
import timeSheetInvoiceManager.timesheet.TimeSheetEntry;
import timeSheetInvoiceManager.timesheet.TimeSheetRepository;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.DatePicker;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import timeSheetInvoiceManager.timesheet.TimeSheetEntryRepository;
//import java.util.List;

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
    private Project project;
    private ProjectRepository projectRepository;
    private TimeSheet timeSheet;
    private TimeSheetRepository timeSheetRepository;
    private TimeSheetEntry timeSheetEntry;
    private TimeSheetEntryRepository timeSheetEntryRepository;

    @FXML
    private Button btnSave;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnEdit;

    @FXML
    private TextField txtEmployee;
    @FXML
    private TextField txtDescription;
    @FXML
    private TextField txtDate;
    @FXML
    private DatePicker dpDateChooser;

    @FXML
    private ListView<String> listClients;
    @FXML
    private ListView<String> listProjects;
    @FXML
    private TableView<TimeSheetEntry> tableEntries;

    @FXML
    TableColumn<TimeSheetEntry, LocalDate> entryDateCol;
    @FXML
    TableColumn<TimeSheetEntry, String> entryDescCol;
    @FXML
    TableColumn<TimeSheetEntry, String> entryNameCol;

    // The table's data
    private ObservableList<TimeSheetEntry> entryList;
    private ObservableList<String> clientList = FXCollections.observableArrayList();
    private ObservableList<String> projectList = FXCollections.observableArrayList();

    /**
     * Initializes the controller class.
     */

    @Autowired
    public ProjectsController(ClientRepository clientRepository, TimeSheetRepository timeSheetRepository, TimeSheetEntryRepository timeSheetEntryRepository) {
        this.clientRepository = clientRepository;
        this.timeSheetRepository = timeSheetRepository;
        this.timeSheetEntryRepository = timeSheetEntryRepository;
        MainServiceCoordinator.getInstance().setProjectsController(this);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("Projects Controller initialized");
        listClients.setItems(clientList);

        ObservableList<String> projectList = FXCollections.observableArrayList("No projects");
        listProjects.setItems(projectList);

        reloadClientList();
        //System.out.println(projectList.toString());
    }

    @FXML
    public void clientListClicked(MouseEvent mouseEvent) {
        System.out.println("Client list Clicked inside projects");

        Client client = getClientFromListView();
        if (client != Client.NONE) {
            System.out.println("selected client: " + client);

            listProjects.getItems().clear();
            listProjects.setItems(projectList);

            client.getProjects().forEach((name, project) -> {
                projectList.add(project.getName());
            });
        }
    }

    @FXML
    public void projectListClicked(MouseEvent mouseEvent) {
        System.out.println("Client list Clicked inside projects");

        var selectedProjectName = listProjects.getSelectionModel().getSelectedItem();
        Client selectedClient = getClientFromListView();

        if (selectedClient != Client.NONE) {
            Project project = getClientFromListView().getProject(selectedProjectName);

            entryDateCol.setCellValueFactory(
                    new PropertyValueFactory<TimeSheetEntry, LocalDate>("date")
            );
            entryDescCol.setCellValueFactory(
                    new PropertyValueFactory<TimeSheetEntry, String>("description")
            );
            entryNameCol.setCellValueFactory(
                    new PropertyValueFactory<TimeSheetEntry, String>("employeeName")
            );

            entryList = FXCollections.observableArrayList();
            if (project != Project.NONE) {
                //System.out.println("project = " + project);
                //System.out.println("project.getTimeSheets() = " + project.getTimeSheets());
                //System.out.println("we want to check projects and get timeSheets");
                project.getTimeSheets().forEach((beginDate, timeSheet) -> {
                    //System.out.println("beginDate= " + beginDate);
                    //System.out.println("timeSheet= " + timeSheet);
                    //System.out.println("timeSheet.getEntries()= " + timeSheet.getEntries());
                    //System.out.println("we're going to get timesheets");
                    Map<Integer, TimeSheetEntry> timeSheetEntries = timeSheet.getEntries();
                    //System.out.println("timeSheet.getEntries()= " + timeSheet.getEntries());
                    //System.out.println("timeSheetEntries = " + timeSheetEntries);

                    timeSheetEntries.forEach((key, value) -> {
                        //System.out.println("Key = " + key + ", Value = " + value);
                        entryList.add(value);
                    });
                });
                tableEntries.setItems(entryList);
            }

        }
    }

    @FXML
    void btnDeleteClicked(ActionEvent event) {
        System.out.println("Delete clicked");
    }

    @FXML
    void btnEditClicked(ActionEvent event) {
        System.out.println("Edit clicked");
    }

    @FXML
    void btnAddClicked(ActionEvent event) {
        System.out.println("Add clicked");
        var selectedProject = listProjects.getSelectionModel().getSelectedItem();
        Project project = getClientFromListView().getProject(selectedProject);
            if (project != Project.NONE) {
                System.out.println("project = " + project);

                project.getTimeSheets().forEach((beginDate, timeSheet) -> {
                    System.out.println("timeSheet = " + timeSheet);                
                    TimeSheetEntry timeSheetEntryForm = new TimeSheetEntry(
                                                            dpDateChooser.getValue(), //date
                                                            txtEmployee.getText(), //employeeName
                                                            txtDescription.getText(), //description
                                                            Math.round(Math.random() * 10), //time
                                                            timeSheet, //timesheet object
                                                            project.getId() //projectid
                                                        );
                    System.out.println("timeSheetEntryForm = " + timeSheetEntryForm);
                    timeSheet.addEntry(timeSheetEntryForm);
                    timeSheetEntryRepository.save(timeSheetEntryForm);
                    System.out.println("timeSheetEntryRepository.saved");
                });
            }
            System.out.println("btnAddClicked finished");
    }
                    /*
                    if (timeSheet_linked.isPresent()) {
                    TimeSheetEntry timeSheetEntry = new TimeSheetEntry(
                                                                LocalDate.now(), //date
                                                                "employee 100 name", //employeeName
                                                                "desc: 100", //description
                                                                Math.round(Math.random() * 10), //time
                                                                timeSheet_linked.get(), //timesheet object
                                                                project.getId() //projectid
                                                            );
                        timeSheet_linked.get().addEntry(timeSheetEntry);
                        timeSheetEntryRepository.save(timeSheetEntry);
/*
                };
            }
        p.ifPresent((project) -> {
            Optional<TimeSheet> timeSheet_linked = timeSheetRepository.findById(project.getId());
            timeSheet_linked.get()
        
        })
            c.ifPresent((client) -> client.getProjects().forEach((name, project) -> {
  */
        /*

0
            System.out.println("project = " + project);
            System.out.println("project.getTimeSheet() = " + project.getTimeSheets());
            System.out.println("project.getTimeSheet() = " + project.getTimeSheets());

            System.out.println("project.getTimeSheetId() = " + project.getTimeSheetId());
            System.out.println("project.getId() = " + project.getId());

        //TimeSheet currentTimeSheet = project.getTimeSheet();
            //System.out.println("currentTimeSheet = " + currentTimeSheet);

            //TimeSheetEntry timeSheetEntry = new TimeSheetEntry(LocalDate.now(), this.txtEmployee.getText(), this.txtDescription.getText(), 10, currentTimeSheet, project.getId(), currentTimeSheet.getId());
            //TimeSheetEntry timeSheetEntry = new TimeSheetEntry(LocalDate.now(), "employee " + i + " name", "desc: " + i, Math.round(Math.random() * 10), timeSheetNew, project.getId(), timeSheetNew.getId());
            //TimeSheetEntry timeSheetEntry = new TimeSheetEntry(LocalDate.now(), "employee 01", "desc 01", 10, currentTimeSheet.getId(), 1);
            //TimeSheetEntry timeSheetEntry = new TimeSheetEntry(LocalDate.now(), "employee 01", "desc 01", 10, currentTimeSheet, 1);

            //System.out.println("timeSheetEntry = " + timeSheetEntry);
            
            //timeSheetEntryRepository.save(timeSheetEntry);
            //entryList.add(timeSheetEntry);

        });
        //tableEntries.setItems(entryList);
        */

    public void saveButtonClicked() {
        System.out.println("Save clicked");
    }

    public void timeEntriesClicked() {
        System.out.println("Save clicked");
    }

    private Client getClientFromListView() {
        String clientName = listClients.getSelectionModel().getSelectedItem();
        System.out.println(clientName);
        if(clientName == null) {
            return Client.NONE;
        }

        Optional<Client> selectedClient = clientRepository.findByName(clientName);
        
        return selectedClient.isPresent() ? selectedClient.get() : Client.NONE;
    }

    public void reloadClientList() {
        listClients.getItems().clear();
        listClients.setItems(clientList);
        clientRepository.findAll().forEach((client) -> {
            clientList.add(client.getName());
        });
    }
}
