package man.api.warehouse;

import man.api.warehouse.system.model.dto.LoginRequest;
import man.api.warehouse.system.model.dto.UserDto;
import man.api.warehouse.system.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserHandlerTests {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private UserService userService;

    private UserDto user;

    @BeforeEach
    public void setUp() {
        user = new UserDto();
        user.setUsername("testUser");
        user.setPassword("testPass");
    }

    @Test
    public void testCreateUser() {

        webTestClient.post().uri("/api/users/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(user), UserDto.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.username").isEqualTo(user.getUsername())
                .jsonPath("$.password").isEqualTo(user.getPassword());
    }

    @Test
    public void testGetAllUsers() {
        webTestClient.get().uri("/api/users")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(UserDto.class);
    }

    @Test
    public void testUpdateUser() {
        UserDto savedUser = userService.save(user).block();
        UserDto updatedUser = new UserDto();
        updatedUser.setUsername("updatedUser");
        updatedUser.setPassword("updatedPass");

        webTestClient.put()
                .uri("/api/users/{id}", Collections.singletonMap("id", savedUser.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(updatedUser), UserDto.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.username").isEqualTo(updatedUser.getUsername())
                .jsonPath("$.password").isEqualTo(updatedUser.getPassword());
    }

    @Test
    public void testDeleteUser() {
        UserDto savedUser = userService.save(user).block();

        webTestClient.delete()
                .uri("/api/users/{id}", Collections.singletonMap("id", savedUser.getId()))
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    public void testLogin() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("jane_smith");
        loginRequest.setPassword("hashedJaneSmithPassword");

        webTestClient.post().uri("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(loginRequest), LoginRequest.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    System.out.println("Response body: " + new String(response.getResponseBody()));
                });
    }

    @Test
    public void testRefreshToken() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("jane_smith");
        loginRequest.setPassword("hashedJaneSmithPassword");

        String refreshToken = webTestClient.post().uri("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(loginRequest), LoginRequest.class)
                .exchange()
                .expectStatus().isOk()
                .returnResult(Map.class)
                .getResponseBody()
                .blockFirst()
                .get("refreshToken")
                .toString();

        // Use refresh token to get new access token
        webTestClient.post().uri("/api/users/refreshToken")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(Collections.singletonMap("refreshToken", refreshToken)), Map.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.accessToken").isNotEmpty();
    }


}
