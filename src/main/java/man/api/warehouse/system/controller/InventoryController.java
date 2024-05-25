package man.api.warehouse.system.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import man.api.warehouse.common.exception.NotFoundException;
import man.api.warehouse.common.exception.OrderNotFoundException;
import man.api.warehouse.system.model.Inventory;
import man.api.warehouse.system.model.Order;
import man.api.warehouse.system.model.dto.InventoryDto;
import man.api.warehouse.system.model.dto.OrderDto;
import man.api.warehouse.system.repository.InventoryRepository;
import man.api.warehouse.system.repository.OrderRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(value = "/inventory")
@Tag(name = "仓库接口", description = "仓库接口 API")
public class InventoryController {
    private final InventoryRepository inventoryRepository;

    @GetMapping("")
    @Operation(summary = "ListAllInventory", description = "ListAllInventory")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "接口请求成功"))
    public Flux<Inventory> AllInventory(){
        return this.inventoryRepository.findAllInventorys();
    }


    @PostMapping("")
    @Operation(summary = "addInventory", description = "addInventory")
    @ApiResponses(@ApiResponse(responseCode = "201", description = "接口请求成功"))
    public Mono<ResponseEntity> add(@RequestBody InventoryDto inventoryDto) {
        return this.inventoryRepository.insertInventory(inventoryDto)
                .map(saved -> ResponseEntity.created(URI.create("/inventory/" + saved.getId())).build());
    }

    @GetMapping("/{id}")
    @Operation(summary = "selectInventoryById", description = "selectInventoryById")
    @ApiResponses(@ApiResponse(responseCode = "201", description = "接口请求成功"))
    public Mono<Inventory> get(@PathVariable("id") String id) {
        return this.inventoryRepository.findInventoryById(id).switchIfEmpty(Mono.error(new NotFoundException(id)));
    }


    @PutMapping("/{id}")
    @Operation(summary = "updateInventory", description = "updateOrder")
    @ApiResponses(@ApiResponse(responseCode = "204", description = "接口请求成功"))
    // 仅支持修改订单的状态
    public Mono<ResponseEntity> update(@PathVariable("id") String id, @RequestBody InventoryDto inventoryDto) {
        return this.inventoryRepository.updateInventory(id, inventoryDto)
                .handle((result, sink) -> {
                    if(true) {
                        sink.next(ResponseEntity.noContent().build());
                    } else {
                        sink.error(new NotFoundException(id));
                    }
                });
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "DeleteInventory", description = "DeleteInventory")
    @ApiResponses(@ApiResponse(responseCode = "204", description = "接口请求成功"))
    public Mono<ResponseEntity> delete(@PathVariable("id") String id) {
        return this.inventoryRepository.deleteInventoryById(id)
                .handle((result, sink) -> {
                    if(true) {
                        sink.next(ResponseEntity.noContent().build());
                    } else {
                        sink.error(new NotFoundException(id));
                    }
                });
    }
}
