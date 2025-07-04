package com.example.demo.graphql.hello;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class HelloGraphqlTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void helloQueryReturnsHelloWorld() {
        String graphqlQuery = "{ \"query\": \"{ hello }\" }";

        webTestClient.post()
                .uri("/graphql")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(graphqlQuery)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.data.hello").isEqualTo("Hello, world!");
    }
}
