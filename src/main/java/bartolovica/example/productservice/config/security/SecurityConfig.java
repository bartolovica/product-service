package bartolovica.example.productservice.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final TestUsersProperties testUsersProperties;

    @Bean
    public SecurityWebFilterChain springSecurityFilterChainLocal(ServerHttpSecurity http) {
        return http
                .cors(ServerHttpSecurity.CorsSpec::disable)
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges ->
                        exchanges
                                .pathMatchers("/actuator/**")
                                .permitAll()
                                .pathMatchers("/swagger-ui/**", "/swagger-resources/**", "/v3/api-docs/**")
                                .permitAll()

                                .pathMatchers(HttpMethod.POST, "/product/**")
                                .hasAuthority("ROLE_ADMIN")
                                .pathMatchers(HttpMethod.GET, "/product/**")
                                .hasAnyAuthority("ROLE_ADMIN", "ROLE_CUSTOMER")
                                .anyExchange()
                                .authenticated()

                )
                .httpBasic(Customizer.withDefaults())
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public MapReactiveUserDetailsService userDetailsService() {
        TestUsersProperties.User adminData = testUsersProperties.admin();
        TestUsersProperties.User customerData = testUsersProperties.customer();
        UserDetails admin = User
                .withUsername(adminData.username())
                .password(passwordEncoder().encode(adminData.password()))
                .roles(adminData.roles())
                .build();

        UserDetails customer = User
                .withUsername(customerData.username())
                .password(passwordEncoder().encode(customerData.password()))
                .roles(customerData.roles())
                .build();
        return new MapReactiveUserDetailsService(admin, customer);
    }

}