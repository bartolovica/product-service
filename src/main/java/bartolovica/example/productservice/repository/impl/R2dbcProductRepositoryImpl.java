package bartolovica.example.productservice.repository.impl;

import bartolovica.example.productservice.model.ProductResponse;
import bartolovica.example.productservice.model.dto.Product;
import bartolovica.example.productservice.repository.ProductRepository;
import io.r2dbc.spi.Readable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
@Repository
public class R2dbcProductRepositoryImpl implements ProductRepository {

    private static final String SYSTEM = "system";
    private final R2dbcEntityTemplate r2dbc;

    private static final String PRODUCT_EXISTS_BY_CODE = "SELECT EXISTS(SELECT 1 FROM product WHERE code = :code)";
    private static final String SAVE_PRODUCT = "INSERT INTO product (id, code, name, priceeur, priceusd, isavailable, conversionrate, createdby, createdat, updatedby, updatedat) VALUES (:id, :code, :name, :priceEur, :priceUsd, :isAvailable, :conversionRate, :createdBy, :createdAt, :updatedBy, :updatedAt)";
    private static final String GET_PRODUCT_BY_CODE_AND_ID = "SELECT * FROM product WHERE code = :code AND id = :id";
    private static final String GET_PRODUCTS = "SELECT id, code, name, priceeur, priceusd, isavailable FROM product";
    private static final String GET_PRODUCTS_PAGED = "SELECT id, code, name, priceeur, priceusd, isavailable FROM product LIMIT :size OFFSET :offset";
    private static final String UPDATE_PRODUCT_USD_PRICE = "UPDATE product SET priceusd = priceeur * :conversionRate, conversionrate = :conversionRate, updatedby = :updatedBy, updatedat = :updatedAt WHERE conversionrate != :conversionRate";

    @Override
    public Mono<Boolean> productExistsByCode(String code) {
        return r2dbc.getDatabaseClient()
                .sql(PRODUCT_EXISTS_BY_CODE)
                .bind("code", code)
                .map(r -> r.get(0, Boolean.class))
                .one();
    }

    @Override
    public Mono<Product> saveProduct(Product product) {
        return r2dbc.getDatabaseClient()
                .sql(SAVE_PRODUCT)
                .bind("id", product.getId())
                .bind("code", product.getCode())
                .bind("name", product.getName())
                .bind("priceEur", product.getPriceEur())
                .bind("priceUsd", product.getPriceUsd())
                .bind("isAvailable", product.getIsAvailable())
                .bind("conversionRate", product.getConversionRate())
                .bind("createdBy", product.getCreatedBy())
                .bind("createdAt", product.getCreatedAt())
                .bind("updatedBy", product.getUpdatedBy())
                .bind("updatedAt", product.getUpdatedAt())
                .then()
                .thenReturn(product);
    }

    @Override
    public Mono<ProductResponse> getProductByCodeAndId(String code, UUID id) {
        return r2dbc.getDatabaseClient()
                .sql(GET_PRODUCT_BY_CODE_AND_ID)
                .bind("code", code)
                .bind("id", id)
                .map(this::toProduct)
                .one();
    }

    @Override
    public Flux<ProductResponse> getProducts() {
        return r2dbc.getDatabaseClient()
                .sql(GET_PRODUCTS)
                .map(this::toProduct)
                .all();
    }

    @Override
    public Flux<ProductResponse> getProductsPaged(int page, int size) {
        int offset = page * size;
        return r2dbc.getDatabaseClient()
                .sql(GET_PRODUCTS_PAGED)
                .bind("size", size)
                .bind("offset", offset)
                .map(this::toProduct)
                .all();
    }

    @Override
    public Mono<Integer> updateProductUsdPrice(BigDecimal conversionRate) {
        return r2dbc.getDatabaseClient()
                .sql(UPDATE_PRODUCT_USD_PRICE)
                .bind("conversionRate", conversionRate)
                .bind("updatedBy", SYSTEM)
                .bind("updatedAt", LocalDateTime.now())
                .fetch()
                .rowsUpdated()
                .map(Long::intValue);
    }

    private ProductResponse toProduct(Readable row) {
        return ProductResponse.builder()
                .id(row.get("id", UUID.class))
                .code(row.get("code", String.class))
                .name(row.get("name", String.class))
                .priceEur(row.get("priceeur", BigDecimal.class))
                .priceUsd(row.get("priceusd", BigDecimal.class))
                .isAvailable(row.get("isavailable", Boolean.class))
                .build();
    }
}