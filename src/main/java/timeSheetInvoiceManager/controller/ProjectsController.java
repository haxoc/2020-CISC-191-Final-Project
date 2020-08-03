/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timeSheetInvoiceManager;

import javafx.event.ActionEvent;
import timeSheetInvoiceManager.client.Client;
import timeSheetInvoiceManager.client.ClientRepository;
import timeSheetInvoiceManager.project.Project;
import timeSheetInvoiceManager.timesheet.TimeSheet;
import timeSheetInvoiceManager.timesheet.TimeSheetEntry;

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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

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
    private Project project;
    private TimeSheet timeSheet;
    private TimeSheetEntry timeSheetEntry;

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
    public ProjectsController(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
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
                project.getTimeSheets().forEach((beginDate, timeSheet) -> {
                    timeSheet.getEntries().forEach((id, timeSheetEntry) -> {
                        entryList.add(timeSheetEntry);
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
    }

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

        Optional<Client> selectedClient = clientRepository.findById(clientName);

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
