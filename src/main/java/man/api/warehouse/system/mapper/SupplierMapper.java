package man.api.warehouse.system.mapper;

import man.api.warehouse.common.exception.ComponentException;
import man.api.warehouse.system.model.Supplier;
import man.api.warehouse.system.model.dto.SupplierDto;
import org.springframework.stereotype.Component;

@Component
public class SupplierMapper {
    public SupplierDto toDto(Supplier supplier) {
        if (supplier == null) {
            throw new ComponentException("Supplier is null");
        }
        SupplierDto supplierDto = new SupplierDto();
        supplierDto.setId(supplier.getId());
        supplierDto.setEmail(supplier.getEmail());
        supplierDto.setName(supplier.getName());
        supplierDto.setAddress(supplier.getAddress());

        return supplierDto;
    }

    public static Supplier toModel(SupplierDto supplierDto) {
        if (supplierDto == null) {
            throw new ComponentException("SupplierDto is null");
        }
        Supplier supplier = new Supplier();
        supplier.setId(supplierDto.getId());
        supplier.setEmail(supplierDto.getEmail());
        supplier.setName(supplierDto.getName());
        supplier.setAddress(supplierDto.getAddress());

        return supplier;
    }
}
