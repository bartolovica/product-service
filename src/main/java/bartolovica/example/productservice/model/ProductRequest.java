package bartolovica.example.productservice.model;

import bartolovica.example.productservice.util.validation.UniqueCode;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.math.BigDecimal;

@Builder(toBuilder = true)
public record ProductRequest(
        @UniqueCode
        @NotBlank
        @Size(min = 10, max = 10, message = "Code must be 10 characters long")
        String code,

        @NotBlank(message = "Product name is mandatory")
        String name,

        @NotNull
        @DecimalMin(value = "0.0", message = "Price must be positive")
        BigDecimal priceEur,

        @NotNull(message = "Availability is mandatory")
        Boolean isAvailable
) {
}
