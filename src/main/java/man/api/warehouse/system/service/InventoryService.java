package man.api.warehouse.system.service;

import man.api.warehouse.system.model.dto.InventoryDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface InventoryService {
    Mono<InventoryDto> save(InventoryDto inventoryDtoDto);

    Flux<InventoryDto> findAllInventories();

    Mono<InventoryDto> update(InventoryDto inventoryDtoDto, String id);

    Mono<Void> delete(String id);
}
