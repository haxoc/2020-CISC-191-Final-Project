package TimeSheetInvoiceManager.client;

import TimeSheetInvoiceManager.project.Project;

import javax.persistence.*;
import java.util.HashMap;

@Entity
public class Client {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE
    )
    private Integer id;

    private String name;
    private String address;

    @OneToMany
    private HashMap<String, Project> projects = new HashMap<>();

    protected Client() {
    }

    public Client(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void addProject(Project project) {
        projects.put(project.getName(), project);
    }

    public void removeProject(Project project) {

    }
    public void setProjects(HashMap<String, Project> projects) {
        this.projects = projects;
    }

    public HashMap<String, Project> getProjects() {
        return projects;
    }

}
