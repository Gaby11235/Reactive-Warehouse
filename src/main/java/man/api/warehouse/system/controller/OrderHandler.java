package man.api.warehouse.system.controller;

import lombok.RequiredArgsConstructor;
import man.api.warehouse.system.model.dto.OrderDto;
import man.api.warehouse.system.model.dto.ProductDto;
import man.api.warehouse.system.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class OrderHandler {

    private final OrderService orderService;

    public Mono<ServerResponse> listOrders(ServerRequest request) {
        Flux<OrderDto> allOrders = orderService.findAllOrders();
        Mono<ServerResponse> notFound = ServerResponse.notFound().build();

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(allOrders, ProductDto.class)
                .switchIfEmpty(notFound);
    }

    public Mono<ServerResponse> getOrder(ServerRequest request) {
        String id = request.pathVariable("id");
        return orderService.findOrderById(id)
                .flatMap(order -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(order))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> saveOrder(ServerRequest serverRequest) {
        Mono<OrderDto> orderDtoMono = serverRequest.bodyToMono(OrderDto.class);
        Mono<ServerResponse> notFound = ServerResponse.notFound().build();

        return orderDtoMono.flatMap(orderDto ->
                        ServerResponse
                                .status(HttpStatus.CREATED)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(orderService.save(orderDto), OrderDto.class))
                .switchIfEmpty(notFound);
    }

    public Mono<ServerResponse> updateOrder(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");
        Mono<OrderDto> productDtoMono = serverRequest.bodyToMono(OrderDto.class);
        Mono<ServerResponse> notFound = ServerResponse.notFound().build();

        return productDtoMono.flatMap(orderDto ->
                        ServerResponse
                                .status(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(orderService.update(orderDto, id), OrderDto.class))
                .switchIfEmpty(notFound);
    }

    public Mono<ServerResponse> deleteOrder(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");
        Mono<ServerResponse> notFound = ServerResponse.notFound().build();
        return ServerResponse
                .status(HttpStatus.NO_CONTENT)
                .build(orderService.delete(id))
                .switchIfEmpty(notFound);
    }

}
