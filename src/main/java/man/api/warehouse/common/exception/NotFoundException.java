package man.api.warehouse.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class NotFoundException extends RuntimeException{
    private Integer status = HttpStatus.NOT_FOUND.value();
    private final String message;

    public NotFoundException(String id) {
        this.message = "Not found: #" + id;
    }
}
