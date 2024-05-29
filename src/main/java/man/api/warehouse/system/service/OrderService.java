package man.api.warehouse.system.service;

import man.api.warehouse.system.model.dto.OrderDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface OrderService {
    Flux<OrderDto> findAllOrders();
    Mono<OrderDto> findOrderById(String id);
    Mono<OrderDto> save(OrderDto orderDto);
    Mono<OrderDto> update(OrderDto orderDto, String id);
    Mono<Void> delete(String id);
}
