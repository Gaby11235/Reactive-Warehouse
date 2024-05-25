package man.api.warehouse.system.repository.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import man.api.warehouse.system.mapper.OrderMapper;
import man.api.warehouse.system.mapper.ProductMapper;
import man.api.warehouse.system.model.Order;
import man.api.warehouse.system.model.Product;
import man.api.warehouse.system.model.dto.OrderDto;
import man.api.warehouse.system.repository.OrderRepository;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Repository
@Slf4j
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {

    private final ReactiveMongoTemplate mongoTemplate;

    @Override
    public Flux<Order> findAllOrders() {
        return mongoTemplate.findAll(Order.class);
    }

    @Override
    public Flux<OrderDto> findOrderByKeyword(String keyword, int offset, int limit) {
        return mongoTemplate.find(
                query(where("name").regex(".*" + keyword + ".*", "i"))
                        .skip(offset)
                        .limit(limit),
                Order.class
        ).map(OrderMapper::toDto);
    }

    @Override
    public Mono<Order> findOrderById(int id) {
        return mongoTemplate.findById(id, Order.class);
    }

    @Override
    public Mono<Order> insertOrder(OrderDto orderDto) {
        return mongoTemplate.insert(
                Order.builder()
                        .id(orderDto.getId())
                        .productId(orderDto.getProductId())
                        .orderNumber(orderDto.getOrderNumber())
                        .userId(orderDto.getUserId())
                        .quantity(orderDto.getQuantity())
                        .status(orderDto.getStatus())
                        .build());
    }

    @Override
    public Mono<Boolean> updateOrder(int id, OrderDto orderDto) {
        return mongoTemplate.update(Order.class)
                .matching(where("id").is(id))
                .apply(Update.update("status", orderDto.getStatus()))
                .all()
                .map(updateResult -> updateResult.getModifiedCount() == 1L);
    }

    @Override
    public Mono<Boolean> deleteOrderById(int id) {
        return mongoTemplate.remove(Order.class)
                .matching(where("id").is(id))
                .all()
                .map(deleteResult -> deleteResult.getDeletedCount() == 1L);
    }

//    @Override
//    public Mono<Long> countByKeyword(String keyword) {
//        return null;
//    }

}
