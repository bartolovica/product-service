package bartolovica.example.productservice.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springdoc.core.utils.SpringDocUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.ModelAttribute;

@OpenAPIDefinition(
        info = @Info(
                title = "Product Service REST OpenApi Documentation"
        ),
        servers = {
                @Server(url = "/",
                        description = "Default url path '/'")
        }
)
@Configuration
public class SwaggerConfig {
    static {
        SpringDocUtils.getConfig().addAnnotationsToIgnore(ModelAttribute.class);
    }
}
