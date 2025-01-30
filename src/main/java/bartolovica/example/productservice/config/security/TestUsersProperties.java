package bartolovica.example.productservice.config.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties("test-users")
public record TestUsersProperties(User admin, User customer) {

    public record User(String username, String password, String roles) {
    }
}
