package man.api.warehouse.system.repository;

import man.api.warehouse.system.model.Inventory;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface InventoryReactiveRepository extends ReactiveCrudRepository<Inventory, String> {
    Mono<Boolean> existsByProductId(String productId);
}
