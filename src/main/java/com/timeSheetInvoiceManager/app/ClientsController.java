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
/**
 *
 * @author xaboo
 */
@Component
@FxmlView("clients.fxml")
public class ClientsController {

    @FXML
    private Label weatherLabel;
    private WeatherService weatherService;
    private Button btnSave;

    
    @Autowired
    public ClientsController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    public void loadWeatherForecast(ActionEvent actionEvent) {
        this.weatherLabel.setText("weatherService.getWeatherForecast()");
    }
    public void saveButtonClicked() {
        System.out.println("Save clicked");
        //btnSave.setText("Saved");
    }

}
