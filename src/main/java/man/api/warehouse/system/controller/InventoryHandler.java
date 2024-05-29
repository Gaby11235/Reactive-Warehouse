package man.api.warehouse.system.controller;

import lombok.RequiredArgsConstructor;
import man.api.warehouse.system.model.dto.InventoryDto;
import man.api.warehouse.system.service.impl.InventoryServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class InventoryHandler {
    private final InventoryServiceImpl inventoryService;

    public Mono<ServerResponse> listInventories(ServerRequest serverRequest) {
        Flux<InventoryDto> allInventories = inventoryService.findAllInventories();
        Mono<ServerResponse> notFound = ServerResponse.notFound().build();

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(allInventories, InventoryDto.class)
                .switchIfEmpty(notFound);
    }

    public Mono<ServerResponse> saveInventory(ServerRequest serverRequest) {
        Mono<InventoryDto> inventoryDtoMono = serverRequest.bodyToMono(InventoryDto.class);
        Mono<ServerResponse> notFound = ServerResponse.notFound().build();

        return inventoryDtoMono.flatMap(inventoryDto ->
                        ServerResponse
                                .status(HttpStatus.CREATED)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(inventoryService.save(inventoryDto), InventoryDto.class))
                .switchIfEmpty(notFound);
    }

    public Mono<ServerResponse> updateInventory(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");
        Mono<InventoryDto> inventoryDtoMono = serverRequest.bodyToMono(InventoryDto.class);
        Mono<ServerResponse> notFound = ServerResponse.notFound().build();

        return inventoryDtoMono.flatMap(inventoryDto ->
                        ServerResponse
                                .status(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(inventoryService.update(inventoryDto, id), InventoryDto.class))
                .switchIfEmpty(notFound);
    }

    public Mono<ServerResponse> deleteInventory(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");
        Mono<ServerResponse> notFound = ServerResponse.notFound().build();
        return ServerResponse
                .status(HttpStatus.NO_CONTENT)
                .build(inventoryService.delete(id))
                .switchIfEmpty(notFound);
    }
}
