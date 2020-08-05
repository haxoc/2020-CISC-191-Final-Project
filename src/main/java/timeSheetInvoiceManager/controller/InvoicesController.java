/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timeSheetInvoiceManager.controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import timeSheetInvoiceManager.client.ClientRepository;
import timeSheetInvoiceManager.invoice.Invoice;
import timeSheetInvoiceManager.services.MainServiceCoordinator;

@Component
@FxmlView("invoices.fxml")
public class InvoicesController implements Initializable {

    private final ClientRepository clientRepository;
    @FXML
    private Label lblInvoiceNum;

    @FXML
    private Label lblHours;

    @FXML
    private Label lblRate;

    @FXML
    private Label lblAmount;

    @FXML
    private DatePicker invoiceDatePicker;

    @FXML
    private DatePicker beginDatePicker;

    @FXML
    private DatePicker endDatePicker;

    @FXML
    private ChoiceBox<String> clientChooser;

    @FXML
    private TextArea txtServiceDesc;

    @FXML
    private CheckBox checkBoxReceived;

    @FXML
    private TableView<Invoice> invoicesTableView;

    @FXML
    private TableColumn<Invoice, Long> invoiceNumCol;
    @FXML
    private TableColumn<Invoice, String> invoiceClientNameCol;
    @FXML
    private TableColumn<Invoice, LocalDate> invoiceDateCol;
    @FXML
    private TableColumn<Invoice, Double> invoiceAmountCol;

    private final ObservableList<String> clientList = FXCollections.observableArrayList();

    /**
     * @param clientRepository
     */
    @Autowired
    public InvoicesController(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
        MainServiceCoordinator.getInstance().setInvoicesController(this);
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("Invoice View Controller initialized");

        invoiceNumCol.setCellValueFactory(
                new PropertyValueFactory<Invoice, Long>("invoiceNumber")
        );
        invoiceClientNameCol.setCellValueFactory(
                new PropertyValueFactory<Invoice, String>("clientName")
        );
        invoiceDateCol.setCellValueFactory(
                new PropertyValueFactory<Invoice, LocalDate>("invoiceDate")
        );
        invoiceAmountCol.setCellValueFactory(
                new PropertyValueFactory<Invoice, Double>("amount")
        );

        /*TODO: load the list chooser with all the clients, also have to update this when adding and removing client
         *  from the clients controller
         */
        reloadClientList();
    }

    @FXML
    public void btnChangeCompanyClicked(ActionEvent event) {
        //TODO: This should open a new window with a new controller, maybe we should have this as another tab instead?
    }

    @FXML
    public void btnNewInvoiceClicked(ActionEvent event) {
    }

    @FXML
    public void btnGenerateInvoiceClicked(ActionEvent event) {
    }

    @FXML
    public void btnRemoveInvoice(ActionEvent event) {
    }

    @FXML
    public void btnSaveClicked(ActionEvent event) {
    }

    public void reloadClientList() {
        clientChooser.getItems().clear();
        clientChooser.setItems(clientList);
        clientRepository.findAll().forEach((client) -> {
            clientList.add(client.getName());
        });
    }

}
