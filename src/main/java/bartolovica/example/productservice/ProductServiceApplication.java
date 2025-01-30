package bartolovica.example.productservice;

import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.math.BigDecimal;

@EnableScheduling
@ConfigurationPropertiesScan
@SpringBootApplication
@RequiredArgsConstructor
public class ProductServiceApplication {

    private final AsyncLoadingCache<String, BigDecimal> conversionRateCache;

    public static void main(String[] args) {
        SpringApplication.run(ProductServiceApplication.class, args);
    }

}
