package timeSheetInvoiceManager.invoice;

import timeSheetInvoiceManager.client.Client;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Represents an invoice, takes in all the entries between the start and end date and adds them up multiply by the client's
 * rate
 * @author haxoc, chesteraustin
 */
@Entity
public class Invoice {
    @SuppressWarnings("unused")
    public static final Invoice NONE = new Invoice(-1L,
            Client.NONE, LocalDate.now(), LocalDate.now(), LocalDate.now(), "new invoice");

    @Id
    /*
      This is the company wide number for the invoice
     */
    private Long invoiceNumber;

    private String clientName;

    private LocalDate beginServiceDate;
    private LocalDate endServiceDate;

    private LocalDate invoiceDate;

    private double amount;

    private double totalHours;

    private String description;

    public Invoice(Long invoiceNumber, Client client, LocalDate beginServiceDate, LocalDate endServiceDate,
                   LocalDate invoiceDate, String description) {
        this.invoiceNumber = invoiceNumber;
        this.beginServiceDate = beginServiceDate;
        this.endServiceDate = endServiceDate;
        this.invoiceDate = invoiceDate;
        this.description = description;
        this.clientName = client.getName();

        // Not the most efficient way to do this since we are checking all the time sheets regardless if they aren't for
        // this month, but this will make sure that we don't miss any entries
        client.getProjects().forEach((name, project) -> project.getTimeSheets().forEach((beginDate, timeSheet) -> timeSheet.getEntries().forEach((mapID, entry) -> {
                if (entry.getDate().isAfter(beginServiceDate) || entry.getDate().isEqual(beginServiceDate)) {
                    totalHours += entry.getHours();
                }
            })));
        this.amount = totalHours * client.getRate();
    }

    protected Invoice() {
    }

    public Long getInvoiceNumber() {
        return invoiceNumber;
    }

    @SuppressWarnings("unused")
    protected void setInvoiceNumber(Long invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getClientName() {
        return clientName;
    }

    @SuppressWarnings("unused")
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public LocalDate getBeginServiceDate() {
        return beginServiceDate;
    }

    public void setBeginServiceDate(LocalDate beginServiceDate) {
        this.beginServiceDate = beginServiceDate;
    }

    public LocalDate getEndServiceDate() {
        return endServiceDate;
    }

    public void setEndServiceDate(LocalDate endServiceDate) {
        this.endServiceDate = endServiceDate;
    }

    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public double getAmount() {
        return amount;
    }


    public void updateAmount(Client client) {
        this.amount = client.getRate() * this.totalHours;
    }

    public double getTotalHours() {
        return totalHours;
    }

    public void updateTotalHours(Client client, LocalDate beginServiceDate, LocalDate endServiceDate) {
        this.totalHours = 0;
        client.getProjects().forEach((name, project) -> project.getTimeSheets().forEach((beginDate, timeSheet) -> timeSheet.getEntries().forEach((mapID, entry) -> {
                if (entry.getDate().isAfter(beginServiceDate) || entry.getDate().isEqual(endServiceDate)) {
                    this.totalHours += entry.getHours();
                }
            })));
        setBeginServiceDate(beginServiceDate);
        setEndServiceDate(endServiceDate);
    }
    
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    
    @Override
    public String toString() {
        return "Invoice{" +
                "invoiceNumber=" + invoiceNumber+
                ", clientName='" + clientName + '\'' +
                ", beginServiceDate=" + beginServiceDate +
                ", endServiceDate=" + endServiceDate +
                ", invoiceDate=" + invoiceDate +
                ", description=" + description +
                ", totalHours=" + totalHours +
                ", amount=" + amount +
                '}';
    }
}