package man.api.warehouse.system.mapper;

import man.api.warehouse.common.exception.ComponentException;
import man.api.warehouse.system.model.Order;
import man.api.warehouse.system.model.Product;
import man.api.warehouse.system.model.dto.OrderDto;
import man.api.warehouse.system.model.dto.ProductDto;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {
    public static OrderDto toDto(Order order) {
        if(order == null) {
            throw new ComponentException(order.getClass().getName());
        }
        OrderDto orderDto = new OrderDto();
        orderDto.setId(orderDto.getId());
        orderDto.setOrderNumber(order.getOrderNumber());
        orderDto.setQuantity(order.getQuantity());
        orderDto.setUserId(order.getUserId());
        orderDto.setProductId(order.getProductId());
        orderDto.setStatus(order.getStatus());
        return orderDto;
    }
}
