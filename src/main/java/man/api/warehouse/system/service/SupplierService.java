package man.api.warehouse.system.service;

import man.api.warehouse.system.model.dto.SupplierDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SupplierService {
    Mono<SupplierDto> save(SupplierDto supplierDto);
    Flux<SupplierDto> findAllSuppliers();
    Mono<SupplierDto> update(SupplierDto supplierDto, String id);
    Mono<Void> delete(String id);
}
