package man.api.warehouse.system.repository;

import man.api.warehouse.system.model.SPRelation;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface SPRelationReactiveRepository extends ReactiveCrudRepository<SPRelation, String> {
}
