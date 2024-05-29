package man.api.warehouse.system.service.Impl;

import lombok.AllArgsConstructor;
import man.api.warehouse.system.service.ProductService;
import man.api.warehouse.system.mapper.ProductMapper;
import man.api.warehouse.system.model.Product;
import man.api.warehouse.system.model.dto.ProductDto;
import man.api.warehouse.system.repository.ProductReactiveRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private ProductReactiveRepository productReactiveRepository;
    private ProductMapper productMapper;

    @Override
    public Mono<ProductDto> save(ProductDto productDto) {
        Product product = productMapper.toModel(productDto);
        product.setCreatedDate(LocalDateTime.now());
        product.setLastModifiedDate(LocalDateTime.now());
        return productReactiveRepository.save(product).map(p -> {
                    productDto.setId(p.getId());
                    return productDto;
                }
        );
    }

    @Override
    public Flux<ProductDto> findAllProducts() {
        return productReactiveRepository.findAll()
                .map(productMapper::toDto)
                .switchIfEmpty(Flux.empty());
    }

    @Override
    public Mono<ProductDto> update(ProductDto productDto, String id) {
        return productReactiveRepository.findById(id)
                .flatMap(existingProduct -> {
                    Product product = productMapper.toModel(productDto);
                    product.setId(existingProduct.getId());
                    return productReactiveRepository.save(product);
                })
                .map(productMapper::toDto);
    }

    @Override
    public Mono<Void> delete(String id) {
        return productReactiveRepository.deleteById(id);
    }
}
