package man.api.warehouse.system.repository.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import man.api.warehouse.system.mapper.ProductMapper;
import man.api.warehouse.system.model.Product;
import man.api.warehouse.system.model.dto.ProductDto;
import man.api.warehouse.system.repository.ProductRepository;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Repository
@Slf4j
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository{

    private final ReactiveMongoTemplate mongoTemplate;

    @Override
    public Flux<Product> findAllProducts() {
        return mongoTemplate.findAll(Product.class);
    }

    @Override
    public Flux<ProductDto> findProductByKeyword(String keyword, int offset, int limit) {
        return mongoTemplate.find(
                query(where("name").regex(".*" + keyword + ".*", "i"))
                        .skip(offset)
                        .limit(limit),
                Product.class
        ).map(ProductMapper::toDto);
    }

    @Override
    public Mono<Product> findProductById(String id) {
        return mongoTemplate.findById(id, Product.class);
    }

    @Override
    public Mono<Product> insertProduct(ProductDto productDto) {
        return mongoTemplate.insert(
                Product.builder()
                        .name(productDto.getName())
                        .price(productDto.getPrice())
                        .stock(productDto.getStock())
                        .build()
        );
    }

    @Override
    public Mono<Boolean> updateProduct(String id, ProductDto productDto) {
        return mongoTemplate.update(Product.class)
                .matching(where("id").is(id))
                .apply(Update.update("name", productDto.getName()).set("price", productDto.getPrice()).set("stock", productDto.getStock()))
                .all()
                .map(updateResult -> updateResult.getModifiedCount() == 1L);
    }

    @Override
    public Mono<Boolean> deleteProductById(String id) {
        return mongoTemplate.remove(Product.class)
                .matching(where("id").is(id))
                .all()
                .map(deleteResult -> deleteResult.getDeletedCount() == 1L);
    }

    @Override
    public Mono<Long> countByKeyword(String keyword) {
        return mongoTemplate.count(query(where("name").regex(".*" + keyword + ".*", "i")), Product.class);
    }
}
