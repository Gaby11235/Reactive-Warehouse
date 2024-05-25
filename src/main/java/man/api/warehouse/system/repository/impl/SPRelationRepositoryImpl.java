package man.api.warehouse.system.repository.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import man.api.warehouse.system.model.Inventory;
import man.api.warehouse.system.model.SPRelation;
import man.api.warehouse.system.model.dto.SPRelationDto;
import man.api.warehouse.system.repository.SPRelationRepository;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.data.mongodb.core.query.Criteria.where;
@Repository
@Slf4j
@RequiredArgsConstructor
public class SPRelationRepositoryImpl implements SPRelationRepository {
    private final ReactiveMongoTemplate mongoTemplate;
    @Override
    public Flux<SPRelation> findAllSPRelations() {
        return mongoTemplate.findAll(SPRelation.class);
    }

    @Override
    public Mono<SPRelation> insertSPRelation(SPRelationDto spRelationDto) {
        return mongoTemplate.insert(
                SPRelation.builder()
                        .productId(spRelationDto.getProductId())
                        .supplierId(spRelationDto.getSupplierId())
                        .price(spRelationDto.getPrice())
                        .build());
    }

    @Override
    public Mono<Boolean> updateSPRelation(String id, SPRelationDto spRelationDto) {
        return mongoTemplate.update(SPRelation.class)
                .matching(where("id").is(id))
                .apply(Update.update("price", spRelationDto.getPrice()))
                .all()
                .map(updateResult -> updateResult.getModifiedCount() == 1L);
    }

    @Override
    public Mono<Boolean> deleteSPRelationById(String id) {
        return mongoTemplate.remove(SPRelation.class)
                .matching(where("id").is(id))
                .all()
                .map(deleteResult -> deleteResult.getDeletedCount() == 1L);
    }
}
