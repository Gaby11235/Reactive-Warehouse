package man.api.warehouse.system.repository;

import man.api.warehouse.system.model.Order;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface OrderReactiveRepository extends ReactiveCrudRepository<Order, String> {
}
