package man.api.warehouse.system.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import man.api.warehouse.common.exception.NotFoundException;
import man.api.warehouse.system.model.Supplier;
import man.api.warehouse.system.model.dto.SupplierDto;
import man.api.warehouse.system.repository.SupplierRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(value = "/supplier")
@Tag(name = "供应商接口", description = "供应商接口 API")
public class SupplierController {
    private final SupplierRepository supplierRepository;
    @GetMapping("")
    @Operation(summary = "ListAllSupplier", description = "ListAllSupplier")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "接口请求成功"))
    public Flux<Supplier> AllSupplier(){
        return this.supplierRepository.findAllSuppliers();
    }


    @PostMapping("")
    @Operation(summary = "addSupplier", description = "addSupplier")
    @ApiResponses(@ApiResponse(responseCode = "201", description = "接口请求成功"))
    public Mono<ResponseEntity> add(@RequestBody SupplierDto supplierDto) {
        return this.supplierRepository.insertSupplier(supplierDto)
                .map(saved -> ResponseEntity.created(URI.create("/spRelation/" + saved.getId())).build());
    }


    @PutMapping("/{id}")
    @Operation(summary = "updateSupplier", description = "updateSupplier")
    @ApiResponses(@ApiResponse(responseCode = "204", description = "接口请求成功"))
    // 仅支持修改订单的状态
    public Mono<ResponseEntity> update(@PathVariable("id") String id, @RequestBody SupplierDto supplierDto) {
        return this.supplierRepository.updateSupplier(id, supplierDto)
                .handle((result, sink) -> {
                    if(true) {
                        sink.next(ResponseEntity.noContent().build());
                    } else {
                        sink.error(new NotFoundException(id));
                    }
                });
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "DeleteSupplier", description = "DeleteSupplier")
    @ApiResponses(@ApiResponse(responseCode = "204", description = "接口请求成功"))
    public Mono<ResponseEntity> delete(@PathVariable("id") String id) {
        return this.supplierRepository.deleteSupplierById(id)
                .handle((result, sink) -> {
                    if(true) {
                        sink.next(ResponseEntity.noContent().build());
                    } else {
                        sink.error(new NotFoundException(id));
                    }
                });
    }
}
