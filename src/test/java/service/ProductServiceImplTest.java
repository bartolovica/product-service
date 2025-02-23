package service;

import bartolovica.example.productservice.mapper.ProductMapper;
import bartolovica.example.productservice.model.ProductRequest;
import bartolovica.example.productservice.model.ProductResponse;
import bartolovica.example.productservice.model.dto.Product;
import bartolovica.example.productservice.repository.ProductRepository;
import bartolovica.example.productservice.service.impl.ProductServiceImpl;
import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ProductServiceImplTest {

    private static final BigDecimal CONVERSION_RATE = BigDecimal.valueOf(1.2);

    @Mock
    private ProductRepository productRepository;

    @Mock
    private AsyncLoadingCache<String, BigDecimal> conversionRateCache;

    @Mock
    private ProductMapper productMapper;

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private ProductServiceImpl productServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createProductSuccessfully() {
        ProductRequest productRequest = ProductRequest.builder()
                .code("123456789")
                .name("testName")
                .priceEur(BigDecimal.valueOf(120))
                .isAvailable(true)
                .build();
        ProductResponse productResponse = getProductResponse();
        Product product = new Product();
        when(conversionRateCache.get("usdToEur")).thenReturn(CompletableFuture.completedFuture(CONVERSION_RATE));
        when(productMapper.requestToProduct(any())).thenReturn(product);
        when(productRepository.saveProduct(any())).thenReturn(Mono.just(productResponse));
        when(userDetails.getUsername()).thenReturn("testUser");

        Mono<ProductResponse> result = productServiceImpl.createProduct(productRequest, userDetails);


        StepVerifier.create(result)
                .assertNext(response -> {
                    assertEquals(productResponse.id(), response.id());
                    assertEquals(productResponse.priceEur(), response.priceEur());
                    assertEquals(productResponse.priceEur().multiply(CONVERSION_RATE).setScale(3, RoundingMode.HALF_UP), response.priceUsd());
                    assertEquals(productResponse.name(), response.name());
                    assertEquals(productResponse.code(), response.code());
                    assertEquals(productResponse.isAvailable(), response.isAvailable());
                })
                .verifyComplete();
    }

    @Test
    void createProductWithCacheFailure() {
        ProductRequest productRequest = ProductRequest.builder().build();
        when(conversionRateCache.get("usdToEur")).thenReturn(CompletableFuture.failedFuture(new RuntimeException("Cache error")));

        Mono<ProductResponse> result = productServiceImpl.createProduct(productRequest, userDetails);

        StepVerifier.create(result)
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    void getProductByIdAndCodeSuccessfully() {
        UUID id = UUID.randomUUID();
        String code = "testCode";
        var productResponse = getProductResponse();
        when(conversionRateCache.get("usdToEur")).thenReturn(CompletableFuture.completedFuture(CONVERSION_RATE));
        when(productRepository.getProductByCodeAndId(code, id)).thenReturn(Mono.just(productResponse));

        Mono<ProductResponse> result = productServiceImpl.getProductByIdAndCode(code, id);

        StepVerifier.create(result)
                .assertNext(response -> {
                    assertEquals(productResponse.id(), response.id());
                    assertEquals(productResponse.priceEur(), response.priceEur());
                    assertEquals(productResponse.priceEur().multiply(CONVERSION_RATE).setScale(3, RoundingMode.HALF_UP), response.priceUsd());
                    assertEquals(productResponse.name(), response.name());
                    assertEquals(productResponse.code(), response.code());
                    assertEquals(productResponse.isAvailable(), response.isAvailable());
                })
                .verifyComplete();
    }

    @Test
    void getProductsSuccessfully() {
        var productResponse = getProductResponse();
        when(productRepository.getProducts()).thenReturn(Flux.just(productResponse));
        when(conversionRateCache.get("usdToEur")).thenReturn(CompletableFuture.completedFuture(CONVERSION_RATE));
        Mono<List<ProductResponse>> result = productServiceImpl.getProducts();

        StepVerifier.create(result)
                .assertNext(responseList -> {
                    assertEquals(1, responseList.size());
                    final var response = responseList.get(0);
                    assertEquals(productResponse.id(), response.id());
                    assertEquals(productResponse.priceEur(), response.priceEur());
                    assertEquals(productResponse.priceEur().multiply(CONVERSION_RATE).setScale(3, RoundingMode.HALF_UP), response.priceUsd());
                    assertEquals(productResponse.name(), response.name());
                    assertEquals(productResponse.code(), response.code());
                    assertEquals(productResponse.isAvailable(), response.isAvailable());
                })
                .verifyComplete();
    }

    @Test
    void getProductsPagedSuccessfully() {
        int page = 0;
        int size = 10;
        var productResponse = getProductResponse();
        when(conversionRateCache.get("usdToEur")).thenReturn(CompletableFuture.completedFuture(CONVERSION_RATE));
        when(productRepository.getProductsPaged(page, size)).thenReturn(Flux.just(productResponse));

        Mono<List<ProductResponse>> result = productServiceImpl.getProductsPaged(page, size);

        StepVerifier.create(result)
                .assertNext(responseList -> {
                    assertEquals(1, responseList.size());
                    final var response = responseList.get(0);
                    assertEquals(productResponse.id(), response.id());
                    assertEquals(productResponse.priceEur(), response.priceEur());
                    assertEquals(productResponse.priceEur().multiply(CONVERSION_RATE).setScale(3, RoundingMode.HALF_UP), response.priceUsd());
                    assertEquals(productResponse.name(), response.name());
                    assertEquals(productResponse.code(), response.code());
                    assertEquals(productResponse.isAvailable(), response.isAvailable());
                })
                .verifyComplete();
    }

    private ProductResponse getProductResponse() {
        return ProductResponse.builder()
                .id(UUID.randomUUID())
                .priceEur(BigDecimal.valueOf(120))
                .name("testName")
                .code("123456789")
                .isAvailable(true)
                .build();
    }
}
