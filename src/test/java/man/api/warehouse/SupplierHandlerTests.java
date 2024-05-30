package man.api.warehouse;

import man.api.warehouse.system.model.dto.SupplierDto;
import man.api.warehouse.system.service.SupplierService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Collections;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SupplierHandlerTests {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private SupplierService supplierService;

    @Test
    public void testCreateSupplier() throws Exception {
        SupplierDto supplier = new SupplierDto();
        supplier.setName("Supplier 1");
        supplier.setEmail("supplier1@example.com");
        supplier.setAddress("123 Street");

        // when - action or behaviour that we are going test
        webTestClient.post().uri("/api/suppliers/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(supplier), SupplierDto.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.name").isEqualTo(supplier.getName())
                .jsonPath("$.email").isEqualTo(supplier.getEmail())
                .jsonPath("$.address").isEqualTo(supplier.getAddress());
    }

    @Test
    public void testGetAllSuppliers() {
        webTestClient.get().uri("/api/suppliers")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(SupplierDto.class)
                .consumeWith(System.out::println);
    }

    @Test
    public void testUpdateSupplier() throws Exception {
        SupplierDto supplier = new SupplierDto();
        supplier.setName("Supplier 1");
        supplier.setEmail("supplier1@example.com");
        supplier.setAddress("123 Street");

        SupplierDto updatedSupplier = new SupplierDto();
        updatedSupplier.setName("Supplier 1 updated");
        updatedSupplier.setEmail("supplier1_updated@example.com");
        updatedSupplier.setAddress("456 Avenue");

        SupplierDto savedSupplier = supplierService.save(supplier).block();

        webTestClient.put()
                .uri("/api/suppliers/{id}", Collections.singletonMap("id", savedSupplier.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(updatedSupplier), SupplierDto.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.name").isEqualTo(updatedSupplier.getName())
                .jsonPath("$.email").isEqualTo(updatedSupplier.getEmail())
                .jsonPath("$.address").isEqualTo(updatedSupplier.getAddress());
    }

    @Test
    public void testDeleteSupplier() {
        SupplierDto supplier = new SupplierDto();
        supplier.setName("Supplier 2");
        supplier.setEmail("supplier2@example.com");
        supplier.setAddress("789 Road");

        SupplierDto savedSupplier = supplierService.save(supplier).block();

        webTestClient.delete()
                .uri("/api/suppliers/{id}", Collections.singletonMap("id", savedSupplier.getId()))
                .exchange()
                .expectStatus().isNoContent()
                .expectBody()
                .consumeWith(System.out::println);
    }
}
