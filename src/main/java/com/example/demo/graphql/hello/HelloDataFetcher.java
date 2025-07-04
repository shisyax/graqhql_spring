package com.example.demo.graphql.hello;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsQuery;

@DgsComponent
public class HelloDataFetcher {
    @SuppressWarnings("unused")
    @DgsQuery
    public String hello() {
        return "Hello, world!";
    }
}
