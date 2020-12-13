package pl.jmiernowski.domain.email;

import pl.jmiernowski.external.email.EmailEntity;

import java.util.Optional;

public interface EmailRepository {

    EmailEntity create(EmailEntity entity);
    Optional<EmailEntity> getById(Long id);

}
