package man.api.warehouse.system.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class UserDto {
    private String id;
    private String username;
    private String password;
    private boolean active;
    private List<String> roles;
}
