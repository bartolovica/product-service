package bartolovica.example.productservice.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;

@RestControllerAdvice(basePackages = {"bartolovica.example.productservice.controller"})
public class GlobalExceptionHandler {
    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<String> handleValidationExceptions(WebExchangeBindException ex) {
        String errorMessage = (ex.getFieldError() != null) ?
                ex.getFieldError().getDefaultMessage() : "Validation error";
        return ResponseEntity.badRequest().body(errorMessage);
    }
}
