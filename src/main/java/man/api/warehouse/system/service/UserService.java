package man.api.warehouse.system.service;

import man.api.warehouse.system.model.dto.UserDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<UserDto> save(UserDto userDto);
    Flux<UserDto> findAllUsers();
    Mono<UserDto> update(UserDto userDto, String id);
    Mono<Void> delete(String id);

    Mono<UserDto> findByUsername(String username);
}
