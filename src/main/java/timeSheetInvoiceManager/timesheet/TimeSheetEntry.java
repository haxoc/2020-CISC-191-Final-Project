package timeSheetInvoiceManager.timesheet;

import javax.persistence.*;
import java.time.LocalDate;


@Entity
@SuppressWarnings("unused")
public class TimeSheetEntry {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Integer id;

    private LocalDate date;
    private String employeeName;
    private String description;
    private final String mapId;

    /**
     * How many hours this entry
     */
    private Double hours;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "timeSheet_id")
    private TimeSheet timeSheet;

    protected TimeSheetEntry() {
        mapId = "";
    }

    public TimeSheetEntry(LocalDate date, String employeeName, String description, Double hours, TimeSheet timeSheet) {
        this.date = date;
        this.employeeName = employeeName;
        this.description = description;
        this.hours = hours;
        this.timeSheet = timeSheet;
        this.mapId = date.toString() + "_" + employeeName;
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

    public Double getHours() {
        return hours;
    }

    public void setHours(Double hours) {
        this.hours = hours;
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
                ", hours=" + hours +
                ", mapID='" + mapId + '\'' +
                '}';
    }

}
