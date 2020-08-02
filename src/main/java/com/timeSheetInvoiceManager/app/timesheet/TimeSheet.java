package com.timeSheetInvoiceManager.app.timesheet;

import com.timeSheetInvoiceManager.app.project.Project;
import com.timeSheetInvoiceManager.app.timesheet.TimeSheetEntry;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity
public class TimeSheet {

    @Id
    private Integer id;

    @OneToMany(
            mappedBy = "timeSheet",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER
    )
    @MapKey
    private final Map<String, TimeSheetEntry> entries = new HashMap<>();

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private Project project;

    public TimeSheet(Project project) {
        this.project = project;
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
        return entries.get(entry.getId());
    }

    public void addEntry(TimeSheetEntry entry) {
        System.out.println("Entry Id: " + entry.getId());
        entries.put(entry.getMapId(), entry);
    }

    public Map<String, TimeSheetEntry> getEntries() {
        return entries;
    }

    public void removeEntry(TimeSheetEntry entry) {
        entries.remove(entry.getId());
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "TimeSheet{" +
                "id=" + id +
                ", project='" + project + '\'' +
                '}';
    }
}
