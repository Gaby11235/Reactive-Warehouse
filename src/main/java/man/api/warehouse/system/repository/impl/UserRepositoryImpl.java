package man.api.warehouse.system.repository.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import man.api.warehouse.system.model.User;
import man.api.warehouse.system.repository.UserRepository;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;
@Repository
@Slf4j
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final ReactiveMongoTemplate mongoTemplate;
    @Override
    public Mono<User> findByUsername(String username) {
        return this.mongoTemplate.findOne(query(where("username").is(username)), User.class);
    }

    @Override
    public Mono<User> create(User user) {
        return this.mongoTemplate.insert(user);
    }

}
