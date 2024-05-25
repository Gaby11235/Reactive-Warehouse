package man.api.warehouse.system.model;

import lombok.*;
import man.api.warehouse.common.utils.BaseModel;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Document(collection = "supplier")
public class Supplier extends BaseModel {
    @Id
    private String id;
    private String name;
    private String email;
    private String address;
}
