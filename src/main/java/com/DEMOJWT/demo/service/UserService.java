package com.DEMOJWT.demo.service;


import com.DEMOJWT.demo.dto.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {

    public Flux<User> findAll();

    public Mono<User> save(User user);

    public Mono<User> findById (String id);
}
