package com.timeSheetInvoiceManager.app.client;

import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface ClientRepository extends CrudRepository<Client, Integer> {
    List<Client> findByName(String name);
}
