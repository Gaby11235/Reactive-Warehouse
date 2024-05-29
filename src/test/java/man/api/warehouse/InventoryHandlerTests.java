package man.api.warehouse;

import man.api.warehouse.system.model.dto.InventoryDto;
import man.api.warehouse.system.service.InventoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Collections;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class InventoryHandlerTests {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private InventoryService inventoryService;

    @Test
    public void testCreateInventory() {
        InventoryDto inventory = new InventoryDto();
        inventory.setProductId("product1");
        inventory.setQuantity(100);

        webTestClient.post().uri("/api/inventories/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(inventory), InventoryDto.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.productId").isEqualTo(inventory.getProductId())
                .jsonPath("$.quantity").isEqualTo(inventory.getQuantity());
    }

    @Test
    public void testGetAllInventories() {
        webTestClient.get().uri("/api/inventories")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(InventoryDto.class)
                .consumeWith(System.out::println);
    }

    @Test
    public void testUpdateInventory() {
        InventoryDto inventory = new InventoryDto();
        inventory.setProductId("product1");
        inventory.setQuantity(100);

        InventoryDto updatedInventory = new InventoryDto();
        updatedInventory.setProductId("product1");
        updatedInventory.setQuantity(120);

        InventoryDto savedInventory = inventoryService.save(inventory).block();

        webTestClient.put()
                .uri("/api/inventories/{id}", Collections.singletonMap("id", savedInventory.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(updatedInventory), InventoryDto.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.productId").isEqualTo(updatedInventory.getProductId())
                .jsonPath("$.quantity").isEqualTo(updatedInventory.getQuantity());
    }

    @Test
    public void testDeleteInventory() {
        InventoryDto inventory = new InventoryDto();
        inventory.setProductId("product2");
        inventory.setQuantity(200);

        InventoryDto savedInventory = inventoryService.save(inventory).block();

        webTestClient.delete()
                .uri("/api/inventories/{id}", Collections.singletonMap("id", savedInventory.getId()))
                .exchange()
                .expectStatus().isNoContent()
                .expectBody()
                .consumeWith(System.out::println);
    }
}
