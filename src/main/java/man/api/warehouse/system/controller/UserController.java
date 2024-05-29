/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package man.api.warehouse.system.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import man.api.warehouse.common.exception.NotFoundException;
import man.api.warehouse.system.model.dto.UserDto;
import man.api.warehouse.system.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import man.api.warehouse.system.model.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 *
 * @author hantsy
 */
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(value = "/user")
@Tag(name = "用户接口", description = "用户接口 API")
public class UserController {

    private final UserRepository userRepository;

    @GetMapping("")
    @Operation(summary = "ListAllUser", description = "ListAllUser")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "接口请求成功"))
    public Flux<User> AllUser(){
        return this.userRepository.findAllUsers();
    }

    @GetMapping("/users/{username}")
    public Mono<User> get(@PathVariable() String username) {
        return this.userRepository.findByUsername(username);
    }

    @PutMapping("/{id}")
    @Operation(summary = "updateUser", description = "updateUser")
    @ApiResponses(@ApiResponse(responseCode = "204", description = "接口请求成功"))
    public Mono<ResponseEntity> update(@PathVariable("id") String id, @RequestBody UserDto userDto) {
        return this.userRepository.updateUser(id, userDto)
                .handle((result, sink) -> {
                    if(true) {
                        sink.next(ResponseEntity.noContent().build());
                    } else {
                        sink.error(new NotFoundException(id));
                    }
                });
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "DeleteUser", description = "DeleteUser")
    @ApiResponses(@ApiResponse(responseCode = "204", description = "接口请求成功"))
    public Mono<ResponseEntity> delete(@PathVariable("id") String id) {
        return this.userRepository.deleteUserById(id)
                .handle((result, sink) -> {
                    if(true) {
                        sink.next(ResponseEntity.noContent().build());
                    } else {
                        sink.error(new NotFoundException(id));
                    }
                });
    }
}
