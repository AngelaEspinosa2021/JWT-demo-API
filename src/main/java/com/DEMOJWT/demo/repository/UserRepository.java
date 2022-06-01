package com.DEMOJWT.demo.repository;

import com.DEMOJWT.demo.dto.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface UserRepository extends ReactiveMongoRepository<User, Integer> {

}
