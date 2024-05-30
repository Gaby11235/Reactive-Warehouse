package man.api.warehouse.system.service.impl;

import lombok.AllArgsConstructor;
import man.api.warehouse.system.mapper.UserMapper;
import man.api.warehouse.system.model.User;
import man.api.warehouse.system.model.dto.UserDto;
import man.api.warehouse.system.repository.UserReactiveRepository;
import man.api.warehouse.system.service.UserService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserReactiveRepository userReactiveRepository;
    private final UserMapper userMapper;

    @Override
    public Mono<UserDto> save(UserDto userDto) {
        User user = userMapper.toModel(userDto);
        user.setCreatedDate(LocalDateTime.now());
        user.setLastModifiedDate(LocalDateTime.now());
        return userReactiveRepository.save(user).map(userMapper::toDto);
    }

    @Override
    public Flux<UserDto> findAllUsers() {
        return userReactiveRepository.findAll()
                .map(userMapper::toDto)
                .switchIfEmpty(Flux.empty());
    }

    @Override
    public Mono<UserDto> update(UserDto userDto, String id) {
        return userReactiveRepository.findById(id)
                .flatMap(existingUser -> {
                    User user = userMapper.toModel(userDto);
                    user.setId(existingUser.getId());
                    user.setLastModifiedDate(LocalDateTime.now());
                    return userReactiveRepository.save(user);
                })
                .map(userMapper::toDto);
    }

    @Override
    public Mono<Void> delete(String id) {
        return userReactiveRepository.deleteById(id);
    }

    @Override
    public Mono<UserDto> findByUsername(String username) {
        return userReactiveRepository.findByUsername(username)
                .map(userMapper::toDto);
    }

}
