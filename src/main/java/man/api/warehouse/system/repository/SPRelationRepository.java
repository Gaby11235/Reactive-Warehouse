package man.api.warehouse.system.repository;

import man.api.warehouse.system.model.SPRelation;
import man.api.warehouse.system.model.dto.InventoryDto;
import man.api.warehouse.system.model.dto.SPRelationDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SPRelationRepository {
    Flux<SPRelation> findAllSPRelations();

    Mono<SPRelation> insertSPRelation(SPRelationDto spRelationDto);

    Mono<Boolean> updateSPRelation(String id, SPRelationDto spRelationDto);

    Mono<Boolean> deleteSPRelationById(String id);
}
