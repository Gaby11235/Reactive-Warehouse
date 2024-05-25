package man.api.warehouse.system.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "产品接口", description = "产品接口 API")
public class ProductController {

    private final ProductRepository productRepository;

    @GetMapping("")
    @Operation(summary = "fetchAllProducts", description = "fetchAllProducts")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "接口请求成功"))
    public Mono<PaginatedResult<ProductDto>> fetchAllProducts(@RequestParam(value = "query", required = false) String query,
                                                              @RequestParam(value = "offset", defaultValue = "0") int offset,
                                                              @RequestParam(value = "limit", defaultValue = "10") int limit) {
        return this.productRepository.findProductByKeyword(query, offset, limit).collectList()
                .zipWith(this.productRepository.countByKeyword(query), PaginatedResult::new);
    }

    @PostMapping("")
    @Operation(summary = "addProduct", description = "addProduct")
    @ApiResponses(@ApiResponse(responseCode = "201", description = "接口请求成功"))
    public Mono<ResponseEntity> add(@RequestBody ProductDto productDto) {
        System.out.println(productDto.getName());
        return this.productRepository.insertProduct(productDto)
                .map(saved -> ResponseEntity.created(URI.create("/product/" + saved.getId())).build());
    }

    @GetMapping("/{id}")
    @Operation(summary = "selectProductById", description = "selectProductById")
    @ApiResponses(@ApiResponse(responseCode = "201", description = "接口请求成功"))
    public Mono<Product> get(@PathVariable("id") String id) {
        return this.productRepository.findProductById(id).switchIfEmpty(Mono.error(new ProductNotFoundException(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "updateProduct", description = "updateProduct")
    @ApiResponses(@ApiResponse(responseCode = "201", description = "接口请求成功"))
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
    @Operation(summary = "DeleteProduct", description = "DeleteProduct")
    @ApiResponses(@ApiResponse(responseCode = "204", description = "接口请求成功"))
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
