package pl.jmiernowski.external.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.jmiernowski.domain.user.UserDto;
import pl.jmiernowski.domain.user.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DatabaseUserRepository implements UserRepository {

    private final JpaUserRepository jpaUserRepository;

    @Override
    public void create(UserEntity entity) {
        jpaUserRepository.save(entity);
    }

    @Override
    public void update(UserEntity entity) {
        Optional<UserEntity> byId = getById(entity.getId());
        if(byId.isPresent()){
            jpaUserRepository.save(entity);
        }
    }

    @Override
    public void delete(Long id) {
        jpaUserRepository.deleteById(id);
    }

    @Override
    public Optional<UserEntity> getById(Long id) {
        return jpaUserRepository.findById(id);
    }

    @Override
    public List<UserEntity> getAll() {
        return jpaUserRepository.findAll();
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        return jpaUserRepository.findByUsername(username);
    }

    @Override
    public void activate(String username) {
        jpaUserRepository.findByUsername(username).ifPresent(user ->{
                user.activate();
                jpaUserRepository.save(user);
        });
    }

    @Override
    public void restartPassword(String username) {

    }
}
