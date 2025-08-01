package pillihuaman.com.pe.support;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class FakeUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Retorna un usuario "fake" para validación del token, ya que el token ya está firmado
        return User.builder()
                .username(username)
                .password("") // No se necesita contraseña
                .authorities(Collections.emptyList()) // O puedes poner roles si lo usas
                .build();
    }
}