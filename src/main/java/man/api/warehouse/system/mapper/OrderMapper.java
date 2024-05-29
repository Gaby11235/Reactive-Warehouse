package man.api.warehouse.system.mapper;

import man.api.warehouse.common.exception.ComponentException;
import man.api.warehouse.system.model.Order;
import man.api.warehouse.system.model.dto.OrderDto;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    public OrderDto toDto(Order order) {
        if (order == null) {
            throw new ComponentException(order.getClass().getName());
        }
        OrderDto orderDto = new OrderDto();
        orderDto.setId(order.getId());
        orderDto.setOrderNumber(order.getOrderNumber());
        orderDto.setUserId(order.getUserId());
        orderDto.setProductId(order.getProductId());
        orderDto.setQuantity(order.getQuantity());
        orderDto.setStatus(order.getStatus());
        return orderDto;
    }

    public Order toModel(OrderDto orderDto) {
        if (orderDto == null) {
            throw new ComponentException(orderDto.getClass().getName());
        }
        Order order = new Order();
        order.setOrderNumber(orderDto.getOrderNumber());
        order.setUserId(orderDto.getUserId());
        order.setProductId(orderDto.getProductId());
        order.setQuantity(orderDto.getQuantity());
        order.setStatus(orderDto.getStatus());
        return order;
    }
}
