package man.api.warehouse.system.repository;

import man.api.warehouse.system.model.Product;
import man.api.warehouse.system.model.dto.ProductDto;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ProductRepository {

    Flux<Product> findAllProducts();

    Flux<ProductDto> findProductByKeyword(String keyword, int offset, int limit);

    Mono<Product> findProductById(String id);

    Mono<Product> insertProduct(ProductDto productDto);

    Mono<Boolean> updateProduct(String id, ProductDto productDto);

    Mono<Boolean> deleteProductById(String id);

    Mono<Long> countByKeyword(String keyword);

}
