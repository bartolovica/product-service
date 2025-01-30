package bartolovica.example.productservice.model.dto;


import bartolovica.example.productservice.util.validation.UniqueCode;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @NotNull
    private UUID id;

    @NotBlank
    @Size(min = 10, max = 10)
    @UniqueCode
    private String code;

    @NotBlank
    private String name;

    @DecimalMin(value = "0.0")
    private BigDecimal priceEur;

    @DecimalMin(value = "0.0")
    private BigDecimal priceUsd;

    @NotNull
    private Boolean isAvailable;

    private BigDecimal conversionRate;

    private String createdBy;

    private LocalDateTime createdAt;

    private String updatedBy;

    private LocalDateTime updatedAt;
}
