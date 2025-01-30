package bartolovica.example.productservice.util.validation;

import bartolovica.example.productservice.exception.ValidationException;
import bartolovica.example.productservice.repository.ProductRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.ExecutionException;

@Slf4j
@Component
@RequiredArgsConstructor
public class UniqueCodeValidator implements ConstraintValidator<UniqueCode, String> {

    private final ProductRepository productRepository;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        try {

            Boolean exists = productRepository
                    .productExistsByCode(value)
                    .subscribeOn(Schedulers.boundedElastic())
                    .toFuture()
                    .get();
            return Boolean.FALSE.equals(exists);
        } catch (InterruptedException | ExecutionException e) {
            log.error("Validation failed due to an error: {}", e.getMessage(), e);
            Thread.currentThread().interrupt(); // Reset the interrupted status
            throw new ValidationException("Validation failed due to an error: " + e.getMessage(), e);
        }
    }
}
