package man.api.warehouse;

import man.api.warehouse.system.model.dto.OrderDto;
import man.api.warehouse.system.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Collections;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderHandlerTests {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private OrderService orderService;

    @Test
    public void testListOrders() {
        webTestClient.get().uri("/api/orders")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(OrderDto.class)
                .consumeWith(System.out::println); // Print response body for debugging
    }

    @Test
    public void testSaveOrder() {
        OrderDto order = new OrderDto();
        order.setOrderNumber("ORD456");
        order.setUserId(456);
        order.setProductId("product-456");
        order.setQuantity(10);

        webTestClient.post().uri("/api/orders/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(order), OrderDto.class)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.orderNumber").isEqualTo(order.getOrderNumber())
                .jsonPath("$.userId").isEqualTo(order.getUserId())
                .jsonPath("$.productId").isEqualTo(order.getProductId())
                .jsonPath("$.quantity").isEqualTo(order.getQuantity());
    }

    @Test
    public void testUpdateOrder() {
        OrderDto orderDto = new OrderDto();
        orderDto.setOrderNumber("ORD123");
        orderDto.setUserId(123);
        orderDto.setProductId("product-123");
        orderDto.setQuantity(5);

        OrderDto updatedOrderDto = new OrderDto();
        updatedOrderDto.setOrderNumber("ORD456");
        updatedOrderDto.setUserId(456);
        updatedOrderDto.setProductId("product-456");
        updatedOrderDto.setQuantity(10);

        OrderDto savedOrder = orderService.save(orderDto).block();

        webTestClient.put()
                .uri("/api/orders/{id}", Collections.singletonMap("id", savedOrder.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(updatedOrderDto), OrderDto.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.orderNumber").isEqualTo(updatedOrderDto.getOrderNumber())
                .jsonPath("$.userId").isEqualTo(updatedOrderDto.getUserId())
                .jsonPath("$.productId").isEqualTo(updatedOrderDto.getProductId())
                .jsonPath("$.quantity").isEqualTo(updatedOrderDto.getQuantity());
    }

    @Test
    public void testDeleteOrder() {
        OrderDto order = new OrderDto();
        order.setOrderNumber("ORD123");

        OrderDto savedOrder = orderService.save(order).block();

        webTestClient.delete()
                .uri("/api/orders/{id}", Collections.singletonMap("id", savedOrder.getId()))
                .exchange()
                .expectStatus().isNoContent()
                .expectBody()
                .consumeWith(System.out::println);
    }

}
