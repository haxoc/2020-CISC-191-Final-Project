package com.timeSheetInvoiceManager.app.client;

import com.timeSheetInvoiceManager.app.project.Project;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity
public class Client {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE
    )
    private Integer id;

    private String name;
    private String address;

    private Double rate;
    private boolean isActive;

    @OneToMany(
            mappedBy = "client",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER
    )
    @MapKey(name = "name")
    private Map<String, Project> projects = new HashMap<>();


    protected Client() {
    }

    public Client(String name, String address, Double hourlyRate) {
        this.name = name;
        this.address = address;
        this.rate = hourlyRate;
        isActive = true;
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

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public Double getRate() {
        return rate;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
        if(!isActive) {
            projects.forEach((name, project) -> project.setActive(false));
        }
    }

    public void addProject(Project project) {
        projects.put(project.getName(), project);
    }

    public void removeProject(Project project) {
        projects.remove(project.getName());
    }

    public Map<String, Project> getProjects() {
        return projects;
    }

    public void setProjects(Map<String, Project> projects) {
        this.projects = projects;
    }

    @Override
    public String toString() {
        return "Client - override {" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", rate='" + rate + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
