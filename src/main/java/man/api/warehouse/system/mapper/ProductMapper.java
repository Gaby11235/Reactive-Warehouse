package man.api.warehouse.system.mapper;

import man.api.warehouse.common.exception.ComponentException;
import man.api.warehouse.system.model.Product;
import man.api.warehouse.system.model.dto.ProductDto;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public static ProductDto toDto(Product product) {
        if(product == null) {
            throw new ComponentException(product.getClass().getName());
        }
        ProductDto productDto = new ProductDto();
        productDto.setId(productDto.getId());
        productDto.setName(product.getName());
        productDto.setPrice(product.getPrice());
        productDto.setStock(product.getStock());
        return productDto;
    }

    public Product toModel(ProductDto productDto) {
        if(productDto == null) {
            throw new ComponentException(productDto.getClass().getName());
        }
        Product product = new Product();
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        product.setStock(productDto.getStock());
        return product;
    }
}
