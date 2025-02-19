package bartolovica.example.productservice.repository;

import bartolovica.example.productservice.model.ProductResponse;
import bartolovica.example.productservice.model.dto.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.UUID;

public interface ProductRepository {

    Mono<Boolean> productExistsByCode(String code);

    Mono<ProductResponse> saveProduct(Product product);

    Mono<ProductResponse> getProductByCodeAndId(String code, UUID id);

    Flux<ProductResponse> getProducts();

    Flux<ProductResponse> getProductsPaged(int page, int size);

    Mono<Integer> updateProductUsdPrice(BigDecimal conversionRate);
}
