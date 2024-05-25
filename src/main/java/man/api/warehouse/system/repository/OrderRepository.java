package man.api.warehouse.system.repository;

import man.api.warehouse.system.model.Order;
import man.api.warehouse.system.model.Product;
import man.api.warehouse.system.model.dto.OrderDto;
import man.api.warehouse.system.model.dto.ProductDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface OrderRepository {

    Flux<Order> findAllOrders();

    Flux<OrderDto> findOrderByKeyword(String keyword, int offset, int limit);

    Mono<Order> findOrderById(int id);

    Mono<Order> insertOrder(OrderDto orderDto);

    Mono<Boolean> updateOrder(int id, OrderDto orderDto);

    Mono<Boolean> deleteOrderById(int id);

//    Mono<Long> countByKeyword(String keyword);
}
