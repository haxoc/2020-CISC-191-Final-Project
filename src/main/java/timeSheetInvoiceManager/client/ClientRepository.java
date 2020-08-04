package timeSheetInvoiceManager.client;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface ClientRepository extends CrudRepository<Client, String> {
}
