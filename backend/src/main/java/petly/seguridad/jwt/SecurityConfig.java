package petly.seguridad.jwt;

import petly.logout.JwtBlacklistLogoutHandler;
import petly.repository.UsuarioRepository;
import petly.token.TokenBlackListService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@AllArgsConstructor
public class SecurityConfig {

    private final JwtConfig jwtConfig;
    private final UsuarioRepository userRepository;

    public record LoginDTO(String email, String password) {
    }

    public record LoginResponse(String token, String name, String email) {
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:4200", "http://localhost:8080"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Aplica a todas las rutas de tu API
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(ar -> ar.anyRequest().permitAll());
        return http.build();
    }

    /**
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtBlacklistLogoutHandler logoutHandler) throws Exception {
        http
                .cors(Customizer.withDefaults()) // Habilita CORS globalmente para evitar errores de Frontend
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(ar -> ar
                        .requestMatchers(HttpMethod.POST, "/users/login", "/users/register").permitAll()
                        // Opcional: si quieres permitir GET público de usuarios:
                        .requestMatchers(HttpMethod.GET, "/users").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/productos/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/categorias/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/productos/create").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/categorias/create").permitAll()

                        // Todo lo demás requiere token
                        .anyRequest().authenticated()
                )
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Habilita validación de JWT en cada petición

                // Habilito el logout
                .logout(logout -> logout
                        .logoutUrl("/users/logout")
                        .addLogoutHandler(logoutHandler)
                        .logoutSuccessHandler((request, response, authentication) -> response.setStatus(HttpServletResponse.SC_OK))
                )
                .oauth2ResourceServer(rs -> rs.jwt(Customizer.withDefaults()));
        return http.build();
    }
**/
    @Bean
    public JwtDecoder jwtDecoder(TokenBlackListService tokenBlacklistService) {
        return new JwtDecoderC(NimbusJwtDecoder.withSecretKey(jwtConfig.getSecretKey()).build(), tokenBlacklistService);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    public AuthenticationManager authManager(AuthenticationProvider authenticationProvider) {
        return new ProviderManager(authenticationProvider);
    }

    @Bean
    UserDetailsService userDetailsService() {
        return username -> /*(org.springframework.security.core.userdetails.UserDetails)*/ userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Bean
    public JwtBlacklistLogoutHandler logoutHandler(TokenBlackListService tokenBlacklistService) {
        return new JwtBlacklistLogoutHandler(tokenBlacklistService);
    }

}