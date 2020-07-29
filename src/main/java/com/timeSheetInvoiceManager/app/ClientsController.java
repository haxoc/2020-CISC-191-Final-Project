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
import javafx.scene.control.TextField;
import org.springframework.context.annotation.Bean;

/**
 *
 * @author xaboo
 */
@Component
@FxmlView("clients.fxml")
public class ClientsController {

    private ClientRepository clientRepository;

    @FXML
    private Label weatherLabel;
    @FXML
    private Button btnSave;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtRate;

    private WeatherService weatherService;

    @Autowired
    public ClientsController(WeatherService weatherService, ClientRepository clientRepository) {
        this.weatherService = weatherService;
        this.clientRepository = clientRepository;
    }

    public void loadWeatherForecast(ActionEvent actionEvent) {
        this.weatherLabel.setText("weatherService.getWeatherForecast()");
    }
    public void saveButtonClicked(ActionEvent actionEvent) {
        System.out.println("Save clicked");
        System.out.println("I am calling clientRepository to console from SaveButton");
        clientRepository.findAll().forEach(System.out::println);
        var firstClient = clientRepository.findById(1);
        this.txtName.setText(firstClient.toString());
        this.weatherLabel.setText(weatherService.getWeatherForecast());

    }

}
