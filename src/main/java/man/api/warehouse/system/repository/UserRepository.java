package man.api.warehouse.system.repository;
import man.api.warehouse.system.model.User;
import man.api.warehouse.system.model.dto.UserDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRepository {
    Flux<User> findAllUsers();
    Mono<User> findByUsername(String username);

    Mono<User> create(User user);

    Mono<Boolean> updateUser(String id, UserDto userDto);

    Mono<Boolean> deleteUserById(String id);
}
