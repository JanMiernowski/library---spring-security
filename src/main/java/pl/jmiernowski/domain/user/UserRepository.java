package pl.jmiernowski.domain.user;

import pl.jmiernowski.external.user.UserEntity;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    void create(UserEntity entity);
    void update(UserEntity entity);
    void delete(Long id);
    Optional<UserEntity> getById(Long id);
    List<UserEntity> getAll();
    Optional<UserEntity> findByUsername(String username);

}
