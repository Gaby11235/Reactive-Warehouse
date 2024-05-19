package man.api.warehouse.system.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDto {

    private Long id;

    private String name;

    private Float price;

    private Long stock;

}
