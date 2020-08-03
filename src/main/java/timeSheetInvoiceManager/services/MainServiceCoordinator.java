package timeSheetInvoiceManager.services;

import timeSheetInvoiceManager.controller.ClientsController;
import timeSheetInvoiceManager.controller.InvoicesController;
import timeSheetInvoiceManager.controller.ProjectsController;

public class MainServiceCoordinator {

    private ClientsController clientsController;
    private ProjectsController projectsController;
    private InvoicesController invoicesController;

    private boolean setClientControlleronce = false;

    private MainServiceCoordinator() {
    }



}
