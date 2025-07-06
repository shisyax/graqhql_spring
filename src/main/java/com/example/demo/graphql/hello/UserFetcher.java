package com.example.demo.graphql.hello;

import com.example.demo.db.user.User;
import com.example.demo.db.user.UserRepository;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsQuery;
import lombok.AllArgsConstructor;

import java.util.concurrent.ExecutionException;

@DgsComponent
@AllArgsConstructor
public class UserFetcher {

    private UserRepository userRepository;

    @DgsQuery
    public User user(long id) throws ExecutionException, InterruptedException {
        return userRepository.findById(id).toFuture().get();
    }
}
