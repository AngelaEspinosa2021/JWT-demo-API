package com.DEMOJWT.demo.service;

import com.DEMOJWT.demo.dto.User;
import com.DEMOJWT.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepo;

    @Override
    public Flux<User> findAll() {
        return userRepo.findAll();
    }

    @Override
    public Mono<User> save(User user) {
        return userRepo.save(user);
    }

    @Override
    public Mono<User> findById(String id) {
        return userRepo.findById(id);
    }

}
