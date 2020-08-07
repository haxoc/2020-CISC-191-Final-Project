package timeSheetInvoiceManager.services;

import timeSheetInvoiceManager.controller.ClientsController;
import timeSheetInvoiceManager.controller.InvoicesController;
import timeSheetInvoiceManager.controller.ProjectsController;

/**
 * This class lets different UI controllers interact with each other by holding instances of all the controllers,
 * we mainly use this to update the client lists across the tabs.
 * @author haxoc
 */
public class MainServiceCoordinator {

    private static final MainServiceCoordinator coordinator = new MainServiceCoordinator();

    private ClientsController clientsController;
    private ProjectsController projectsController;
    private InvoicesController invoicesController;

    private boolean setClientControllerOnce = false;
    private boolean setProjectsControllerOnce = false;
    private boolean setInvoicesControllerOnce = false;

    private MainServiceCoordinator() {
    }

    @SuppressWarnings("unused")
    public ClientsController getClientsController() {
        return clientsController;
    }

    public void setClientsController(ClientsController clientsController) {
        if (!setClientControllerOnce) {
            this.clientsController = clientsController;
            setClientControllerOnce = true;
        } else {
            System.out.println("Tried to set clients controller twice!");
        }
    }

    public ProjectsController getProjectsController() {
        return projectsController;
    }

    public void setProjectsController(ProjectsController projectsController) {
        if (!setProjectsControllerOnce) {
            this.projectsController = projectsController;
            setProjectsControllerOnce = true;
        } else {
            System.out.println("Tried to set projects controller twice!");
        }
    }

    public InvoicesController getInvoicesController() {
        return invoicesController;
    }

    public void setInvoicesController(InvoicesController invoicesController) {
        if(!setInvoicesControllerOnce) {
            this.invoicesController = invoicesController;
            setInvoicesControllerOnce = true;
        } else {
            System.out.println("Tried to set projects controller twice!");
        }
    }

    public static MainServiceCoordinator getInstance() {
        return coordinator;
    }
}
