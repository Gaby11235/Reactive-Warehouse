package man.api.warehouse.system.repository;

import man.api.warehouse.system.model.Inventory;
import man.api.warehouse.system.model.Order;
import man.api.warehouse.system.model.dto.InventoryDto;
import man.api.warehouse.system.model.dto.OrderDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface InventoryRepository {
    Flux<Inventory> findAllInventorys();

    Mono<Inventory> findInventoryById(String id);

    Mono<Inventory> insertInventory(InventoryDto inventoryDto);

    Mono<Boolean> updateInventory(String id, InventoryDto inventoryDto);

    Mono<Boolean> deleteInventoryById(String id);
}
