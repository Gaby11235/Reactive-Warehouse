package man.api.warehouse.system.repository;

import man.api.warehouse.system.model.Inventory;
import man.api.warehouse.system.model.Supplier;
import man.api.warehouse.system.model.dto.InventoryDto;
import man.api.warehouse.system.model.dto.SupplierDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SupplierRepository {
    Flux<Supplier> findAllSuppliers();

    Mono<Supplier> insertSupplier(SupplierDto supplierDto);

    Mono<Boolean> updateSupplier(String id, SupplierDto supplierDto);

    Mono<Boolean> deleteSupplierById(String id);
}
