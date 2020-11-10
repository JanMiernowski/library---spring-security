package pl.jmiernowski.domain.user;

import org.springframework.stereotype.Component;
import pl.jmiernowski.external.user.UserEntity;

@Component
public class UserMapper {

    public UserDto toDto(UserEntity entity){
        if(entity == null) return null;
        return UserDto.toDto(entity);
    }
    public UserEntity toEntity(UserDto dto){
        if(dto == null) return null;
        return dto.toEntity();
    }

}
