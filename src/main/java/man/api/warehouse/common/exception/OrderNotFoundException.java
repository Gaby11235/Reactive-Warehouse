package man.api.warehouse.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
public class OrderNotFoundException extends RuntimeException{
    private Integer status = HttpStatus.NOT_FOUND.value();
    private final String message;

    public OrderNotFoundException(int id) {
        this.message = "Order not found: #" + id;
    }
}
