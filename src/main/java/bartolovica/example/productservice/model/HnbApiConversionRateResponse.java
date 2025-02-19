package bartolovica.example.productservice.model;

import bartolovica.example.productservice.util.BigDecimalCommaDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Value;

import java.math.BigDecimal;

@Value
public class HnbApiConversionRateResponse {

    @JsonProperty("srednji_tecaj")
    @JsonDeserialize(using = BigDecimalCommaDeserializer.class)
    BigDecimal middleRate;
}