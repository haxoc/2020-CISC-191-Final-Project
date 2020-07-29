package com.timeSheetInvoiceManager.app.timesheet;

import org.springframework.data.repository.CrudRepository;

public interface TimeSheetEntryRepository extends CrudRepository<TimeSheetEntry, Integer> {
}
