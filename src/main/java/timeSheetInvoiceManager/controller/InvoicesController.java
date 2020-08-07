/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timeSheetInvoiceManager.controller;

import java.net.URL;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

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
import timeSheetInvoiceManager.invoice.Invoice;
import timeSheetInvoiceManager.invoice.InvoiceRepository;
import timeSheetInvoiceManager.services.MainServiceCoordinator;

@Component
@FxmlView("invoices.fxml")
public class InvoicesController implements Initializable {

    private final ClientRepository clientRepository;
    private final InvoiceRepository invoiceRepository;
    private static final DecimalFormat twoDecimals = new DecimalFormat("0.00");
    private Invoice invoice;
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
    private Button btnSaveInvoice;

    @FXML
    private Button btnRemoveInvoice;

    @FXML
    private Button btnGenerateInvoice;

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
    private ObservableList<Invoice> invoiceList = FXCollections.observableArrayList();

    /**
     * @param clientRepository
     */
    @Autowired
    public InvoicesController(ClientRepository clientRepository, InvoiceRepository invoiceRepository) {
        this.clientRepository = clientRepository;
        this.invoiceRepository = invoiceRepository;
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
        //Disable Save and Remove button
        btnGenerateInvoice.setDisable(true);
        btnRemoveInvoice.setDisable(true);
        btnSaveInvoice.setDisable(true);

        loadInvoicesList();
        reloadClientList();
    }

    @FXML
    public void btnChangeCompanyClicked(ActionEvent event) {
        //TODO: This should open a new window with a new controller, maybe we should have this as another tab instead?
        System.out.println("btnChangeCompanyClicked");

    }

    @FXML
    public void btnNewInvoiceClicked(ActionEvent event) {
        resetEntryTextFields();

        //Disable Save and Remove button
        btnGenerateInvoice.setDisable(false);
        btnSaveInvoice.setDisable(true);
        btnRemoveInvoice.setDisable(true);
        /*
        invoiceRepository.save(invoice.NONE);
        loadInvoicesList();
        */
    }

    @FXML
    void invoicesTableViewClicked(MouseEvent event) {
        System.out.println("invoicesTableViewClicked");
        Invoice selectedInvoice = invoicesTableView.getSelectionModel().getSelectedItem();
        //System.out.println(invoicesTableView);

        //Set information to forms
        if (selectedInvoice != null) {
            lblInvoiceNum.setText(Long.toString(selectedInvoice.getInvoiceNumber()));
            lblHours.setText(Double.toString(selectedInvoice.getTotalHours()));
            lblRate.setText(Double.toString(clientRepository.findByName(selectedInvoice.getClientName()).get().getRate()));
            lblAmount.setText(twoDecimals.format(selectedInvoice.getAmount()));
            invoiceDatePicker.setValue(selectedInvoice.getInvoiceDate());
            beginDatePicker.setValue(selectedInvoice.getBeginServiceDate());
            endDatePicker.setValue(selectedInvoice.getEndServiceDate());
            clientChooser.setValue(selectedInvoice.getClientName());
            clientChooser.setDisable(true);
            txtServiceDesc.setText(selectedInvoice.getDescription());

            btnGenerateInvoice.setDisable(true);
            btnRemoveInvoice.setDisable(false);
            btnSaveInvoice.setDisable(false);

            System.out.println(selectedInvoice);
        } else {
            btnGenerateInvoice.setDisable(false);
            btnRemoveInvoice.setDisable(true);
            btnSaveInvoice.setDisable(true);
        }
    }

    @FXML
    public void btnGenerateInvoiceClicked(ActionEvent event) {
        System.out.println("btnGenerateInvoiceClicked");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        String selectedClientName = clientChooser.getValue();
        if (selectedClientName != null) {
            Client selectedClient = clientRepository.findByName(selectedClientName).get();

            Invoice invoiceNew = new Invoice(timestamp.getTime(), selectedClient, beginDatePicker.getValue(),
                    endDatePicker.getValue(), invoiceDatePicker.getValue(), txtServiceDesc.getText());
            invoiceRepository.save(invoiceNew);

            btnGenerateInvoice.setDisable(false);
            btnRemoveInvoice.setDisable(true);
            btnSaveInvoice.setDisable(true);

            loadInvoicesList();
        }


        System.out.println("btnGenerateInvoiceClicked - finished");
    }

    @FXML
    public void btnRemoveInvoice(ActionEvent event) {
        System.out.println("btnRemoveInvoice");
        Invoice selectedInvoice = invoicesTableView.getSelectionModel().getSelectedItem();
        if (selectedInvoice != null) {
            invoiceRepository.delete(selectedInvoice);
        } else {
            btnGenerateInvoice.setDisable(false);
            btnRemoveInvoice.setDisable(true);
            btnSaveInvoice.setDisable(true);
        }

        resetEntryTextFields();
        loadInvoicesList();

        if (invoicesTableView.getSelectionModel().getSelectedItem() == null) {
            btnGenerateInvoice.setDisable(false);
            btnRemoveInvoice.setDisable(true);
            btnSaveInvoice.setDisable(true);
            resetEntryTextFields();
            loadInvoicesList();
        }
    }

    @FXML
    public void btnSaveClicked(ActionEvent event) {
        System.out.println("btnSaveClicked");
        Invoice selectedInvoice = invoicesTableView.getSelectionModel().getSelectedItem();
        if (selectedInvoice != null) {
            String selectedClientName = clientChooser.getValue();
            LocalDate invoiceDate = invoiceDatePicker.getValue();
            LocalDate beginDate = beginDatePicker.getValue();
            LocalDate endDate = endDatePicker.getValue();
            String description = txtServiceDesc.getText();

            if (selectedClientName != null && invoiceDate != null && beginDate != null && endDate != null) {
                Optional<Client> optSelectedClient = clientRepository.findByName(selectedClientName);

                if (optSelectedClient.isPresent()) {
                    Client selectedClient = optSelectedClient.get();
                    selectedInvoice.updateTotalHours(selectedClient, beginDatePicker.getValue(), endDatePicker.getValue());
                    selectedInvoice.updateAmount(selectedClient);
                    selectedInvoice.setInvoiceDate(invoiceDate);
                    invoiceRepository.save(selectedInvoice);
                }
            }
        }
        loadInvoicesList();

        //update form with new values
        lblHours.setText(Double.toString(selectedInvoice.getTotalHours()));
        lblAmount.setText(twoDecimals.format(selectedInvoice.getAmount()));

        System.out.println("btnSaveClicked - finished");
    }

    public void reloadClientList() {
        System.out.println("reloadClientList");
        clientChooser.getItems().clear();
        clientChooser.setItems(clientList);
        clientRepository.findAll().forEach((client) -> {
            clientList.add(client.getName());
        });
    }

    private void loadInvoicesList() {
        invoiceList = FXCollections.observableArrayList();

        Iterable<Invoice> allInvoice = invoiceRepository.findAll();
        allInvoice.forEach((invoice) -> {
            invoiceList.add(invoice);
        });

        invoicesTableView.setItems(invoiceList);
    }

    private void resetEntryTextFields() {
        //Reset fields to create a new invoice
        lblInvoiceNum.setText("");
        lblHours.setText("");
        lblRate.setText("");
        lblAmount.setText("");
        invoiceDatePicker.setValue(null);
        beginDatePicker.setValue(null);
        endDatePicker.setValue(null);
        clientChooser.setValue(null);
        clientChooser.setDisable(false);
        txtServiceDesc.setText(null);
    }
}
