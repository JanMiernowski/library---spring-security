package pl.jmiernowski.domain.email;

import pl.jmiernowski.external.email.EmailEntity;

public interface EmailRepository {

    EmailEntity create(EmailEntity entity);

}
