package man.api.warehouse.system.controller;

import lombok.RequiredArgsConstructor;
import man.api.warehouse.system.model.dto.UserDto;
import man.api.warehouse.system.service.impl.UserServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class UserHandler {
    private final UserServiceImpl userService;

    public Mono<ServerResponse> listUsers(ServerRequest serverRequest) {
        Flux<UserDto> allUsers = userService.findAllUsers();
        Mono<ServerResponse> notFound = ServerResponse.notFound().build();

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(allUsers, UserDto.class)
                .switchIfEmpty(notFound);
    }

    public Mono<ServerResponse> saveUser(ServerRequest serverRequest) {
        Mono<UserDto> userDtoMono = serverRequest.bodyToMono(UserDto.class);
        Mono<ServerResponse> notFound = ServerResponse.notFound().build();

        return userDtoMono.flatMap(userDto ->
                        ServerResponse
                                .status(HttpStatus.CREATED)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(userService.save(userDto), UserDto.class))
                .switchIfEmpty(notFound);
    }

    public Mono<ServerResponse> updateUser(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");
        Mono<UserDto> userDtoMono = serverRequest.bodyToMono(UserDto.class);
        Mono<ServerResponse> notFound = ServerResponse.notFound().build();

        return userDtoMono.flatMap(userDto ->
                        ServerResponse
                                .status(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(userService.update(userDto, id), UserDto.class))
                .switchIfEmpty(notFound);
    }

    public Mono<ServerResponse> deleteUser(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");
        Mono<ServerResponse> notFound = ServerResponse.notFound().build();
        return ServerResponse
                .status(HttpStatus.NO_CONTENT)
                .build(userService.delete(id))
                .switchIfEmpty(notFound);
    }

}
