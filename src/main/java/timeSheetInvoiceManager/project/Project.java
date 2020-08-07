package timeSheetInvoiceManager.project;

import timeSheetInvoiceManager.client.Client;
import timeSheetInvoiceManager.timesheet.TimeSheet;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * A project has multiple timesheets (one for each month) and belongs to a single client, basically a sane way of grouping
 * timesheet entries
 * @author haxoc, chester austin
 */
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
            fetch = FetchType.EAGER,
            orphanRemoval = true
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

    public Project(String name, Client client) {
        this(name, client.getRate(), client);
    }

    protected Project() {
    }

    @SuppressWarnings("unused")
    public double getRate() {
        return rate;
    }

    @SuppressWarnings("unused")
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

    @SuppressWarnings("unused")
    protected void setId(Integer id) {
        this.id = id;
    }

    public Map<LocalDate, TimeSheet> getTimeSheets() {
        return timeSheets;
    }

    public TimeSheet getTimeSheet(LocalDate yearMonth) {
        if (!timeSheets.containsKey(yearMonth)) {
            timeSheets.put(yearMonth, new TimeSheet(this, yearMonth, yearMonth.plusMonths(1).minusDays(1)));
        }
        return timeSheets.get(yearMonth);
    }

    @SuppressWarnings("unused")
    public void addTimeSheet(TimeSheet timeSheet) {
        timeSheets.put(timeSheet.getBeginDate(), timeSheet);
    }

    @SuppressWarnings("unused")
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

    @SuppressWarnings("unused")
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
