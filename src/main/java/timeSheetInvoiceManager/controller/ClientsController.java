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
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import timeSheetInvoiceManager.client.Client;
import timeSheetInvoiceManager.client.ClientRepository;
import timeSheetInvoiceManager.services.MainServiceCoordinator;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controls the clients tab UI, lets the user add remove and edit clients and their hourly rate.
 *
 * @author chesteraustin, haxoc
 */
@SuppressWarnings("unused")
@Component
@FxmlView("clients.fxml")
public class ClientsController implements Initializable {

    private final ClientRepository clientRepository;

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
     * @param clientRepository autowired
     */
    @Autowired
    public ClientsController(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
        MainServiceCoordinator.getInstance().setClientsController(this);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        reloadClientListView();
        resetInputFields();
    }

    @FXML
    public void clientListClicked(MouseEvent mouseEvent) {
        Client client = getClientFromListView();

        if (client != Client.NONE) {
            this.txtName.setText(client.getName());
            this.txtName.setDisable(false);
            this.txtRate.setText(client.getRate().toString());
            this.txtRate.setDisable(false);
            this.txtAddress.setText(client.getAddress());
            this.txtAddress.setDisable(false);
        } else {
            resetInputFields();
        }
    }


    public void btnSaveClicked(ActionEvent actionEvent) {
        Client client = getClientFromListView();
        if (client != Client.NONE) {
            try {
                client.setName(this.txtName.getText());
                //TODO: make sure that the app doesn't crash if we can't parse this double
                client.setRate(Double.parseDouble(txtRate.getText()));
                client.setAddress(this.txtAddress.getText());

                clientRepository.save(client);
            } catch (NumberFormatException e) {
                System.out.println(e.toString());
                System.out.println("Rate is either empty or not a number");
            }

            //Update the projects controller whenever we save the client here
            reloadClientListView();
            updateClientListViewInOtherControllers();
        } else {
            resetInputFields();
        }
    }

    public void btnRemoveClicked(ActionEvent actionEvent) {
        Client client = getClientFromListView();
        if (client != Client.NONE) {
            clientRepository.delete(client);
        }

        reloadClientListView();
        resetInputFields();
        updateClientListViewInOtherControllers();
    }

    public void btnAddClicked(ActionEvent actionEvent) {
        try {
            if (clientRepository.findByName("NEW CLIENT").isEmpty()) {
                clientRepository.save(new Client("NEW CLIENT", -1.0, ""));
            }

        } catch (NumberFormatException e) {
            System.out.println("hourly rate is not a double");
            System.out.println(e.toString());
        } catch (NullPointerException e) {
            System.out.println("Rate is empty");
            System.out.println(e.toString());
        }

        //Show names to Contact list
        reloadClientListView();
    }

    public void btnActiveClicked(ActionEvent actionEvent) {
        System.out.println("Client Status changed");
    }

    private Client getClientFromListView() {
        String clientName = listContacts.getSelectionModel().getSelectedItem();
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
        clientRepository.findAll().forEach((client) -> clientList.add(client.getName()));
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
        txtName.setPromptText("Name");
        txtName.setDisable(true);

        txtRate.setText("");
        txtRate.setPromptText("Hourly Rate");
        txtRate.setDisable(true);

        txtAddress.setText("");
        txtAddress.setPromptText("");
        txtAddress.setDisable(true);
    }

}
