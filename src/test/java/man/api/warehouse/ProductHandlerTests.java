package man.api.warehouse;

import man.api.warehouse.system.model.dto.ProductDto;
import man.api.warehouse.system.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Collections;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductHandlerTests {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ProductService productService;

    @Test
    public void testCreateProduct() throws Exception {

        ProductDto product = new ProductDto();
        product.setName("Product 1");
        product.setPrice(100.0F);

        // when - action or behaviour that we are going test
        webTestClient.post().uri("/api/products/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(product), ProductDto.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.name").isEqualTo(product.getName())
                .jsonPath("$.price").isEqualTo(product.getPrice());
    }

    @Test
    public void testGetAllProducts() {
        webTestClient.get().uri("/api/products")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(ProductDto.class)
                .consumeWith(System.out::println);
    }

    @Test
    public void testUpdateProduct() throws Exception {

        ProductDto product = new ProductDto();
        product.setName("Product 1");
        product.setPrice(100.0F);

        ProductDto updatedProduct = new ProductDto();
        updatedProduct.setName("Product 1 updated");
        updatedProduct.setPrice(120.0F);

        ProductDto savedProduct = productService.save(product).block();

        webTestClient.put()
                .uri("/api/products/{id}", Collections.singletonMap("id", savedProduct.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(updatedProduct), ProductDto.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.name").isEqualTo(updatedProduct.getName())
                .jsonPath("$.price").isEqualTo(updatedProduct.getPrice());
    }

    @Test
    public void testDeleteProduct() {
        ProductDto product = new ProductDto();
        product.setName("Product 2");
        product.setPrice(200.0F);

        ProductDto savedProduct = productService.save(product).block();

        webTestClient.delete()
                .uri("/api/products/{id}", Collections.singletonMap("id", savedProduct.getId()))
                .exchange()
                .expectStatus().isNoContent()
                .expectBody()
                .consumeWith(System.out::println);
    }
}