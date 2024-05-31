package man.api.warehouse.system.controller;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import man.api.warehouse.system.model.AuthResponse;
import man.api.warehouse.system.model.dto.LoginRequest;
import man.api.warehouse.system.model.dto.UserDto;
import man.api.warehouse.system.service.impl.UserServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserHandler {
    private final UserServiceImpl userService;
    private static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    public Mono<ServerResponse> login(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(LoginRequest.class)
                .flatMap(loginRequest -> userService.findByUsername(loginRequest.getUsername())
                        .flatMap(user -> {
                            // 添加日志记录以输出返回的用户信息
                            log.info("User found: {}", user);
                            if (loginRequest.getPassword().equals(user.getPassword())) {
                                String accessToken = generateAccessToken(user.getUsername());
                                String refreshToken = generateRefreshToken(user.getUsername());
                                return ServerResponse.ok()
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .body(BodyInserters.fromValue(new AuthResponse(accessToken, refreshToken)));
                            } else {
                                // 身份验证失败，返回401状态码
                                return ServerResponse.status(HttpStatus.UNAUTHORIZED).build();
                            }
                        }).switchIfEmpty(ServerResponse.status(HttpStatus.UNAUTHORIZED).build())
                );
    }

    public Mono<ServerResponse> refreshToken(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(AuthResponse.class)
                .flatMap(authResponse -> {
                    String refreshToken = authResponse.getRefreshToken();
                    try {
                        String username = Jwts.parserBuilder()
                                .setSigningKey(SECRET_KEY)
                                .build()
                                .parseClaimsJws(refreshToken)
                                .getBody()
                                .getSubject();
                        String newAccessToken = generateAccessToken(username);
                        return ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(BodyInserters.fromValue(new AuthResponse(newAccessToken, refreshToken)));
                    } catch (Exception e) {
                        log.error("Invalid refresh token", e);
                        return ServerResponse.status(HttpStatus.UNAUTHORIZED).build();
                    }
                });
    }


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

    private String generateAccessToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(SECRET_KEY)
                .compact();
    }

    private String generateRefreshToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + 604800000))
                .signWith(SECRET_KEY)
                .compact();
    }
}
