package bartolovica.example.productservice.mapper;

import bartolovica.example.productservice.model.ProductRequest;
import bartolovica.example.productservice.model.dto.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    Product requestToProduct(ProductRequest productRequest);
}
