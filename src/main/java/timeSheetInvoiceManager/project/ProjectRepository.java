package com.timeSheetInvoiceManager.app.project;

import com.timeSheetInvoiceManager.app.client.Client;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface ProjectRepository extends CrudRepository<Project, Integer> {
    List<Project> findByName(String name);

}
