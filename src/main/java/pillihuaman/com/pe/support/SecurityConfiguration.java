package pillihuaman.com.pe.support; // o tu paquete de config

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthFilter;

    public SecurityConfiguration(JwtAuthenticationFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(withDefaults())
                .authorizeHttpRequests(auth -> auth
                        // ▼▼▼ ¡ESTE ES EL ORDEN Y CONTENIDO CORRECTO! ▼▼▼

                        // 1. REGLAS PÚBLICAS DE LA API:
                        // Permite el acceso sin token a los endpoints de autenticación y documentación.
                        .requestMatchers(
                                "/api/v1/auth/**",
                                "/v3/api-docs/**",
                                "/swagger-ui/**"
                        ).permitAll()

                        // 2. REGLA DE PROTECCIÓN DE LA API:
                        // CUALQUIER otra ruta que empiece con /api necesita autenticación.
                        .requestMatchers("/api/**").authenticated()

                        // 3. REGLA "CATCH-ALL" PÚBLICA PARA EL FRONTEND:
                        // CUALQUIER otra petición que no haya coincidido con las reglas de /api
                        // (es decir, /home, /product/123, /, etc.) es permitida.
                        // Esto permite que la petición llegue al ForwardingController.
                        .anyRequest().permitAll()
                )
                // ▲▲▲ FIN DE LA SECCIÓN DE REGLAS ▲▲▲

                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout -> logout
                        .logoutUrl("/api/v1/auth/logout")
                        .logoutSuccessHandler((request, response, authentication) ->
                                SecurityContextHolder.clearContext()
                        )
                );

        return http.build();
    }
}