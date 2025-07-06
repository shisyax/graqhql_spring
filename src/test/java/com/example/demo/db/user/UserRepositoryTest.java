package com.example.demo.db.user;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@DataR2dbcTest
@Transactional
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Flyway flyway;

    @BeforeAll
    public static void setUp(@Autowired Flyway flyway) {
//        flyway.clean();
        flyway.migrate();
    }

    @Test
    void testSaveAndFindUser() {
        User user = new User(null,
                "June Doe",
                "june@emailo.com");
        var save = userRepository.save(user).block();
        Assertions.assertEquals(user,save);

        Assertions.assertEquals(1, userRepository.count().block());

        var foundUser = userRepository.findById(Objects.requireNonNull(save).getId());
        Assertions.assertTrue(foundUser.blockOptional().isPresent());
    }
}