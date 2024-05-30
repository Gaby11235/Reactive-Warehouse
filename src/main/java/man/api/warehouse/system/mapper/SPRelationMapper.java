package man.api.warehouse.system.mapper;

import man.api.warehouse.common.exception.ComponentException;
import man.api.warehouse.system.model.SPRelation;
import man.api.warehouse.system.model.dto.SPRelationDto;
import org.springframework.stereotype.Component;

@Component
public class SPRelationMapper {
    public SPRelationDto toDto(SPRelation spRelation) {
        if(spRelation == null) {
            throw new ComponentException("SPRelation is null");
        }
        SPRelationDto spRelationDto = new SPRelationDto();
        spRelationDto.setId(spRelation.getId());
        spRelationDto.setPrice(spRelation.getPrice());
        spRelationDto.setProductId(spRelation.getProductId());
        spRelationDto.setSupplierId(spRelation.getSupplierId());

        return spRelationDto;
    }

    public SPRelation toModel(SPRelationDto spRelationDto) {
        if(spRelationDto == null) {
            throw new ComponentException("SPRelationDto is null");
        }
        SPRelation spRelation = new SPRelation();
        spRelation.setId(spRelationDto.getId());
        spRelation.setPrice(spRelationDto.getPrice());
        spRelation.setProductId(spRelationDto.getProductId());
        spRelation.setSupplierId(spRelationDto.getSupplierId());
        return spRelation;
    }
}
