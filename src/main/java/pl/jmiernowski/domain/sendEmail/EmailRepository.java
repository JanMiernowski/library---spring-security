package pl.jmiernowski.domain.sendEmail;

public interface EmailRepository {

    void sendEmail(Email email);
    void sendResetPasswordEmail(Email email);
}
