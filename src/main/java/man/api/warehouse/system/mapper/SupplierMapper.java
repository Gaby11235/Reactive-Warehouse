package man.api.warehouse.system.mapper;

import man.api.warehouse.common.exception.ComponentException;
import man.api.warehouse.system.model.SPRelation;
import man.api.warehouse.system.model.Supplier;
import man.api.warehouse.system.model.dto.SPRelationDto;
import man.api.warehouse.system.model.dto.SupplierDto;
import org.springframework.stereotype.Component;

@Component
public class SupplierMapper {
    public static SupplierDto toDto(Supplier supplier) {
        if(supplier == null) {
            throw new ComponentException(supplier.getClass().getName());
        }
        SupplierDto supplierDto = new SupplierDto();
        supplierDto.setId(supplierDto.getId());
        supplierDto.setEmail(supplier.getEmail());
        supplierDto.setName(supplier.getName());
        supplierDto.setAddress(supplier.getAddress());

        return supplierDto;
    }
}
