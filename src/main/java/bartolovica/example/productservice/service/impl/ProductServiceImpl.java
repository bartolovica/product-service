package bartolovica.example.productservice.service.impl;

import bartolovica.example.productservice.mapper.ProductMapper;
import bartolovica.example.productservice.model.ProductRequest;
import bartolovica.example.productservice.model.ProductResponse;
import bartolovica.example.productservice.model.dto.Product;
import bartolovica.example.productservice.repository.ProductRepository;
import bartolovica.example.productservice.service.ProductService;
import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final AsyncLoadingCache<String, BigDecimal> conversionRateCache;
    private final ProductMapper productMapper;

    @Override
    public Mono<ProductResponse> createProduct(ProductRequest productRequest, UserDetails userDetails) {
        return Mono.fromFuture(conversionRateCache.get("usdToEur"))
                .map(conversionRate -> buildProduct(productRequest, conversionRate, userDetails.getUsername()))
                .flatMap(productRepository::saveProduct)
                .flatMap(this::buildProductResponse);
    }

    @Override
    public Mono<ProductResponse> getProductByIdAndCode(String code, UUID id) {
        return productRepository.getProductByCodeAndId(code, id)
                .flatMap(this::buildProductResponse);
    }

    @Override
    public Mono<List<ProductResponse>> getProducts() {
        return productRepository.getProducts()
                .flatMap(this::buildProductResponse)
                .collectList();
    }

    @Override
    public Mono<List<ProductResponse>> getProductsPaged(int page, int size) {
        return productRepository.getProductsPaged(page, size)
                .flatMap(this::buildProductResponse)
                .collectList();
    }

    private Mono<ProductResponse> buildProductResponse(ProductResponse product) {
        return Mono.fromFuture(conversionRateCache.get("usdToEur"))
                .map(conversionRate -> product.toBuilder()
                        .priceUsd(product.priceEur().multiply(conversionRate).setScale(3, RoundingMode.HALF_UP))
                        .build());
    }

    private Product buildProduct(ProductRequest productRequest, BigDecimal conversionRate, String username) {
        return productMapper.requestToProduct(productRequest)
                .toBuilder()
                .id(UUID.randomUUID())
                .conversionRate(conversionRate)
                .createdAt(LocalDateTime.now())
                .createdBy(username)
                .updatedBy(username)
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
