package man.api.warehouse.system.model.dto;

import java.util.List;

public record PaginatedResult<T>(List<T> data, Long count) {
}
