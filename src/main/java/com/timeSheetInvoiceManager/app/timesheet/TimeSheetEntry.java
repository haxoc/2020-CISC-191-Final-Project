package com.timeSheetInvoiceManager.app.timesheet;

import javax.persistence.*;
import java.time.LocalDate;


@Entity
public class TimeSheetEntry {

    @Id
    @GeneratedValue(
            strategy = GenerationType.AUTO
    )
    private Integer id;

    private LocalDate date;
    private String employeeName;
    private String description;
    private final String mapId;

    /**
     * How many hours this entry
     */
    private double time;

    @ManyToOne(
            fetch = FetchType.EAGER
    )
    @JoinColumn(
            name = "timeSheet_id"
    )
    private TimeSheet timeSheet;

    protected TimeSheetEntry() {
        mapId = "";
    }

    public TimeSheetEntry(LocalDate date, String employeeName, String description, double time, TimeSheet timeSheet) {
        this.date = date;
        this.employeeName = employeeName;
        this.description = description;
        this.time = time;
        this.timeSheet = timeSheet;
        mapId = date.toString() + employeeName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public TimeSheet getTimeSheet() {
        return timeSheet;
    }

    public void setTimeSheet(TimeSheet timeSheet) {
        this.timeSheet = timeSheet;
    }

    public String getMapId() {
        return mapId;
    }

    @Override
    public String toString() {
        return "TimeSheetEntry{" +
                "id=" + id +
                ", date=" + date +
                ", employeeName='" + employeeName + '\'' +
                ", description='" + description + '\'' +
                ", time=" + time +
                '}';
    }
}
