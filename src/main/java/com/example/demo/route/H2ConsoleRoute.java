package com.example.demo.route;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.net.URI;

@Configuration
@EnableWebFlux
public class H2ConsoleRoute implements WebFluxConfigurer {

    @SuppressWarnings("unused")
    public RouterFunction<ServerResponse> h2ConsoleRouter() {
        return RouterFunctions.route()
                .GET("/h2-console", request ->
                        ServerResponse
                                .temporaryRedirect(URI.create("/h2-console/"))
                                .build())
                .build();
    }
}