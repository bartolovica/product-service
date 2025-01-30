package bartolovica.example.productservice.config.conversion;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties("conversion-rate-api")
public record ConversionRateApiProperties(
        @NotNull String conversionProviderBaseUrl,
        @NotNull Integer restClientMinBackoff,
        @NotNull Integer restClientTimeoutMillis,
        @NotNull QueryParams queryParams) {

    public record QueryParams(
            @NotNull String currency,
            @NotNull String date,
            @NotNull String currencyValue) {
    }
}
