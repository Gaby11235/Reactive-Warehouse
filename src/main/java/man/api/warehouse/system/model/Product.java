package man.api.warehouse.system.model;

import lombok.*;
import man.api.warehouse.common.utils.BaseModel;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Document(collation = "product")
public class Product extends BaseModel {

    @Id
    private String id;

    private String name;

    private Float price;

    private Long stock;

}
