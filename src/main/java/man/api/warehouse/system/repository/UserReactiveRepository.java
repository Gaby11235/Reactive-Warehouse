package man.api.warehouse.system.repository;

import man.api.warehouse.system.model.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface UserReactiveRepository extends ReactiveCrudRepository<User, String> {
    Mono<User> findByUsername(String username);
}
