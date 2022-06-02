package com.DEMOJWT.demo.repository;

import com.DEMOJWT.demo.dto.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends MongoRepository<User, String> {

     User findByUser(String User);

}
