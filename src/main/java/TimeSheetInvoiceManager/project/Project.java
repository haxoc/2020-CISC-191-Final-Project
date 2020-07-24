package TimeSheetInvoiceManager.project;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Project {
    @Id
    private String name;

    private double rate;

    protected Project() {
    }

    public Project(String name, double rate) {
        this.name = name;
        this.rate = rate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
