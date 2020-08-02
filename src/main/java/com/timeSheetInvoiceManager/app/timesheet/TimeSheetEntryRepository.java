package com.timeSheetInvoiceManager.app.timesheet;

import org.springframework.data.repository.CrudRepository;
import com.timeSheetInvoiceManager.app.timesheet.TimeSheet;

public interface TimeSheetEntryRepository extends CrudRepository<TimeSheetEntry, Integer> {
}
