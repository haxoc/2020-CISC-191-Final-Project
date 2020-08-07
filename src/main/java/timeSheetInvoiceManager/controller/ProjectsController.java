/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timeSheetInvoiceManager.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import timeSheetInvoiceManager.client.Client;
import timeSheetInvoiceManager.client.ClientRepository;
import timeSheetInvoiceManager.project.Project;
import timeSheetInvoiceManager.services.MainServiceCoordinator;
import timeSheetInvoiceManager.timesheet.TimeSheet;
import timeSheetInvoiceManager.timesheet.TimeSheetEntry;

import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;


/**
 * FXML Controller class
 *
 * @author xaboo
 */
@Component
@FxmlView("projects.fxml")

public class ProjectsController implements Initializable {

    private final ClientRepository clientRepository;

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
    private TextField txtHours;
    @FXML
    private DatePicker dpDateChooser;

    @FXML
    private TextField txtProjectName;

    @FXML
    private ListView<String> listViewClients;
    @FXML
    private ListView<String> listViewProjects;

    @FXML
    private TableView<TimeSheetEntry> tableEntries;

    @FXML
    TableColumn<TimeSheetEntry, LocalDate> entryDateCol;
    @FXML
    TableColumn<TimeSheetEntry, String> entryDescCol;
    @FXML
    TableColumn<TimeSheetEntry, String> entryNameCol;
    @FXML
    TableColumn<TimeSheetEntry, Double> entryHoursCol;

    // The table's data
    private ObservableList<TimeSheetEntry> entryList;
    private final ObservableList<String> clientList = FXCollections.observableArrayList();
    private final ObservableList<String> projectList = FXCollections.observableArrayList();

    /**
     * Initializes the controller class.
     */

    @Autowired
    public ProjectsController(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
        MainServiceCoordinator.getInstance().setProjectsController(this);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listViewClients.setItems(clientList);

        ObservableList<String> projectList = FXCollections.observableArrayList("No projects");
        listViewProjects.setItems(projectList);

        entryDateCol.setCellValueFactory(
                new PropertyValueFactory<>("date")
        );
        entryDescCol.setCellValueFactory(
                new PropertyValueFactory<>("description")
        );
        entryNameCol.setCellValueFactory(
                new PropertyValueFactory<>("employeeName")
        );
        entryHoursCol.setCellValueFactory(
                new PropertyValueFactory<>("hours")
        );

        entryDateCol.setSortType(TableColumn.SortType.DESCENDING);
        reloadClientList();
    }

    @FXML
    public void clientListClicked(MouseEvent mouseEvent) {
        Client client = getClientFromListView();
        loadProjectList(client);
    }

    @FXML
    public void projectListClicked(MouseEvent mouseEvent) {
        var selectedProjectName = listViewProjects.getSelectionModel().getSelectedItem();
        Client selectedClient = getClientFromListView();

        if (selectedClient != Client.NONE) {
            Project project = getClientFromListView().getProject(selectedProjectName);

            loadEntryList(project);
        }
    }

    @FXML
    void btnDeleteClicked(ActionEvent event) {
        TimeSheetEntry selectedEntry = tableEntries.getSelectionModel().getSelectedItem();
        if (selectedEntry != null) {
            Client selectedClient = getClientFromListView();
            Project selectedProject = selectedClient.getProject(listViewProjects.getSelectionModel().getSelectedItem());

            selectedProject.getTimeSheet(selectedEntry.getDate().withDayOfMonth(1)).removeEntry(selectedEntry);
            clientRepository.save(selectedClient);
            loadEntryList(selectedProject);
        }
    }

    @FXML
    void btnAddClicked(ActionEvent event) {
        var selectedProjectName = listViewProjects.getSelectionModel().getSelectedItem();
        Client selectedClient = getClientFromListView();
        Project project = selectedClient.getProject(selectedProjectName);
        if (project != Project.NONE) {
            try {
                LocalDate entryDate = dpDateChooser.getValue();
                try {
                    TimeSheet timeSheet = project.getTimeSheet(entryDate.withDayOfMonth(1));
                    timeSheet.addEntry(
                            new TimeSheetEntry(
                                    entryDate,
                                    txtEmployee.getText(),
                                    txtDescription.getText(),
                                    Double.parseDouble(txtHours.getText()),
                                    timeSheet
                            )
                    );
                    clientRepository.save(selectedClient);
                } catch (NumberFormatException e) {
                    System.out.println("Hours worked must be a double");
                    System.out.println(e.toString());
                }
                loadEntryList(project);
            } catch (NullPointerException e) {
                System.out.println("There must be a date");
                System.out.println(e.toString());
            }
        }
    }

    @FXML
    public void btnAddProjectClicked(ActionEvent event) {
        //TODO: make this work, open an alert box with a prompt for the name
        Client client = getClientFromListView();
        String projectName = txtProjectName.getText();
        if (client != Client.NONE && projectName != null) {
            client.addProject(new Project(projectName, client));
            clientRepository.save(client);
            loadProjectList(client);
            txtProjectName.clear();
        }
    }

    @FXML
    public void btnRemoveProjectClicked(ActionEvent event) {
        Client client = getClientFromListView();
        String selectedProjectName = listViewProjects.getSelectionModel().getSelectedItem();
        if (client != Client.NONE && selectedProjectName != null) {
            client.removeProject(client.getProject(selectedProjectName));
            clientRepository.save(client);
            loadProjectList(client);
            tableEntries.getItems().clear();
        }
    }

    private Client getClientFromListView() {
        String clientName = listViewClients.getSelectionModel().getSelectedItem();
        if (clientName == null) {
            return Client.NONE;
        }

        Optional<Client> selectedClient = clientRepository.findByName(clientName);

        return selectedClient.orElse(Client.NONE);
    }

    public void reloadClientList() {
        listViewClients.getItems().clear();
        listViewClients.setItems(clientList);
        clientRepository.findAll().forEach((client) -> clientList.add(client.getName()));
    }

    private void loadProjectList(Client client) {
        if (client != Client.NONE) {
            listViewProjects.getItems().clear();
            listViewProjects.setItems(projectList);

            client.getProjects().forEach((name, project) -> projectList.add(project.getName()));
        } else {
            listViewProjects.getItems().clear();
        }
    }

    private void loadEntryList(Project project) {
        entryList = FXCollections.observableArrayList();
        if (project != Project.NONE) {
            project.getTimeSheets().forEach((yearMonth, timeSheet) -> timeSheet.getEntries().forEach((mapId, entry) -> entryList.add(entry)));
            tableEntries.setItems(entryList);
            tableEntries.getSortOrder().add(entryDateCol);
        } else {
            tableEntries.getItems().clear();
        }
    }
}
