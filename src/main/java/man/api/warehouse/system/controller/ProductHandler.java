package man.api.warehouse.system.controller;

import lombok.RequiredArgsConstructor;
import man.api.warehouse.system.model.dto.ProductDto;
import man.api.warehouse.system.service.Impl.ProductServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ProductHandler {
    private final ProductServiceImpl productService;

    public Mono<ServerResponse> listProducts(ServerRequest serverRequest) {
        Flux<ProductDto> allProducts = productService.findAllProducts();
        Mono<ServerResponse> notFound = ServerResponse.notFound().build();

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(allProducts, ProductDto.class)
                .switchIfEmpty(notFound);
    }

    public Mono<ServerResponse> saveProduct(ServerRequest serverRequest) {
        Mono<ProductDto> productDtoMono = serverRequest.bodyToMono(ProductDto.class);
        Mono<ServerResponse> notFound = ServerResponse.notFound().build();

        return productDtoMono.flatMap(productDto ->
                        ServerResponse
                                .status(HttpStatus.CREATED)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(productService.save(productDto), ProductDto.class))
                .switchIfEmpty(notFound);
    }

    public Mono<ServerResponse> updateProduct(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");
        Mono<ProductDto> productDtoMono = serverRequest.bodyToMono(ProductDto.class);
        Mono<ServerResponse> notFound = ServerResponse.notFound().build();

        return productDtoMono.flatMap(productDto ->
                        ServerResponse
                                .status(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(productService.update(productDto, id), ProductDto.class))
                .switchIfEmpty(notFound);
    }

    public Mono<ServerResponse> deleteProduct(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");
        Mono<ServerResponse> notFound = ServerResponse.notFound().build();
        return ServerResponse
                .status(HttpStatus.NO_CONTENT)
                .build(productService.delete(id))
                .switchIfEmpty(notFound);
    }
}
