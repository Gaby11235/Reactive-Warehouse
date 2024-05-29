package man.api.warehouse;

import man.api.warehouse.system.controller.InventoryHandler;
import man.api.warehouse.system.controller.OrderHandler;
import man.api.warehouse.system.controller.ProductHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@SpringBootApplication
public class WarehouseApplication {

	public static void main(String[] args) {
		SpringApplication.run(WarehouseApplication.class, args);
	}

	@Bean
	RouterFunction<ServerResponse> productRoutes(ProductHandler productHandler) {
		return nest(path("/api/products"),
				nest(accept(MediaType.APPLICATION_JSON),
						route(method(HttpMethod.GET), productHandler::listProducts)
								.andRoute(DELETE("/{id}"), productHandler::deleteProduct)
								.andRoute(POST("/"), productHandler::saveProduct)
								.andRoute(PUT("/{id}"), productHandler::updateProduct)));
	}

	@Bean
	RouterFunction<ServerResponse> inventoryRoutes(InventoryHandler inventoryHandler) {
		return nest(path("/api/inventories"),
				nest(accept(MediaType.APPLICATION_JSON),
						route(method(HttpMethod.GET), inventoryHandler::listInventories)
								.andRoute(DELETE("/{id}"), inventoryHandler::deleteInventory)
								.andRoute(POST("/"), inventoryHandler::saveInventory)
								.andRoute(PUT("/{id}"), inventoryHandler::updateInventory)));
	}

	@Bean
	public RouterFunction<ServerResponse> orderRoutes(OrderHandler orderHandler) {
		return nest(path("/api/orders"),
				nest(accept(MediaType.APPLICATION_JSON),
						route(method(HttpMethod.GET), orderHandler::listOrders)
								.andRoute(GET("/{id}"), orderHandler::getOrder)
								.andRoute(POST("/"), orderHandler::saveOrder)
								.andRoute(PUT("/{id}"), orderHandler::updateOrder)
								.andRoute(DELETE("/{id}"), orderHandler::deleteOrder)
				)
		);
	}

}
