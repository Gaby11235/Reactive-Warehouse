package man.api.warehouse;

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

}
