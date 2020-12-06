package pl.jmiernowski.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import pl.jmiernowski.domain.email.EmailService;
import pl.jmiernowski.domain.sendEmail.Email;
import pl.jmiernowski.domain.sendEmail.EmailRepository;
import pl.jmiernowski.domain.user.token.Token;
import pl.jmiernowski.domain.user.token.TokenRepository;
import pl.jmiernowski.external.user.UserEntity;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final EmailRepository emailRepository;
    private final TemplateEngine templateEngine;
    private final TokenRepository tokenRepository;

    public void create(UserDto dto){

        UserEntity entity = userMapper.toEntity(dto);
        entity.encodePassword(passwordEncoder);
        userRepository.create(entity);

        Token token = tokenRepository.generateForUser(dto.getUsername());

        sendActivationEmail(dto, token);
    }

    public boolean activate(String token){
        Optional<Token> foundedToken = tokenRepository.getByToken(token)
                .filter(tok -> tok.getValidTo().isAfter(LocalDateTime.now()));
        if(foundedToken.isEmpty()){
            return false;
        }

        foundedToken.ifPresent(tok ->
                userRepository.activate(tok.getUsername()));

        return true;
    }

    //tworzymy content z szablonu html
    private String prepareActivationMail(String username, String token, String validTo){

        Map<String, Object> variables = new HashMap<>();
        variables.put("username", username);
        variables.put("token", token);
        variables.put("validTo", validTo);

        Context context = new Context();
        context.setVariables(variables);

        return templateEngine.process("/email/activationEmail.html",context);
    }
    private String prepareRestartPasswordMail(String username, String token, String validTo){

        Map<String, Object> variables = new HashMap<>();
        variables.put("username", username);
        variables.put("token", token);
        variables.put("validTo", validTo);

        Context context = new Context();
        context.setVariables(variables);

        return templateEngine.process("/email/restartPasswordMail.html",context);
    }

    private void sendActivationEmail(UserDto dto, Token token) {
        Set<String> attachments = new HashSet<>();
        attachments.add("attachment/CV.pdf");
        emailRepository.sendEmail(
                new Email(dto.getUsername(),
                        "Witamy w bibliotece 'Żółć'!",
                        prepareActivationMail(dto.getUsername(), token.getToken(), token.getValidTo().toString()),
                        attachments));


    }

    public void sendRestartPasswordEmail(UserDto dto) {
        Token token = tokenRepository.generateForUser(dto.getUsername());
        Set<String> attachments = new HashSet<>();
        emailRepository.sendEmail(
                new Email(dto.getUsername(),
                        "Restart hasła",
                        prepareRestartPasswordMail(dto.getUsername(), token.getToken(), token.getValidTo().toString()),
                        attachments));
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
