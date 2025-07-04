package com.example.demo.graphql.hello;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.IOException;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "spring.main.allow-bean-definition-overriding=true")
public class FibonacciFetcherTest {

    @Autowired
    private WebTestClient webTestClient;

    // fib field test
    @Test
    public void testFibonacci() throws IOException {
        String graphqlQuery = "{ \"query\": \"{ fib(n: 5) }\" }";

        var body = webTestClient.post()
                .uri("/graphql")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(graphqlQuery)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .returnResult().getResponseBody();

        ObjectMapper mapper = new ObjectMapper();
        var tree =mapper.readTree(body);
        int[] expected = {1, 1, 2, 3, 5};
        int[] fibArray;
        try (var parser = tree.at("/data/fib").traverse(mapper)) {
            fibArray = mapper.readValue(parser, int[].class);
        }
        Assertions.assertArrayEquals(expected, fibArray);
    }

}
