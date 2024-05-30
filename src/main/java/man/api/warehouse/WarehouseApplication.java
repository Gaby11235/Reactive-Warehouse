package man.api.warehouse;

import man.api.warehouse.system.controller.*;
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
								.andRoute(POST("/"), orderHandler::saveOrder)
								.andRoute(PUT("/{id}"), orderHandler::updateOrder)
								.andRoute(DELETE("/{id}"), orderHandler::deleteOrder)
				)
		);
	}

	@Bean
	RouterFunction<ServerResponse> spRelationRoutes(SPRelationHandler spRelationHandler) {
		return nest(path("/api/spRelations"),
				nest(accept(MediaType.APPLICATION_JSON),
						route(method(HttpMethod.GET), spRelationHandler::listSPRelations)
								.andRoute(DELETE("/{id}"), spRelationHandler::deleteSPRelation)
								.andRoute(POST("/"), spRelationHandler::saveSPRelation)
								.andRoute(PUT("/{id}"), spRelationHandler::updateSPRelation)));
	}

	@Bean
	public RouterFunction<ServerResponse> supplierRoutes(SupplierHandler supplierHandler) {
		return nest(path("/api/suppliers"),
				nest(accept(MediaType.APPLICATION_JSON),
						route(method(HttpMethod.GET), supplierHandler::listSuppliers)
								.andRoute(DELETE("/{id}"), supplierHandler::deleteSupplier)
								.andRoute(POST("/"), supplierHandler::saveSupplier)
								.andRoute(PUT("/{id}"), supplierHandler::updateSupplier)));
	}

	@Bean
	public RouterFunction<ServerResponse> userRoutes(UserHandler userHandler) {
		return nest(path("/api/users"),
				nest(accept(MediaType.APPLICATION_JSON),
						route(method(HttpMethod.GET), userHandler::listUsers)
								.andRoute(POST("/"), userHandler::saveUser)
								.andRoute(PUT("/{id}"), userHandler::updateUser)
								.andRoute(DELETE("/{id}"), userHandler::deleteUser)
								.andRoute(POST("/login"), userHandler::login)));

	}

}
