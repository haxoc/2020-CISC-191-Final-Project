package TimeSheetInvoiceManager.project;

import TimeSheetInvoiceManager.client.Client;
import TimeSheetInvoiceManager.timesheet.TimeSheet;
import com.sun.istack.NotNull;

import javax.persistence.*;

@Entity
public class Project {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE
    )
    private Integer id;

    private String name;

    private double rate;

    @OneToOne(
            cascade = CascadeType.ALL,
            mappedBy = "project"
    )
    private TimeSheet timeSheet;

    @ManyToOne(
            fetch = FetchType.EAGER
    )
    @JoinColumn(name = "client_id")
    private Client client;

    public Project(String name, double rate, Client client) {
        this.name = name;
        this.rate = rate;
        this.client = client;
        timeSheet = new TimeSheet(this);
    }

    protected Project() {
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public TimeSheet getTimeSheet() {
        return timeSheet;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setTimeSheet(TimeSheet timeSheet) {
        this.timeSheet = timeSheet;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
        client.addProject(this);
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", rate=" + rate +
                '}';
    }
}
