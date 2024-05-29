package man.api.warehouse.system.mapper;

import man.api.warehouse.common.exception.ComponentException;
import man.api.warehouse.system.model.User;
import man.api.warehouse.system.model.dto.UserDto;

public class UserMapper {
    public static UserDto toDto(User user) {
        if(user == null) {
            throw new ComponentException(user.getClass().getName());
        }
        UserDto userDto = new UserDto();
        userDto.setId(userDto.getId());
        userDto.setUsername(user.getUsername());
        userDto.setPassword(user.getPassword());

        return userDto;
    }
}
