/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.timeSheetInvoiceManager.app;

import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;

import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.timeSheetInvoiceManager.app.client.Client;
import com.timeSheetInvoiceManager.app.client.ClientRepository;
import com.timeSheetInvoiceManager.app.project.Project;
import com.timeSheetInvoiceManager.app.project.ProjectRepository;
import com.timeSheetInvoiceManager.app.timesheet.TimeSheetEntry;
import java.time.LocalDate;
import java.util.Optional;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.ListView;
import javafx.fxml.Initializable;
import org.springframework.context.annotation.Bean;
import java.net.URL;
import java.util.Currency;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
/**
 *
 * @author xaboo
 */
@Component
@FxmlView("clients.fxml")
public class ClientsController implements Initializable{

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

    private WeatherService weatherService;

    /**
     *
     * @param weatherService
     * @param clientRepository
     */
    @Autowired
    public ClientsController(WeatherService weatherService, ClientRepository clientRepository) {
        this.weatherService = weatherService;
        this.clientRepository = clientRepository;
    }
    
    /**
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("Clients Controller initialized");
        /*
        Optional<Client> c = clientRepository.findById(1);
        c.ifPresent((client) -> {
            System.out.println(client.toString());
            System.out.println(client.getName());
            this.txtName.setText(client.getName());
            this.txtAddress.setText(client.getAddress());
        });
        */
        //Show names to Contact list
        listContacts.setItems(clientList);
        clientRepository.findAll().forEach((client) -> {
            clientList.add(client.getName());
        });
    }

    /**
     *
     * @param actionEvent
     */
    public void loadWeatherForecast(ActionEvent actionEvent) {
        this.weatherLabel.setText("weatherService.getWeatherForecast()");
    }

    @FXML
    public void clientListClicked(MouseEvent mouseEvent) {
        System.out.println("Client list Clicked");

        var selectedClient = listContacts.getSelectionModel().getSelectedItem();
        System.out.println(selectedClient);

        List<Client> c = clientRepository.findByName(selectedClient);
        c.forEach((client) -> {
            this.txtName.setText(client.getName());
            this.txtRate.setText(client.getRate().toString());
            this.txtAddress.setText(client.getAddress());
        });
    }

    /**
     *
     * @param actionEvent
     */
    public void btnSaveClicked(ActionEvent actionEvent) {
        var selectedClient = listContacts.getSelectionModel().getSelectedItem();
        System.out.println(selectedClient);

        List<Client> c = clientRepository.findByName(selectedClient);
        c.forEach((client) -> {
            client.setName(this.txtName.getText());
            client.setRate(Double.parseDouble(this.txtRate.getText()));
            client.setAddress(this.txtAddress.getText());
            clientRepository.save(client);
        });

        //Show names to Contact list       
        listContacts.getItems().clear();
        listContacts.setItems(clientList);
        clientRepository.findAll().forEach((client) -> {
            System.out.println(client.toString());
            clientList.add(client.getName());
        });
    }

    public void btnRemoveClicked(ActionEvent actionEvent) {
        System.out.println("Client Removed");
        var selectedClient = listContacts.getSelectionModel().getSelectedItem();
        System.out.println(selectedClient);

        List<Client> c = clientRepository.findByName(selectedClient);
        c.forEach((client) -> {
            clientRepository.delete(client);
        });

        //Show names to Contact list       
        listContacts.getItems().clear();
        listContacts.setItems(clientList);
        clientRepository.findAll().forEach((client) -> {
            System.out.println(client.toString());
            clientList.add(client.getName());
        });

    }

    public void btnAddClicked(ActionEvent actionEvent) {
        System.out.println("Client Added");
        Double clientRate = Double.parseDouble(this.txtRate.getText());
        Client newClient = new Client(this.txtName.getText(), clientRate, this.txtAddress.getText());
        clientRepository.save(newClient);
        System.out.println("Client saved");

        //Show names to Contact list       
        listContacts.getItems().clear();
        listContacts.setItems(clientList);
        clientRepository.findAll().forEach((client) -> {
            System.out.println(client.toString());
            clientList.add(client.getName());
        });
    }

    public void btnActiveClicked(ActionEvent actionEvent) {
        System.out.println("Client Status changed");
    }

}
