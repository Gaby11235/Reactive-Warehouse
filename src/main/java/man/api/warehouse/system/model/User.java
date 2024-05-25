package man.api.warehouse.system.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import man.api.warehouse.common.utils.BaseModel;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document
public class User extends BaseModel {

    @Id
    private String id;
    private String username;

    @JsonIgnore
    private String password;

    @Builder.Default()
    private boolean active = true;

    @Builder.Default()
    private List<String> roles = new ArrayList<>();

}
