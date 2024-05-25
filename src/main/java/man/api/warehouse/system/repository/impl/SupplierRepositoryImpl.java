package man.api.warehouse.system.repository.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import man.api.warehouse.system.model.Inventory;
import man.api.warehouse.system.model.Supplier;
import man.api.warehouse.system.model.dto.SupplierDto;
import man.api.warehouse.system.repository.SupplierRepository;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.data.mongodb.core.query.Criteria.where;
@Repository
@Slf4j
@RequiredArgsConstructor
public class SupplierRepositoryImpl implements SupplierRepository {
    private final ReactiveMongoTemplate mongoTemplate;
    @Override
    public Flux<Supplier> findAllSuppliers() {
        return mongoTemplate.findAll(Supplier.class);
    }

    @Override
    public Mono<Supplier> insertSupplier(SupplierDto supplierDto) {
        return mongoTemplate.insert(
                Supplier.builder()
                        .id(supplierDto.getId())
                        .address(supplierDto.getAddress())
                        .name(supplierDto.getName())
                        .email(supplierDto.getEmail())
                        .build());
    }

    @Override
    public Mono<Boolean> updateSupplier(String id, SupplierDto supplierDto) {
        return mongoTemplate.update(Supplier.class)
                .matching(where("id").is(id))
                .apply(Update.update("name", supplierDto.getName()).set("address",supplierDto.getName()).set("email",supplierDto.getEmail()))
                .all()
                .map(updateResult -> updateResult.getModifiedCount() == 1L);
    }

    @Override
    public Mono<Boolean> deleteSupplierById(String id) {
        return mongoTemplate.remove(Supplier.class)
                .matching(where("id").is(id))
                .all()
                .map(deleteResult -> deleteResult.getDeletedCount() == 1L);
    }
}
