package man.api.warehouse.system.controller;

import lombok.RequiredArgsConstructor;
import man.api.warehouse.system.model.dto.SPRelationDto;
import man.api.warehouse.system.service.impl.SPRelationServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class SPRelationHandler {
    private final SPRelationServiceImpl spRelationService;

    public Mono<ServerResponse> listSPRelations(ServerRequest serverRequest) {
        Flux<SPRelationDto> allSPRelations = spRelationService.findAllSPRelations();
        Mono<ServerResponse> notFound = ServerResponse.notFound().build();

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(allSPRelations, SPRelationDto.class)
                .switchIfEmpty(notFound);
    }

    public Mono<ServerResponse> saveSPRelation(ServerRequest serverRequest) {
        Mono<SPRelationDto> spRelationDtoMono = serverRequest.bodyToMono(SPRelationDto.class);
        Mono<ServerResponse> notFound = ServerResponse.notFound().build();

        return spRelationDtoMono.flatMap(spRelationDto ->
                        ServerResponse
                                .status(HttpStatus.CREATED)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(spRelationService.save(spRelationDto), SPRelationDto.class))
                .switchIfEmpty(notFound);
    }

    public Mono<ServerResponse> updateSPRelation(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");
        Mono<SPRelationDto> spRelationDtoMono = serverRequest.bodyToMono(SPRelationDto.class);
        Mono<ServerResponse> notFound = ServerResponse.notFound().build();

        return spRelationDtoMono.flatMap(spRelationDto ->
                        ServerResponse
                                .status(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(spRelationService.update(spRelationDto, id), SPRelationDto.class))
                .switchIfEmpty(notFound);
    }

    public Mono<ServerResponse> deleteSPRelation(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");
        Mono<ServerResponse> notFound = ServerResponse.notFound().build();
        return ServerResponse
                .status(HttpStatus.NO_CONTENT)
                .build(spRelationService.delete(id))
                .switchIfEmpty(notFound);
    }
}
