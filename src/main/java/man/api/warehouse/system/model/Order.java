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
@Document(collection = "order")
public class Order extends BaseModel {
    @Id
    private String id;
    @Field("order_number")
    private String orderNumber;
    @Field("user_id")
    private int userId;
    @Field("product_id")
    private String productId;
    private int quantity;
    @Builder.Default
    private Status status=Status.PROCESSING;
}
