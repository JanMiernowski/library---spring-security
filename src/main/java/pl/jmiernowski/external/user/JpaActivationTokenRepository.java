package pl.jmiernowski.external.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaActivationTokenRepository extends JpaRepository<ActivationTokenEntity, Long> {

    Optional<ActivationTokenEntity> findByTokenValue(String token);
    Optional<ActivationTokenEntity> findById(ActivationTokenEntity entity);

}
