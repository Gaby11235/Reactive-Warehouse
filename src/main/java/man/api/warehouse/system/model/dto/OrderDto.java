package man.api.warehouse.system.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import man.api.warehouse.system.model.Order;
import man.api.warehouse.system.model.Status;

@Getter
@Setter
public class OrderDto {

    private String id;
    private String orderNumber;
    private int userId;
    private String productId;
    private int quantity;
    private Status status;
}
