package pl.jmiernowski.config;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.jmiernowski.domain.user.UserDto;
import pl.jmiernowski.domain.user.UserRepository;
import pl.jmiernowski.domain.user.UserService;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SecurityUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(user -> mapToUserDetail(UserDto.toDto(user)))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    private UserDetails mapToUserDetail(UserDto dto) {

        List<GrantedAuthority> authorities =
                Arrays.asList(new SimpleGrantedAuthority("ROLE_" + dto.getRole()));

        return new User(dto.getUsername(), dto.getPassword(), authorities);

    }


}
