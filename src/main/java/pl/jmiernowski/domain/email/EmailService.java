package pl.jmiernowski.domain.email;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.jmiernowski.domain.user.UserDto;
import pl.jmiernowski.domain.user.UserRepository;
import pl.jmiernowski.domain.user.UserService;
import pl.jmiernowski.external.email.DatabaseEmailRepository;
import pl.jmiernowski.external.email.EmailEntity;
import pl.jmiernowski.external.user.UserEntity;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final DatabaseEmailRepository emailRepository;
    private final EmailMapper emailMapper;
    private final UserRepository userRepository;

    public EmailDto create(EmailDto dto){
        EmailEntity emailEntity = emailMapper.toEntity(dto);
        emailRepository.create(emailEntity);
        Optional<UserEntity> user = userRepository.findByUsername(dto.getUsername());
        if (user.isPresent()){
            user.get().getEmails().add(emailEntity);
            userRepository.update(user.get());
        }
        return emailMapper.toDto(emailEntity);
    }

}
