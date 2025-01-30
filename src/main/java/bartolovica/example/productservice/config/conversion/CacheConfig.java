package bartolovica.example.productservice.config.conversion;

import bartolovica.example.productservice.service.ConversionService;
import com.github.benmanes.caffeine.cache.AsyncCacheLoader;
import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Configuration
public class CacheConfig {

    private final ConversionService conversionService;

    @Bean
    public AsyncLoadingCache<String, BigDecimal> conversionRateCache() {
        AsyncCacheLoader<String, BigDecimal> loader = (key, executor) ->
                conversionService.getUsdToEurConversion().toFuture();

        return Caffeine.newBuilder()
                .expireAfterWrite(1, TimeUnit.HOURS)
                .buildAsync(loader);
    }
}
