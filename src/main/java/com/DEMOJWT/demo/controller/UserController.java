package com.DEMOJWT.demo.controller;

import com.DEMOJWT.demo.dto.User;
import com.DEMOJWT.demo.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class UserController {

    @Autowired
    private UserService service;

    @GetMapping
    public Mono<ResponseEntity<Flux<User>>> listarUsuarios() {
        return Mono.just(ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(service.findAll()));
    }

    @PostMapping("/registrar")
    public Mono<ResponseEntity<User>> crearUsuario(User user) {
        return service.save(user)
                .map(element -> ResponseEntity.created(URI.create("/users".concat(element.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(element));
    }

    @PostMapping
    public Mono<ResponseEntity<Map<String, Object>>> guardarUsuario(@RequestBody Mono<User> monoUser) {
        Map<String, Object> resp = new HashMap<>();
        return monoUser.flatMap(user -> {
            return service.save(user).map(element -> {
                resp.put("user", user);
                resp.put("mensaje", "Usuario creado con éxito");
                resp.put("timestamp", new Date());
                return ResponseEntity
                        .created(URI.create("/users".concat(element.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(resp);
            });
        }).onErrorResume(t -> {
            return Mono.just(t).cast(WebExchangeBindException.class)
                    .flatMap(e -> Mono.just(e.getFieldErrors()))
                    .flatMapMany(Flux::fromIterable)
                    .map(fieldErrors -> "El campo: " + fieldErrors.getField() + fieldErrors.getDefaultMessage())
                    .collectList()
                    .flatMap(list -> {
                        resp.put("errors", list);
                        resp.put("timestamp", new Date());
                        resp.put("status", HttpStatus.BAD_REQUEST.value());

                        return Mono.just(ResponseEntity.badRequest().body(resp));
                    });
        });
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<User>> buscarUsuario(@PathVariable String id) {
        return service.findById(id).map(element -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(element))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping("/user")
    public Mono<ResponseEntity<User>> login(@RequestParam("user") String username, @RequestParam("password") String pwd) {

        String token = getJWTToken(username);
        User user = new User(username, pwd, token);

        return service.save(user)
                .map(element -> ResponseEntity.created(URI.create("/users".concat(element.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(element));
    }

    private String getJWTToken(String username) {
        String secretKey = "mySecretKey";
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList("ROLE_USER");

        String token = Jwts
                .builder()
                .setId("sofkaJWT")
                .setSubject(username)
                .claim("authorities",
                        grantedAuthorities.stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 600000))
                .signWith(SignatureAlgorithm.HS512,
                        secretKey.getBytes()).compact();

        return "Valido " + token;
    }
}
