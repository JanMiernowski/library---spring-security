package pl.jmiernowski.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.jmiernowski.domain.email.Email;
import pl.jmiernowski.domain.email.EmailRepository;
import pl.jmiernowski.external.user.UserEntity;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final EmailRepository emailRepository;

    public void create(UserDto dto){
        UserEntity entity = userMapper.toEntity(dto);
        entity.encodePassword(passwordEncoder);
        userRepository.create(entity);
        emailRepository.sendEmail(
                new Email(dto.getUsername(),
                        "Witamy w bibliotece!",
                        "Witaj w naszej bibliotece by Jasmistrz"));
    }
    public void update(UserDto dto){
        if(getById(dto.getId()).isEmpty()){
            throw new IllegalStateException("Updated object not exists");
        }
        UserEntity entity = userMapper.toEntity(dto);
        userRepository.update(entity);
    }
    public void delete(Long id){
        userRepository.delete(id);
    }
    Optional<UserDto> getById(Long id){
        return userRepository.getById(id)
                .map(userMapper::toDto);
    }
    public List<UserDto> getAll(){
        return userRepository.getAll().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }
    public Optional<UserDto> findByUsername(String username){
        return userRepository.findByUsername(username)
                .map(userMapper::toDto);
    }

}
