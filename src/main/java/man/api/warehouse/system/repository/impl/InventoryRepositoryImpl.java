package man.api.warehouse.system.repository.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import man.api.warehouse.system.model.Inventory;
import man.api.warehouse.system.model.Order;
import man.api.warehouse.system.model.dto.InventoryDto;
import man.api.warehouse.system.repository.InventoryRepository;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Repository
@Slf4j
@RequiredArgsConstructor
public class InventoryRepositoryImpl implements InventoryRepository {
    private final ReactiveMongoTemplate mongoTemplate;
    @Override
    public Flux<Inventory> findAllInventorys() {
        return mongoTemplate.findAll(Inventory.class);
    }

    @Override
    public Mono<Inventory> findInventoryById(String id) {
        return mongoTemplate.findById(id, Inventory.class);
    }

    @Override
    public Mono<Inventory> insertInventory(InventoryDto inventoryDto) {
        return mongoTemplate.insert(
                Inventory.builder()
                        .id(inventoryDto.getId())
                        .productId(inventoryDto.getProductId())
                        .quantity(inventoryDto.getQuantity())
                        .build());
    }

    @Override
    public Mono<Boolean> updateInventory(String id, InventoryDto inventoryDto) {
        return mongoTemplate.update(Inventory.class)
                .matching(where("id").is(id))
                .apply(Update.update("quantity", inventoryDto.getQuantity()))
                .all()
                .map(updateResult -> updateResult.getModifiedCount() == 1L);
    }

    @Override
    public Mono<Boolean> deleteInventoryById(String id) {
        return mongoTemplate.remove(Inventory.class)
                .matching(where("id").is(id))
                .all()
                .map(deleteResult -> deleteResult.getDeletedCount() == 1L);
    }
}
