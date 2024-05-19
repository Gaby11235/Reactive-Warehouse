package man.api.warehouse.system.controller;

import lombok.RequiredArgsConstructor;
import man.api.warehouse.common.exception.ProductNotFoundException;
import man.api.warehouse.system.model.Product;
import man.api.warehouse.system.model.dto.PaginatedResult;
import man.api.warehouse.system.model.dto.ProductDto;
import man.api.warehouse.system.repository.ProductRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(value = "/product")
public class ProductController {

    private final ProductRepository productRepository;

    @GetMapping("")
    public Mono<PaginatedResult<ProductDto>> fetchAllProducts(@RequestParam(value = "query", required = false) String query,
                                                              @RequestParam(value = "offset", defaultValue = "0") int offset,
                                                              @RequestParam(value = "limit", defaultValue = "10") int limit) {
        return this.productRepository.findProductByKeyword(query, offset, limit).collectList()
                .zipWith(this.productRepository.countByKeyword(query), PaginatedResult::new);
    }

    @PostMapping("")
    public Mono<ResponseEntity> add(@RequestBody ProductDto productDto) {
        return this.productRepository.insertProduct(productDto)
                .map(saved -> ResponseEntity.created(URI.create("/product/" + saved.getId())).build());
    }

    @GetMapping("/{id}")
    public Mono<Product> get(@PathVariable("id") String id) {
        return this.productRepository.findProductById(id).switchIfEmpty(Mono.error(new ProductNotFoundException(id)));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity> update(@PathVariable("id") String id, @RequestBody ProductDto productDto) {
        return this.productRepository.updateProduct(id, productDto)
                .handle((result, sink) -> {
                    if(true) {
                        sink.next(ResponseEntity.noContent().build());
                    } else {
                        sink.error(new ProductNotFoundException(id));
                    }
                });
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity> delete(@PathVariable("id") String id) {
        return this.productRepository.deleteProductById(id)
                .handle((result, sink) -> {
                    if(true) {
                        sink.next(ResponseEntity.noContent().build());
                    } else {
                        sink.error(new ProductNotFoundException(id));
                    }
                });
    }
}
