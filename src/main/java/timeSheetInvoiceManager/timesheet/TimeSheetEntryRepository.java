package com.timeSheetInvoiceManager.app.timesheet;

import com.timeSheetInvoiceManager.app.project.Project;
import org.springframework.data.repository.CrudRepository;
import com.timeSheetInvoiceManager.app.timesheet.TimeSheet;
import java.util.List;

public interface TimeSheetEntryRepository extends CrudRepository<TimeSheetEntry, String> {
}
