package bartolovica.example.productservice.controller;

import bartolovica.example.productservice.model.ProductRequest;
import bartolovica.example.productservice.model.ProductResponse;
import bartolovica.example.productservice.model.dto.Product;
import bartolovica.example.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequestMapping("/product")
@RequiredArgsConstructor
@RestController
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public Mono<ResponseEntity<Product>> createProduct(@Validated @RequestBody ProductRequest productRequest, @AuthenticationPrincipal UserDetails userDetails) {
        log.info("User {} is creating product", userDetails.getUsername());
        return productService.createProduct(productRequest, userDetails)
                .map(product -> ResponseEntity.ok().body(product))
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @GetMapping("/{id}/{code}")
    public Mono<ResponseEntity<ProductResponse>> getProductByIdAndCode(@PathVariable UUID id, @PathVariable String code) {
        return productService.getProductByIdAndCode(code, id)
                .map(product -> ResponseEntity.ok().body(product))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping
    public Mono<ResponseEntity<List<ProductResponse>>> getProducts() {
        return productService.getProducts()
                .map(products -> ResponseEntity.ok().body(products))
                .defaultIfEmpty(ResponseEntity.noContent().build());
    }

    @GetMapping("/paged")
    public Mono<ResponseEntity<List<ProductResponse>>> getProductsPaged(@RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                                                        @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return productService.getProductsPaged(page, size)
                .map(products -> ResponseEntity.ok().body(products))
                .defaultIfEmpty(ResponseEntity.noContent().build());
    }
}
