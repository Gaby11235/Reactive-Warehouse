package man.api.warehouse.system.mapper;

import man.api.warehouse.common.exception.ComponentException;
import man.api.warehouse.system.model.Inventory;
import man.api.warehouse.system.model.SPRelation;
import man.api.warehouse.system.model.dto.InventoryDto;
import man.api.warehouse.system.model.dto.SPRelationDto;
import org.springframework.stereotype.Component;

@Component
public class SPRelationMapper {
    public static SPRelationDto toDto(SPRelation spRelation) {
        if(spRelation == null) {
            throw new ComponentException(spRelation.getClass().getName());
        }
        SPRelationDto spRelationDto = new SPRelationDto();
        spRelationDto.setId(spRelationDto.getId());
        spRelationDto.setPrice(spRelation.getPrice());
        spRelationDto.setProductId(spRelation.getProductId());
        spRelationDto.setSupplierId(spRelation.getSupplierId());

        return spRelationDto;
    }
}
