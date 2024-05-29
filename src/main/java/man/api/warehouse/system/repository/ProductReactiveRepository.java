package man.api.warehouse.system.repository;

import man.api.warehouse.system.model.Product;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface ProductReactiveRepository extends ReactiveCrudRepository<Product, String> {
    Mono<Boolean> existsByName(String name);
}
