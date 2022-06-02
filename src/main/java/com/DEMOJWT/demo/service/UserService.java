package com.DEMOJWT.demo.service;

import com.DEMOJWT.demo.dto.User;
import com.DEMOJWT.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public List<User> obtenerTodos() {
        List<User> users = (List<User>) userRepository.findAll();
        return users;
    }

    public User crear(User user) {
       return userRepository.save(user);
    }
    
    public User obtenerPorUser(String username){
        return userRepository.findByUser(username);
    }

}
