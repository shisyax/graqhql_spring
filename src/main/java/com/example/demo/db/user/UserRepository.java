package com.example.demo.db.user;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;


public interface UserRepository extends ReactiveCrudRepository<User, Long> {
}
