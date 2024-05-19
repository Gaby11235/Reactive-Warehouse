package man.api.warehouse.common.exception;

import org.springframework.http.HttpStatus;

public class ComponentException extends RuntimeException{

    private Integer status = HttpStatus.INTERNAL_SERVER_ERROR.value();
    private final String message;

    public ComponentException(String className) {
        this.message = "Internal component error: " + className;
    }
}
