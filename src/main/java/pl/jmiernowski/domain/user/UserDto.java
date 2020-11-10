package pl.jmiernowski.domain.user;

import lombok.*;
import pl.jmiernowski.external.user.UserEntity;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserDto {

    private Long id;
    private String username;
    private String password;
    private String role;

    public static UserDto toDto(UserEntity userEntity){
        return UserDto.builder()
                .id(userEntity.getId())
                .username(userEntity.getUsername())
                .password(userEntity.getPassword())
                .role(userEntity.getRole())
                .build();
    }

    public UserEntity toEntity(){
        return UserEntity.builder()
                .id(this.getId())
                .username(this.username)
                .password(this.getPassword())
                .role(this.getRole())
                .build();
    }

}
