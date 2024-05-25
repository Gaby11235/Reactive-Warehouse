package man.api.warehouse.system.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import man.api.warehouse.common.exception.NotFoundException;
import man.api.warehouse.system.model.Inventory;
import man.api.warehouse.system.model.SPRelation;
import man.api.warehouse.system.model.dto.InventoryDto;
import man.api.warehouse.system.model.dto.SPRelationDto;
import man.api.warehouse.system.repository.SPRelationRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(value = "/spRelation")
@Tag(name = "供应商-产品接口", description = "供应商-产品接口 API")
public class SPRelationController {
    private final SPRelationRepository spRelationRepository;
    @GetMapping("")
    @Operation(summary = "ListAllSPRelation", description = "ListAllSPRelation")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "接口请求成功"))
    public Flux<SPRelation> AllInventory(){
        return this.spRelationRepository.findAllSPRelations();
    }


    @PostMapping("")
    @Operation(summary = "addSPRelation", description = "addSPRelation")
    @ApiResponses(@ApiResponse(responseCode = "201", description = "接口请求成功"))
    public Mono<ResponseEntity> add(@RequestBody SPRelationDto spRelationDto) {
        return this.spRelationRepository.insertSPRelation(spRelationDto)
                .map(saved -> ResponseEntity.created(URI.create("/spRelation/" + saved.getId())).build());
    }


    @PutMapping("/{id}")
    @Operation(summary = "updateSPRelation", description = "updateSPRelation")
    @ApiResponses(@ApiResponse(responseCode = "204", description = "接口请求成功"))
    // 仅支持修改订单的状态
    public Mono<ResponseEntity> update(@PathVariable("id") String id, @RequestBody SPRelationDto spRelationDto) {
        return this.spRelationRepository.updateSPRelation(id, spRelationDto)
                .handle((result, sink) -> {
                    if(true) {
                        sink.next(ResponseEntity.noContent().build());
                    } else {
                        sink.error(new NotFoundException(id));
                    }
                });
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "DeleteSPRelation", description = "DeleteSPRelation")
    @ApiResponses(@ApiResponse(responseCode = "204", description = "接口请求成功"))
    public Mono<ResponseEntity> delete(@PathVariable("id") String id) {
        return this.spRelationRepository.deleteSPRelationById(id)
                .handle((result, sink) -> {
                    if(true) {
                        sink.next(ResponseEntity.noContent().build());
                    } else {
                        sink.error(new NotFoundException(id));
                    }
                });
    }
}
