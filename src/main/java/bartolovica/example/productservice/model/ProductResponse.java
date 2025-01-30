package bartolovica.example.productservice.model;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder(toBuilder = true)
public record ProductResponse(UUID id,
                              String code,
                              String name,
                              BigDecimal priceEur,
                              BigDecimal priceUsd,
                              Boolean isAvailable) {
}
