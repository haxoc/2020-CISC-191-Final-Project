package timeSheetInvoiceManager.project;


import timeSheetInvoiceManager.client.Client;
import timeSheetInvoiceManager.timesheet.TimeSheet;


import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Entity
public class Project {
    public static final Project NONE = new Project("NONE", -1, Client.NONE);
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Integer id;

    private String name;

    private double rate;

    private boolean isActive;

    @OneToMany(
            cascade = CascadeType.ALL,
            mappedBy = "project",
            fetch = FetchType.EAGER
    )
    @MapKey(name = "beginDate")
    private Map<LocalDate, TimeSheet> timeSheets;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    private Client client;

    public Project(String name, double rate, Client client) {
        this.name = name;
        this.rate = rate;
        this.client = client;
        timeSheets = new HashMap<>();
    }

    protected Project() {
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
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

    protected void setId(Integer id) {
        this.id = id;
    }

    public void setTimeSheets(Map<LocalDate, TimeSheet> timeSheets) {
        this.timeSheets = timeSheets;
    }

    public Map<LocalDate, TimeSheet> getTimeSheets() {
        return timeSheets;
    }

    public TimeSheet getTimeSheet(LocalDate yearMonth) {
        return timeSheets.get(yearMonth);
    }

    public void addTimeSheet(TimeSheet timeSheet) {
        timeSheets.put(timeSheet.getBeginDate(), timeSheet);
    }

    public void removeTimeSheet(LocalDate beginDate) {
        timeSheets.remove(beginDate);
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
        client.addProject(this);
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", rate=" + rate +
                ", clientID=" + client.getId() +
                '}';
    }
}
