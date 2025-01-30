package bartolovica.example.productservice.service;

import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface ConversionService {

    Mono<BigDecimal> getUsdToEurConversion();
}
