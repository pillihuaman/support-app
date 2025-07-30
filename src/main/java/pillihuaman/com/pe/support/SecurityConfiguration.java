package pillihuaman.com.pe.support;

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

/**
 * Configuración centralizada de la seguridad de la aplicación.
 * Define qué rutas son públicas, cuáles están protegidas y el manejo de sesiones.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthFilter;

    // El constructor ahora es más simple, solo necesita el filtro JWT.
    public SecurityConfiguration(JwtAuthenticationFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Deshabilitamos CSRF (práctica común para APIs sin estado)
                .csrf(AbstractHttpConfigurer::disable)

                // Habilitamos CORS usando la configuración global definida en WebConfig.java
                .cors(withDefaults())

                // Definimos las reglas de autorización para las rutas
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                // Rutas públicas para la documentación de la API (Swagger/OpenAPI)
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-resources/**",
                                "/webjars/**"
                                // Si tuvieras algún endpoint PÚBLICO en 'support', iría aquí.
                                // Ejemplo: "/public/v1/support/products"
                        ).permitAll() // Permite el acceso a estas rutas sin autenticación.

                        .anyRequest().authenticated() // CUALQUIER OTRA ruta requiere autenticación.
                )

                // Configuramos la gestión de sesiones para que sea SIN ESTADO
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // Añadimos nuestro filtro JWT para que se ejecute antes del filtro de autenticación estándar
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)

                // Configuramos la funcionalidad de logout (aunque generalmente no se usa en APIs sin estado)
                .logout(logout -> logout
                        .logoutUrl("/api/v1/auth/logout")
                        .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
                );

        return http.build();
    }
}