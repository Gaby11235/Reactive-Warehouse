package man.api.warehouse.system.model.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
public class SPRelationDto {
    private String id;
    private String supplierId;
    private String productId;
    private double price;
}
