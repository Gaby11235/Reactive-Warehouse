package man.api.warehouse.system.model;

import lombok.*;
import man.api.warehouse.common.utils.BaseModel;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Document(collection = "supplier_product")
public class SPRelation extends BaseModel {
    @Id
    private String id;
    @Field("supplier_id")
    private String supplierId;
    @Field("product_id")
    private String productId;
    private double price;
}
