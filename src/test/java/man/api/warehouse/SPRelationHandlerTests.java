package man.api.warehouse;

import man.api.warehouse.system.model.dto.SPRelationDto;
import man.api.warehouse.system.service.SPRelationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Collections;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SPRelationHandlerTests {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private SPRelationService spRelationService;

    @Test
    public void testCreateSPRelation() throws Exception {

        SPRelationDto spRelation = new SPRelationDto();
        spRelation.setSupplierId("supplier_1");
        spRelation.setProductId("product_1");
        spRelation.setPrice(100.0);

        // when - action or behaviour that we are going test
        webTestClient.post().uri("/api/spRelations/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(spRelation), SPRelationDto.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.supplierId").isEqualTo(spRelation.getSupplierId())
                .jsonPath("$.productId").isEqualTo(spRelation.getProductId())
                .jsonPath("$.price").isEqualTo(spRelation.getPrice());
    }

    @Test
    public void testGetAllSPRelations() {
        webTestClient.get().uri("/api/spRelations")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(SPRelationDto.class)
                .consumeWith(System.out::println);
    }

    @Test
    public void testUpdateSPRelation() throws Exception {

        SPRelationDto spRelation = new SPRelationDto();
        spRelation.setSupplierId("supplier_1");
        spRelation.setProductId("product_1");
        spRelation.setPrice(100.0);

        SPRelationDto updatedSPRelation = new SPRelationDto();
        updatedSPRelation.setSupplierId("supplier_2");
        updatedSPRelation.setProductId("product_2");
        updatedSPRelation.setPrice(150.0);

        SPRelationDto savedSPRelation = spRelationService.save(spRelation).block();

        webTestClient.put()
                .uri("/api/spRelations/{id}", Collections.singletonMap("id", savedSPRelation.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(updatedSPRelation), SPRelationDto.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.supplierId").isEqualTo(updatedSPRelation.getSupplierId())
                .jsonPath("$.productId").isEqualTo(updatedSPRelation.getProductId())
                .jsonPath("$.price").isEqualTo(updatedSPRelation.getPrice());
    }

    @Test
    public void testDeleteSPRelation() {
        SPRelationDto spRelation = new SPRelationDto();
        spRelation.setSupplierId("supplier_2");
        spRelation.setProductId("product_2");
        spRelation.setPrice(200.0);

        SPRelationDto savedSPRelation = spRelationService.save(spRelation).block();

        webTestClient.delete()
                .uri("/api/spRelations/{id}", Collections.singletonMap("id", savedSPRelation.getId()))
                .exchange()
                .expectStatus().isNoContent()
                .expectBody()
                .consumeWith(System.out::println);
    }
}
