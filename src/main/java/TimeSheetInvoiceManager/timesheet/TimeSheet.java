package TimeSheetInvoiceManager.timesheet;

import TimeSheetInvoiceManager.project.Project;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Entity
public class TimeSheet {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE
    )
    private Integer id;

    private LocalDate beginDate;
    private LocalDate endDate;

    @OneToMany(
            mappedBy = "timeSheet",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER
    )
    @MapKey
    private final Map<String, TimeSheetEntry> entries = new HashMap<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "project_id")
    private Project project;

    public TimeSheet(Project project, LocalDate beginDate, LocalDate endDate) {
        this.project = project;
        this.beginDate = beginDate;
        this.endDate = endDate;
    }

    protected TimeSheet() {
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public TimeSheetEntry getEntry(TimeSheetEntry entry) {
        return entries.get(entry.getMapId());
    }

    //TODO: maybe check that entry is in the time sheet's month
    public void addEntry(TimeSheetEntry entry) {
        entries.put(entry.getMapId(), entry);
    }

    public Map<String, TimeSheetEntry> getEntries() {
        return entries;
    }

    public void removeEntry(TimeSheetEntry entry) {
        entries.remove(entry.getMapId());
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(LocalDate beginDate) {
        this.beginDate = beginDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
