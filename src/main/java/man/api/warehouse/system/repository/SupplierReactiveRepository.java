package man.api.warehouse.system.repository;

import man.api.warehouse.system.model.Supplier;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface SupplierReactiveRepository extends ReactiveCrudRepository<Supplier, String> {
}
