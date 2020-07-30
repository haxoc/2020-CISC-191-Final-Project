/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.timeSheetInvoiceManager.app;

import javafx.event.ActionEvent;
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
import javafx.fxml.Initializable;
import org.springframework.context.annotation.Bean;
import java.net.URL;
import java.util.ResourceBundle;

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

    private WeatherService weatherService;

    @Autowired
    public ClientsController(WeatherService weatherService, ClientRepository clientRepository) {
        this.weatherService = weatherService;
        this.clientRepository = clientRepository;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("Clients Controller initialized");
        Optional<Client> c = clientRepository.findById(1);
        c.ifPresent((client) -> {
            System.out.println(client.toString());
            System.out.println(client.getName());
            this.txtName.setText(client.getName());
            this.txtAddress.setText("Client Address");
        });
    }    

    public void loadWeatherForecast(ActionEvent actionEvent) {
        this.weatherLabel.setText("weatherService.getWeatherForecast()");
    }
    public void saveButtonClicked(ActionEvent actionEvent) {
        System.out.println("Save clicked");
        System.out.println("I am calling clientRepository to console from SaveButton");
    }

}
