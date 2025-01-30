package bartolovica.example.productservice.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.validation.FieldError;

import java.util.Optional;

@RestControllerAdvice(basePackages = {"bartolovica.example.productservice.controller"})
public class GlobalExceptionHandler {
    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<String> handleValidationExceptions(WebExchangeBindException ex) {
        Optional<FieldError> fieldError = Optional.ofNullable(ex.getFieldError());
        String errorMessage = fieldError.map(FieldError::getDefaultMessage).orElse("Validation error");
        return ResponseEntity.badRequest().body(errorMessage);
    }
}