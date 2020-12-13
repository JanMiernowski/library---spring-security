package pl.jmiernowski.external.email;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import pl.jmiernowski.domain.email.EmailRepository;
import pl.jmiernowski.domain.user.UserDto;
import pl.jmiernowski.domain.user.UserService;
import pl.jmiernowski.external.user.DatabaseUserRepository;
import pl.jmiernowski.external.user.UserEntity;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DatabaseEmailRepository implements EmailRepository {

    private final JpaEmailRepository jpaEmailRepository;


    @Override
    public EmailEntity create(EmailEntity emailEntity){
        return jpaEmailRepository.save(emailEntity);
    }

    @Override
    public Optional<EmailEntity> getById(Long id) {
        return jpaEmailRepository.findById(id);
    }


}
