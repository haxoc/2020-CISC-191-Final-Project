package timeSheetInvoiceManager.project;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface ProjectRepository extends CrudRepository<Project, Integer> {
    Optional<Project> findByName(String name);

}
