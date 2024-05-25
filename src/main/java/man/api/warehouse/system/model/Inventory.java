package man.api.warehouse.system.model;

import lombok.*;
import man.api.warehouse.common.utils.BaseModel;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Document(collection = "inventory")
public class Inventory extends BaseModel {
    @Id
    private String id;
    @Field("product_id")
    private String productId;
    private int quantity;
}
