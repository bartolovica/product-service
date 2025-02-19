package bartolovica.example.productservice.service;

import bartolovica.example.productservice.model.ProductRequest;
import bartolovica.example.productservice.model.ProductResponse;
import org.springframework.security.core.userdetails.UserDetails;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

public interface ProductService {

    Mono<ProductResponse> createProduct(ProductRequest productRequest, UserDetails userDetails);

    Mono<ProductResponse> getProductByIdAndCode(String code, UUID id);

    Mono<List<ProductResponse>> getProducts();

    Mono<List<ProductResponse>> getProductsPaged(int page, int size);
}
