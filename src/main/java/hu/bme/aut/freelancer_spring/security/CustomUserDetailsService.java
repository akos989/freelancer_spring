package hu.bme.aut.freelancer_spring.security;

import hu.bme.aut.freelancer_spring.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public CustomUserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var user = userRepository.findByEmail(email);

        if (user.isPresent()) {
            return new CustomUser(
                    user.get().getId(),
                    user.get().getEmail(),
                    user.get().getPassword()
            );
        }
        throw new UsernameNotFoundException("not found");
    }
}