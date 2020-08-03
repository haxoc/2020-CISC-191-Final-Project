/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timeSheetInvoiceManager;

import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;

import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import timeSheetInvoiceManager.client.Client;
import timeSheetInvoiceManager.client.ClientRepository;

import java.util.Optional;

import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.ListView;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

/**
 * @author xaboo
 */
@Component
@FxmlView("clients.fxml")
public class ClientsController implements Initializable {

    private ClientRepository clientRepository;
    private Client client;

    @FXML
    private Label weatherLabel;
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
    private ObservableList<String> clientList = FXCollections.observableArrayList();


    /**
     * @param clientRepository
     */
    @Autowired
    public ClientsController(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    /**
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("Clients Controller initialized");

        reloadClientListView();
    }

    /**
     * @param actionEvent
     */
    public void loadWeatherForecast(ActionEvent actionEvent) {
        this.weatherLabel.setText("weatherService.getWeatherForecast()");
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
        String previousID = client.getName();
        client.setName(this.txtName.getText());
        client.setRate(Double.parseDouble(this.txtRate.getText()));
        client.setAddress(this.txtAddress.getText());

        clientRepository.save(client);
        // Changing the name changes the ID, so we have to delete the extraneous client if the names are different
        if(!client.getName().equals(previousID)) {
            clientRepository.deleteById(previousID);
        }

        //TODO: update the projects controller whenever we save the client here
        reloadClientListView();
    }

    public void btnRemoveClicked(ActionEvent actionEvent) {
        Client client = getClientFromListView();

        clientRepository.delete(client);
        System.out.println("Client Removed: " + client);

        reloadClientListView();

    }

    public void btnAddClicked(ActionEvent actionEvent) {
        try {
            Double clientRate = Double.parseDouble(this.txtRate.getText());
            Client newClient = new Client(this.txtName.getText(), clientRate, this.txtAddress.getText());
            System.out.println("Client Added");

            clientRepository.save(newClient);
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
        Optional<Client> selectedClient = clientRepository.findById(clientName);

        if (!selectedClient.isPresent()) {
            throw new NullPointerException("client with name: " + clientName + " doesn't exist!");
        } else {
            return selectedClient.get();
        }
    }

    private void reloadClientListView() {
        listContacts.getItems().clear();
        listContacts.setItems(clientList);
        clientRepository.findAll().forEach((client) -> {
            System.out.println("Adding client to list: " + client.toString());
            clientList.add(client.getName());
        });
    }

}
