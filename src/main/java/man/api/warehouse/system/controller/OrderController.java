package man.api.warehouse.system.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import man.api.warehouse.common.exception.OrderNotFoundException;
import man.api.warehouse.system.model.Order;
import man.api.warehouse.system.model.dto.OrderDto;
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
@RequestMapping(value = "/order")
@Tag(name = "订单接口", description = "订单接口 API")
public class OrderController {

    private final OrderRepository orderRepository;

    @GetMapping("")
    @Operation(summary = "ListAllOrder", description = "ListAllOrder")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "接口请求成功"))
    public Flux<Order> AllOrder(){
        return this.orderRepository.findAllOrders();
    }


    @PostMapping("")
    @Operation(summary = "addOrder", description = "addOrder")
    @ApiResponses(@ApiResponse(responseCode = "201", description = "接口请求成功"))
    public Mono<ResponseEntity> add(@RequestBody OrderDto orderDto) {
        return this.orderRepository.insertOrder(orderDto)
                .map(saved -> ResponseEntity.created(URI.create("/order/" + saved.getId())).build());
    }

    @GetMapping("/{id}")
    @Operation(summary = "selectOrderById", description = "selectOrderById")
    @ApiResponses(@ApiResponse(responseCode = "201", description = "接口请求成功"))
    public Mono<Order> get(@PathVariable("id") int id) {
        return this.orderRepository.findOrderById(id).switchIfEmpty(Mono.error(new OrderNotFoundException(id)));
    }


    @PutMapping("/{id}")
    @Operation(summary = "updateOrder", description = "updateOrder")
    @ApiResponses(@ApiResponse(responseCode = "201", description = "接口请求成功"))
    // 仅支持修改订单的状态
    public Mono<ResponseEntity> update(@PathVariable("id") int id, @RequestBody OrderDto orderDto) {
        return this.orderRepository.updateOrder(id, orderDto)
                .handle((result, sink) -> {
                    if(true) {
                        sink.next(ResponseEntity.noContent().build());
                    } else {
                        sink.error(new OrderNotFoundException(id));
                    }
                });
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "DeleteOrder", description = "DeleteOrder")
    @ApiResponses(@ApiResponse(responseCode = "204", description = "接口请求成功"))
    public Mono<ResponseEntity> delete(@PathVariable("id") int id) {
        return this.orderRepository.deleteOrderById(id)
                .handle((result, sink) -> {
                    if(true) {
                        sink.next(ResponseEntity.noContent().build());
                    } else {
                        sink.error(new OrderNotFoundException(id));
                    }
                });
    }
}
