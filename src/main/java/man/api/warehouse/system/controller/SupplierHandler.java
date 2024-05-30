package man.api.warehouse.system.controller;

import lombok.RequiredArgsConstructor;
import man.api.warehouse.system.model.dto.SupplierDto;
import man.api.warehouse.system.service.impl.SupplierServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class SupplierHandler {
    private final SupplierServiceImpl supplierService;

    public Mono<ServerResponse> listSuppliers(ServerRequest serverRequest) {
        Flux<SupplierDto> allSuppliers = supplierService.findAllSuppliers();
        Mono<ServerResponse> notFound = ServerResponse.notFound().build();

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(allSuppliers, SupplierDto.class)
                .switchIfEmpty(notFound);
    }

    public Mono<ServerResponse> saveSupplier(ServerRequest serverRequest) {
        Mono<SupplierDto> supplierDtoMono = serverRequest.bodyToMono(SupplierDto.class);
        Mono<ServerResponse> notFound = ServerResponse.notFound().build();

        return supplierDtoMono.flatMap(supplierDto ->
                        ServerResponse
                                .status(HttpStatus.CREATED)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(supplierService.save(supplierDto), SupplierDto.class))
                .switchIfEmpty(notFound);
    }

    public Mono<ServerResponse> updateSupplier(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");
        Mono<SupplierDto> supplierDtoMono = serverRequest.bodyToMono(SupplierDto.class);
        Mono<ServerResponse> notFound = ServerResponse.notFound().build();

        return supplierDtoMono.flatMap(supplierDto ->
                        ServerResponse
                                .status(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(supplierService.update(supplierDto, id), SupplierDto.class))
                .switchIfEmpty(notFound);
    }

    public Mono<ServerResponse> deleteSupplier(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");
        Mono<ServerResponse> notFound = ServerResponse.notFound().build();
        return ServerResponse
                .status(HttpStatus.NO_CONTENT)
                .build(supplierService.delete(id))
                .switchIfEmpty(notFound);
    }
}
