package com.example.demo.graphql.hello;

import com.example.demo.db.user.User;
import com.example.demo.db.user.UserRepository;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.ExecutionException;

@Slf4j
@DgsComponent
@AllArgsConstructor
public class UserFetcher {
    private UserRepository userRepository;

    @DgsQuery
    public User user(long id) throws ExecutionException, InterruptedException {
        log.info("Fetching user with id {}", id);
        return userRepository.findById(id).toFuture().get();
    }

    @DgsMutation
    public User createUser(String name, String email) throws ExecutionException, InterruptedException {
        log.info("Creating user with name {} and email {}", name, email);
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        return userRepository.save(user).toFuture().get();
    }

    @DgsMutation
    public Mono<Boolean> deleteUser(long id) {
        log.info("Deleting user with id {}", id);
        return userRepository.existsById(id)
                .flatMap(exists -> {
                    if (exists) {
                        return userRepository.deleteById(id).thenReturn(true);
                    } else {
                        return Mono.error(new IllegalArgumentException("User with id " + id + " does not exist"));
                    }
                })
                .onErrorResume(e -> {
                    log.error("Error deleting user with id {}", id, e);
                    return Mono.error(e);
                });
    }
}
