package com.DEMOJWT.demo.repository;

import com.DEMOJWT.demo.dto.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveMongoRepository<User, String> {

    Mono<User> findByUser(String user);
}
