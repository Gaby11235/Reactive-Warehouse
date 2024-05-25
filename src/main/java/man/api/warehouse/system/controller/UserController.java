/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package man.api.warehouse.system.controller;


import lombok.RequiredArgsConstructor;
import man.api.warehouse.system.repository.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import man.api.warehouse.system.model.User;
import reactor.core.publisher.Mono;

/**
 *
 * @author hantsy
 */
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserRepository users;

    @GetMapping("/users/{username}")
    public Mono<User> get(@PathVariable() String username) {
        return this.users.findByUsername(username);
    }
}
