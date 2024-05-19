package man.api.warehouse.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ProductNotFoundException extends RuntimeException{

    private Integer status = HttpStatus.NOT_FOUND.value();
    private final String message;

    public ProductNotFoundException(String id) {
        this.message = "Product not found: #" + id;
    }
}
