package com.timeSheetInvoiceManager.app.timesheet;

import org.springframework.data.repository.CrudRepository;

public interface TimeSheetRepository extends CrudRepository<TimeSheet, Integer> {
}
