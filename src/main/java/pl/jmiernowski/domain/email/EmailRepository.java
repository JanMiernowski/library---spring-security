package pl.jmiernowski.domain.email;

public interface EmailRepository {

    void sendEmail(Email email);
    void sendResetPasswordEmail(Email email);
}
