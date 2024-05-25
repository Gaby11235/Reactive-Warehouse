package man.api.warehouse.system.repository;
import man.api.warehouse.system.model.User;
import reactor.core.publisher.Mono;

public interface UserRepository {
    Mono<User> findByUsername(String username);
    Mono<User> create(User user);
}
