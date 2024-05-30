package man.api.warehouse.system.service;

import man.api.warehouse.system.model.dto.SPRelationDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SPRelationService {
    Mono<SPRelationDto> save(SPRelationDto spRelationDto);
    Flux<SPRelationDto> findAllSPRelations();

    Mono<SPRelationDto> update(SPRelationDto spRelationDto, String id);

    Mono<Void> delete(String id);
}
