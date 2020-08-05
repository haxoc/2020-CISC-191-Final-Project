/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timeSheetInvoiceManager.controller;

import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.fxml.FXML;

import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import timeSheetInvoiceManager.client.Client;
import timeSheetInvoiceManager.client.ClientRepository;

import java.util.Optional;

import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import timeSheetInvoiceManager.services.MainServiceCoordinator;
import timeSheetInvoiceManager.timesheet.TimeSheetEntry;

/**
 * @author xaboo
 */
@Component
@FxmlView("clients.fxml")
public class ClientsController implements Initializable {

    private ClientRepository clientRepository;

    @FXML
    private Button btnSave;
    @FXML
    private TextField txtName;
    @FXML
    private TextArea txtAddress;
    @FXML
    private TextField txtRate;
    @FXML
    private ListView<String> listContacts;
    private final ObservableList<String> clientList = FXCollections.observableArrayList();

    /**
     * @param clientRepository
     */
    @Autowired
    public ClientsController(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
        MainServiceCoordinator.getInstance().setClientsController(this);
    }

    /**
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("Clients Controller initialized");

        reloadClientListView();
        resetInputFields();
    }

    @FXML
    public void clientListClicked(MouseEvent mouseEvent) {
        System.out.println("Client list Clicked");
        Client client = getClientFromListView();

        this.txtName.setText(client.getName());
        this.txtRate.setText(client.getRate().toString());
        this.txtAddress.setText(client.getAddress());
    }

    /**
     * @param actionEvent
     */
    public void btnSaveClicked(ActionEvent actionEvent) {
        Client client = getClientFromListView();
        if (client != Client.NONE) {
            Integer previousID = client.getId();
            try {
                client.setName(this.txtName.getText());
                //TODO: make sure that the app doesn't crash if we can't parse this double
                client.setRate(Double.parseDouble(txtRate.getText()));
                client.setAddress(this.txtAddress.getText());

                clientRepository.save(client);
                // Changing the name changes the ID, so we have to delete the extraneous client if the names are different
                if (!client.getName().equals(previousID)) {
                    clientRepository.deleteById(previousID);
                }
            } catch (NumberFormatException e) {
                System.out.println(e);
                System.out.println("Rate is either empty or not a number");
            }

            //Update the projects controller whenever we save the client here
            reloadClientListView();
            updateClientListViewInOtherControllers();
        }
    }

    public void btnRemoveClicked(ActionEvent actionEvent) {
        Client client = getClientFromListView();
        if (client != Client.NONE) {
            clientRepository.delete(client);
            System.out.println("Client Removed: " + client);
        }

        reloadClientListView();
        resetInputFields();
        updateClientListViewInOtherControllers();
    }

    public void btnAddClicked(ActionEvent actionEvent) {
        try {

            clientRepository.save(Client.NONE);
            System.out.println("Client saved");

        } catch (NumberFormatException e) {
            System.out.println("hourly rate is not a double");
            System.out.println(e);
        } catch (NullPointerException e) {
            System.out.println("Rate is empty");
            System.out.println(e);
        }

        //Show names to Contact list
        reloadClientListView();
    }

    public void btnActiveClicked(ActionEvent actionEvent) {
        System.out.println("Client Status changed");
    }

    private Client getClientFromListView() {
        String clientName = listContacts.getSelectionModel().getSelectedItem();
        System.out.println(clientName);
        if (clientName != null) {
            Optional<Client> selectedClient = clientRepository.findByName(clientName);
            if (selectedClient.isPresent()) {
                return selectedClient.get();
            }
        }
        return Client.NONE;
    }

    private void reloadClientListView() {
        listContacts.getItems().clear();
        listContacts.setItems(clientList);
        clientRepository.findAll().forEach((client) -> {
            System.out.println("Adding client to list: " + client.toString());
            clientList.add(client.getName());
        });
    }

    private void updateClientListViewInOtherControllers() {
        ProjectsController projectsControllerInstance = MainServiceCoordinator.getInstance().getProjectsController();
        if (projectsControllerInstance != null) {
            projectsControllerInstance.reloadClientList();
        }

        InvoicesController invoicesControllerInstance = MainServiceCoordinator.getInstance().getInvoicesController();
        if (invoicesControllerInstance != null) {
            invoicesControllerInstance.reloadClientList();
        }

    }

    private void resetInputFields() {
        txtName.setText("");
        txtAddress.setText("");
        txtRate.setText("");
        txtName.setPromptText("Name");
        txtAddress.setPromptText("");
        txtRate.setPromptText("Hourly Rate");
    }

}
