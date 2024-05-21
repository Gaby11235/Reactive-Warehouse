package man.api.warehouse;

import man.api.warehouse.system.controller.ProductController;
import man.api.warehouse.system.mapper.ProductMapper;
import man.api.warehouse.system.model.Product;
import man.api.warehouse.system.repository.ProductRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.access.SecurityConfig;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@WebFluxTest(controllers = ProductController.class)
public class ProductControllerTest {

    @TestConfiguration
    @Import(SecurityConfig.class)
    static class TestConfig {}

    @Autowired
    WebTestClient client;

    @MockBean
    ProductRepository productRepository;

    @Test
    public void createProduct() {
        Product product = Product.builder().name("water").price(2.50F).stock(100L).build();
        given(productRepository.insertProduct(ProductMapper.toDto(product)))
                .willReturn(Mono.just(Product.builder().id("1").name("water").price(2.50F).stock(100L).build()));
        client.post().uri("/product").body(BodyInserters.fromValue(product))
                .exchange()
                .expectStatus().isCreated()
                .expectBody().isEmpty();
        verify(this.productRepository, times(1)).insertProduct(any());
        verifyNoMoreInteractions(this.productRepository);
    }
}
