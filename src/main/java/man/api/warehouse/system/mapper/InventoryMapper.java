package man.api.warehouse.system.mapper;

import man.api.warehouse.common.exception.ComponentException;
import man.api.warehouse.system.model.Inventory;
import man.api.warehouse.system.model.Order;
import man.api.warehouse.system.model.dto.InventoryDto;
import man.api.warehouse.system.model.dto.OrderDto;
import org.springframework.stereotype.Component;

@Component
public class InventoryMapper {
    public static InventoryDto toDto(Inventory inventory) {
        if(inventory == null) {
            throw new ComponentException(inventory.getClass().getName());
        }
        InventoryDto inventoryDto = new InventoryDto();
        inventoryDto.setQuantity(inventory.getQuantity());
        inventoryDto.setProductId(inventory.getProductId());
        inventoryDto.setId(inventoryDto.getId());
        return inventoryDto;
    }
}
