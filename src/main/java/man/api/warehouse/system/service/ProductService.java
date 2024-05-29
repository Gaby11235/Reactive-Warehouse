package man.api.warehouse.system.service;

import man.api.warehouse.system.model.dto.ProductDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductService {
    Mono<ProductDto> save(ProductDto productDto);

    Flux<ProductDto> findAllProducts();

    Mono<ProductDto> update(ProductDto productDto, String id);

    Mono<Void> delete(String id);
}
