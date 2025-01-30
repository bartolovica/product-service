package bartolovica.example.productservice.service.impl;

import bartolovica.example.productservice.config.conversion.ConversionRateApiProperties;
import bartolovica.example.productservice.model.HnbApiConversionRateResponse;
import bartolovica.example.productservice.service.ConversionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RequiredArgsConstructor
@Service
public class ConversionServiceImpl implements ConversionService {
    private final WebClient conversionRateWebClient;
    private final ConversionRateApiProperties properties;

    @Override
    public Mono<BigDecimal> getUsdToEurConversion() {
        return conversionRateWebClient.get()
                .uri(uriBuilder -> uriBuilder.queryParams(buildQueryParams()).build())
                .retrieve()
                .bodyToFlux(HnbApiConversionRateResponse.class)
                .collectList()
                .map(list -> list.get(0).getMiddleRate());
    }

    private MultiValueMap<String, String> buildQueryParams() {
        var queryParams = new LinkedMultiValueMap<String, String>();
        var queryParamsProperties = properties.queryParams();
        queryParams.add(queryParamsProperties.currency(), queryParamsProperties.currencyValue());
        queryParams.add(queryParamsProperties.date(), LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        return queryParams;
    }
}
