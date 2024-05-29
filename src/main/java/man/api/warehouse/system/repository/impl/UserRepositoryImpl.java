package man.api.warehouse.system.repository.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import man.api.warehouse.system.model.User;
import man.api.warehouse.system.model.dto.UserDto;
import man.api.warehouse.system.repository.UserRepository;
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
public class UserRepositoryImpl implements UserRepository {
    private final ReactiveMongoTemplate mongoTemplate;

    @Override
    public Flux<User> findAllUsers() {
        return mongoTemplate.findAll(User.class);
    }
    @Override
    public Mono<User> findByUsername(String username) {
        return this.mongoTemplate.findOne(query(where("username").is(username)), User.class);
    }

    @Override
    public Mono<User> create(User user) {
        return this.mongoTemplate.insert(user);
    }

    @Override
    public Mono<Boolean> updateUser(String id, UserDto userDto) {
        return mongoTemplate.update(User.class)
                .matching(where("id").is(id))
                .apply(Update.update("username", userDto.getUsername()).set("password",userDto.getPassword()))
                .all()
                .map(updateResult -> updateResult.getModifiedCount() == 1L);
    }

    @Override
    public Mono<Boolean> deleteUserById(String id) {
        return mongoTemplate.remove(User.class)
                .matching(where("id").is(id))
                .all()
                .map(deleteResult -> deleteResult.getDeletedCount() == 1L);
    }

}
