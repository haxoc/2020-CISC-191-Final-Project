package TimeSheetInvoiceManager.invoice;

import TimeSheetInvoiceManager.client.Client;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Invoice {

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
                    totalHours += entry.getTime();
                }
            })));
        this.amount = totalHours * client.getHourlyRate();
    }

    protected Invoice() {
    }

    public Long getInvoiceNumber() {
        return invoiceNumber;
    }

    protected void setInvoiceNumber(Long invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getClientName() {
        return clientName;
    }

    public LocalDate getBeginServiceDate() {
        return beginServiceDate;
    }

    public LocalDate getEndServiceDate() {
        return endServiceDate;
    }

    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public double getAmount() {
        return amount;
    }

    public double getTotalHours() {
        return totalHours;
    }

    public String getDescription() {
        return description;
    }
}
